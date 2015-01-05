/**
 * Copyright 2001-2015 the original author or authors.
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

import static java.util.Arrays.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.easymock.*;

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
     * <code>replayAll/verifyAll</code> to work afterwards
     * @param host the object on which to inject mocks
     * @since 3.2
     */
    public static void injectMocks(final Object host) {

        final InjectionPlan injectionPlan = new InjectionPlan();

        Class<?> hostClass = host.getClass();
        while (hostClass != Object.class) {
            createMocksForAnnotations(hostClass, host, injectionPlan);
            hostClass = hostClass.getSuperclass();
        }

        for (final Field f : injectionPlan.getTestSubjectFields()) {
            f.setAccessible(true);
            Object testSubject;
            try {
                testSubject = f.get(host);
            } catch (final IllegalAccessException e) {
                // ///CLOVER:OFF
                throw new RuntimeException(e);
                // ///CLOVER:ON
            }
            Class<?> testSubjectClass = testSubject.getClass();
            while (testSubjectClass != Object.class) {
                injectMocksOnClass(testSubjectClass, testSubject, injectionPlan);
                testSubjectClass = testSubjectClass.getSuperclass();
            }
        }

        // Check for unsatisfied qualified injections only after having scanned all TestSubjects and their superclasses
        for (final Injection injection : injectionPlan.getQualifiedInjections()) {
            if (!injection.isMatched()) {
                throw new RuntimeException(
                        String.format("Unsatisfied qualifier: '%s'", injection.getAnnotation().fieldName()));
            }
        }
    }

    /**
     * Create the mocks and find the fields annotated with {@link TestSubject}
     *
     * @param hostClass class to search
     * @param host object of the class
     * @param injectionPlan output parameter where the created mocks andfields to inject are added
     */
    private static void createMocksForAnnotations(final Class<?> hostClass, final Object host,
            final InjectionPlan injectionPlan) {
        final Field[] fields = hostClass.getDeclaredFields();
        for (final Field f : fields) {
            final TestSubject ima = f.getAnnotation(TestSubject.class);
            if (ima != null) {
                injectionPlan.addTestSubjectField(f);
                continue;
            }
            final Mock annotation = f.getAnnotation(Mock.class);
            if (annotation == null) {
                continue;
            }
            final Class<?> type = f.getType();
            String name = annotation.name();
            // Empty string means we are on the default value which we means no name (aka null) from the EasyMock point of view
            name = (name.length() == 0 ? null : name);

            final MockType mockType = annotation.type();
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
            } catch (final IllegalAccessException e) {
                // ///CLOVER:OFF
                throw new RuntimeException(e);
                // ///CLOVER:ON
            }

            injectionPlan.addInjection(new Injection(mock, annotation));
        }
    }

    /**
     * Try to inject a mock to every fields in the class
     *
     * @param clazz class where the fields are taken
     * @param obj object being a instance of clazz
     * @param injectionPlan details of possible mocks for injection
     */
    private static void injectMocksOnClass(final Class<?> clazz, final Object obj,
            final InjectionPlan injectionPlan) {

        final List<Field> fields = injectByName(clazz, obj, injectionPlan.getQualifiedInjections());
        injectByType(obj, fields, injectionPlan.getUnqualifiedInjections());
    }

    private static List<Field> injectByName(final Class<?> clazz, final Object obj,
            final List<Injection> qualifiedInjections) {

        final List<Field> fields = fieldsOf(clazz);

        for (final Injection injection : qualifiedInjections) {

            final Field f = getFieldByName(clazz, injection.getQualifier());
            final InjectionTarget target = injectionTargetWithField(f);
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

    private static void injectByType(final Object obj, final List<Field> fields,
            final List<Injection> injections) {

        for (final Field f : fields) {

            final InjectionTarget target = injectionTargetWithField(f);
            if (target == null) {
                continue;
            }

            final Injection toAssign = findUniqueAssignable(injections, target);
            if (toAssign == null) {
                continue;
            }

            target.inject(obj, toAssign);
        }
    }

    private static List<Field> fieldsOf(final Class<?> clazz) {
        final List<Field> fields = new ArrayList<Field>();
        fields.addAll(asList(clazz.getDeclaredFields()));
        return fields;
    }

    private static Field getFieldByName(final Class<?> clazz, final String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (final NoSuchFieldException e) {
            return null;
        } catch (final SecurityException e) {
            // ///CLOVER:OFF
            return null;
            // ///CLOVER:ON
        }
    }

    private static InjectionTarget injectionTargetWithField(final Field f) {
        if (shouldNotAssignTo(f)) {
            return null;
        }
        return new InjectionTarget(f);
    }

    private static boolean shouldNotAssignTo(final Field f) {
        // Skip final or static fields
        return f == null || (f.getModifiers() & (Modifier.STATIC + Modifier.FINAL)) != 0;
    }

    private static Injection findUniqueAssignable(final List<Injection> injections,
            final InjectionTarget target) {
        Injection toAssign = null;
        for (final Injection injection : injections) {
            if (target.accepts(injection)) {
                if (toAssign != null) {
                    throw new RuntimeException(
                            String.format("At least two mocks can be assigned to '%s': %s and %s",
                                    target.getTargetField(), toAssign.getMock(), injection.getMock()));
                }
                toAssign = injection;
            }
        }
        return toAssign;
    }

}
