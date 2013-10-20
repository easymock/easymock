/**
 * Copyright 2001-2013 the original author or authors.
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
     *     <li>If a mock can be assigned to a field, do it. The same mock an be assigned more than once</li>
     *     <li>If no mock can be assigned to a field, skip it silently</li>
     *     <li>If two mocks can be assigned to the same field, return an error</li>
     *     <li>A mock can be assigned if the type is compatible and, where a qualifier is set, the qualifier matches the field name
     * </ul>
     * Fields are searched recursively on the superclasses
     * <p>
     * <b>Note:</b> If the parameter extends {@link EasyMockSupport}, the mocks will be created using it to allow
     * <code>replayAll/verifyAll</code> to work afterwards
     * @param host the object on which to inject mocks
     * @since 3.2
     */
    public static void injectMocks(final Object host) {
        final List<Field> testSubjectFields = new ArrayList<Field>(1);
        final List<Injection> injections = new ArrayList<Injection>(5);

        Class<?> hostClass = host.getClass();
        while (hostClass != Object.class) {
            createMocksForAnnotations(hostClass, host, injections, testSubjectFields);
            hostClass = hostClass.getSuperclass();
        }

        for (final Field f : testSubjectFields) {
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
                injectMocksOnClass(testSubjectClass, testSubject, injections);
                testSubjectClass = testSubjectClass.getSuperclass();
            }
        }
    }

    /**
     * Create the mocks and find the fields annotated with {@link TestSubject}
     *
     * @param hostClass class to search
     * @param host object of the class
     * @param injections output parameter where the created mocks are added
     * @param testSubjectFields output parameter where the fields to inject are added
     */
    private static void createMocksForAnnotations(final Class<?> hostClass, final Object host,
            final List<Injection> injections, final List<Field> testSubjectFields) {
        final Field[] fields = hostClass.getDeclaredFields();
        for (final Field f : fields) {
            final TestSubject ima = f.getAnnotation(TestSubject.class);
            if (ima != null) {
                testSubjectFields.add(f);
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
            Object o;
            if (host instanceof EasyMockSupport) {
                o = ((EasyMockSupport) host).createMock(name, mockType, type);
            }
            else {
                o = EasyMock.createMock(name, mockType, type);
            }
            f.setAccessible(true);
            try {
                f.set(host, o);
            } catch (final IllegalAccessException e) {
                // ///CLOVER:OFF
                throw new RuntimeException(e);
                // ///CLOVER:ON
            }
            injections.add(new Injection(o, annotation));
        }
    }

    /**
     * Try to inject a mock to every fields in the class
     *
     * @param clazz class where the fields are taken
     * @param obj object being a instance of clazz
     * @param injections list of possible mocks for injection
     */
    private static void injectMocksOnClass(final Class<?> clazz, final Object obj,
            final List<Injection> injections) {

        final Field[] fields = clazz.getDeclaredFields();

        for (final Field f : fields) {

            // Skip final or static fields
            if ((f.getModifiers() & (Modifier.STATIC + Modifier.FINAL)) != 0) {
                continue;
            }

            final InjectionTarget target = new InjectionTarget(f);

            for (final Injection injection : injections) {
                if (target.accepts(injection)) {
                    target.setInjection(injection);
                }
            }

            target.inject(obj);
        }
    }

}
