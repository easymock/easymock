/*
 * Copyright 2001-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.easymock.internal;

import org.easymock.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.*;

/**
 * Performs creation of mocks and injection into test subjects in accordance with annotations present in the host object.
 *
 * @author Henri Tremblay
 * @author Alistair Todd
 * @since 3.3
 */
public class Injector {

    /**
     * Inject a mock to every fields annotated with {@link Mock} on the class passed
     * in parameter. Then, inject these mocks to the fields of every class annotated with {@link TestSubject}.
     * <p>
     * The rules are
     * <ul>
     *     <li>Static and final fields are ignored</li>
     *     <li>If two mocks have the same field name, return an error</li>
     *     <li>If a mock has a field name and no matching field is found, return an error</li>
     * </ul>
     * Then, ignoring all fields and mocks matched by field name
     * <ul>
     *     <li>If a mock without field name can be assigned to a field, do it. The same mock can be assigned more than once</li>
     *     <li>If no mock can be assigned to a field, skip the field silently</li>
     *     <li>If the mock cannot be assigned to any field, skip the mock silently</li>
     *     <li>If two mocks can be assigned to the same field, return an error</li>
     * </ul>
     * Fields are searched recursively on the superclasses
     * <p>
     * <b>Note:</b> If the parameter extends {@link EasyMockSupport}, the mocks will be created using it to allow
     * {@code replayAll/verifyAll} to work afterwards
     * @param host the object on which to inject mocks
     * @since 3.2
     */
    public static void injectMocks(Object host) {

        InjectionPlan injectionPlan = new InjectionPlan();

        Class<?> hostClass = host.getClass();
        while (hostClass != Object.class) {
            createMocksForAnnotations(hostClass, host, injectionPlan);
            hostClass = hostClass.getSuperclass();
        }

        for (Field f : injectionPlan.getTestSubjectFields()) {
            f.setAccessible(true);
            Object testSubject;
            try {
                testSubject = f.get(host);
            } catch (IllegalAccessException e) {
                // ///CLOVER:OFF
                throw new AssertionError(e);
                // ///CLOVER:ON
            }
            if (testSubject == null) {
                // The attribute isn't initialized, try to call the default constructor
                testSubject = instantiateTestSubject(f);
                try {
                    f.setAccessible(true);
                    f.set(host, testSubject);
                } catch (ReflectiveOperationException e) {
                    throw new AssertionError("Failed to assign the created TestSubject to  " + f.getName(), e);
                }
            }
            Class<?> testSubjectClass = testSubject.getClass();
            while (testSubjectClass != Object.class) {
                injectMocksOnClass(testSubjectClass, testSubject, injectionPlan);
                testSubjectClass = testSubjectClass.getSuperclass();
            }
        }

        // Check for unsatisfied qualified injections only after having scanned all TestSubjects and their superclasses
        for (Injection injection : injectionPlan.getQualifiedInjections()) {
            if (!injection.isMatched()) {
                throw new AssertionError(
                        String.format("Unsatisfied qualifier: '%s'", injection.getAnnotation().fieldName()));
            }
        }
    }

    static <T> T instantiateTestSubject(Field f) {
        T testSubject;
        @SuppressWarnings("unchecked")
        Class<T> type = (Class<T>) f.getType();
        if(type.isMemberClass() && !Modifier.isStatic(type.getModifiers())) {
            throw new AssertionError("TestSubject is an inner class. You need to instantiate '" + f.getName() + "' manually");
        }
        Constructor<T> defaultConstructor;
        try {
            defaultConstructor = type.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new AssertionError("TestSubject is null and has no default constructor. You need to instantiate '" + f.getName() + "' manually");
        }
        defaultConstructor.setAccessible(true);
        try {
            testSubject = defaultConstructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("TestSubject is null and default constructor fails on invocation. You need to instantiate '" + f.getName() + "' manually", e);
        }
        return testSubject;
    }

    /**
     * Create the mocks and find the fields annotated with {@link TestSubject}
     *
     * @param hostClass class to search
     * @param host object of the class
     * @param injectionPlan output parameter where the created mocks and fields to inject are added
     */
    private static void createMocksForAnnotations(Class<?> hostClass, Object host,
            InjectionPlan injectionPlan) {
        Field[] fields = hostClass.getDeclaredFields();
        for (Field f : fields) {
            TestSubject ima = f.getAnnotation(TestSubject.class);
            if (ima != null) {
                injectionPlan.addTestSubjectField(f);
                continue;
            }
            Mock annotation = f.getAnnotation(Mock.class);
            if (annotation == null) {
                continue;
            }
            Class<?> type = f.getType();
            String name = annotation.name();
            // Empty string means we are on the default value which we means no name (aka null) from the EasyMock point of view
            name = (name.length() == 0 ? null : name);

            MockType mockType = mockTypeFromAnnotation(annotation);
            Object mock;
            if (host instanceof EasyMockSupport) {
                mock = ((EasyMockSupport) host).createMock(name, mockType, type);
            }
            else {
                mock = EasyMock.createMock(name, mockType, type);
            }
            f.setAccessible(true);
            try {
                f.set(host, mock);
            } catch (IllegalAccessException e) {
                // ///CLOVER:OFF
                throw new RuntimeException(e);
                // ///CLOVER:ON
            }

            injectionPlan.addInjection(new Injection(mock, annotation));
        }
    }

    private static MockType mockTypeFromAnnotation(Mock annotation) {
        MockType valueMockType = annotation.value();
        MockType mockType = annotation.type();
        if(valueMockType != MockType.DEFAULT && mockType != MockType.DEFAULT) {
            throw new AssertionError("@Mock.value() and @Mock.type() are aliases, you can't specify both at the same time");
        }
        if(valueMockType != MockType.DEFAULT) {
            mockType = valueMockType;
        }
        return mockType;
    }

    /**
     * Try to inject a mock to every fields in the class
     *
     * @param clazz class where the fields are taken
     * @param obj object being a instance of clazz
     * @param injectionPlan details of possible mocks for injection
     */
    private static void injectMocksOnClass(Class<?> clazz, Object obj,
            InjectionPlan injectionPlan) {

        List<Field> fields = injectByName(clazz, obj, injectionPlan.getQualifiedInjections());
        injectByType(obj, fields, injectionPlan.getUnqualifiedInjections());
    }

    private static List<Field> injectByName(Class<?> clazz, Object obj,
            List<Injection> qualifiedInjections) {

        List<Field> fields = fieldsOf(clazz);

        for (Injection injection : qualifiedInjections) {

            Field f = getFieldByName(clazz, injection.getQualifier());
            InjectionTarget target = injectionTargetWithField(f);
            if (target == null) {
                continue;
            }

            if (target.accepts(injection)) {
                target.inject(obj, injection);
                fields.remove(target.getTargetField());
            }
        }

        return fields;
    }

    private static void injectByType(Object obj, List<Field> fields,
            List<Injection> injections) {

        for (Field f : fields) {

            InjectionTarget target = injectionTargetWithField(f);
            if (target == null) {
                continue;
            }

            Injection toAssign = findUniqueAssignable(injections, target);
            if (toAssign == null) {
                continue;
            }

            target.inject(obj, toAssign);
        }
    }

    private static List<Field> fieldsOf(Class<?> clazz) {
        return new ArrayList<>(asList(clazz.getDeclaredFields()));
    }

    private static Field getFieldByName(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException | SecurityException e) {
            return null;
        }
    }

    private static InjectionTarget injectionTargetWithField(Field f) {
        if (shouldNotAssignTo(f)) {
            return null;
        }
        return new InjectionTarget(f);
    }

    private static boolean shouldNotAssignTo(Field f) {
        // Skip final or static fields
        return f == null || (f.getModifiers() & (Modifier.STATIC + Modifier.FINAL)) != 0;
    }

    private static Injection findUniqueAssignable(List<Injection> injections,
            InjectionTarget target) {
        Injection toAssign = null;
        for (Injection injection : injections) {
            if (target.accepts(injection)) {
                if (toAssign != null) {
                    throw new AssertionError(
                            String.format("At least two mocks can be assigned to '%s': %s and %s",
                                    target.getTargetField(), toAssign.getMock(), injection.getMock()));
                }
                toAssign = injection;
            }
        }
        return toAssign;
    }

}
