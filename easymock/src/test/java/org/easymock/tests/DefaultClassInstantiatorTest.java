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
package org.easymock.tests;

import java.io.Serializable;

import org.easymock.internal.ClassInstantiatorFactory;
import org.easymock.internal.DefaultClassInstantiator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * Class testing the default instantiator. I'm cheating a little bit here since
 * I'm not unit testing directly the class. The reason I'm doing this is that I
 * want to make sure it works well with the cglib class and not the actual
 * mocked class.
 * 
 * @author Henri Tremblay
 */
public class DefaultClassInstantiatorTest {

    public static class PrimitiveParamClass {
        public PrimitiveParamClass(final int i) {
        }
    }

    public static class FinalParamClass {
        public FinalParamClass(final String i) {
        }
    }

    public static class ProtectedConstructorClass {
        protected ProtectedConstructorClass() {
        }
    }

    public static class ProtectedWithPrimitiveConstructorClass {
        protected ProtectedWithPrimitiveConstructorClass(final int i) {
        }
    }

    public static class ParamClass {
        public ParamClass(final FinalParamClass f) {
        }
    }

    public static class ObjectClass {
        public ObjectClass(final Object c) {
        }
    }

    public static class ObjectParamClass {
        public ObjectParamClass(final ParamClass c) {
        }
    }

    public static class PrivateConstructorClass {
        private PrivateConstructorClass() {
        }
    }

    public static class ConstructorWithCodeClass {
        public ConstructorWithCodeClass() {
            throw new RuntimeException();
        }
    }

    @SuppressWarnings("serial")
    public static class SerializableClass implements Serializable {
        public SerializableClass() {
            throw new RuntimeException();
        }
    }

    public static class SerializableWithUIDClass implements Serializable {

        private static final long serialVersionUID = -1;

        public SerializableWithUIDClass() {
            throw new RuntimeException();
        }
    }

    @SuppressWarnings("serial")
    public static class BadlyDoneSerializableClass implements Serializable {

        private final long serialVersionUID = 2; // not static

        public BadlyDoneSerializableClass() {
            throw new RuntimeException();
        }
    }

    private final String vendor = null;

    @BeforeClass
    public static void setUp() {
        // Set the default instantiator
        ClassInstantiatorFactory.setInstantiator(new DefaultClassInstantiator());
    }

    @AfterClass
    public static void tearDown() {
        // Set the value back to be clean
        ClassInstantiatorFactory.setDefaultInstantiator();
    }

    @Test
    public void emptyConstructor() {
        checkInstantiation(DefaultClassInstantiator.class);
    }

    @Test
    public void primitiveType() {
        checkInstantiation(PrimitiveParamClass.class);
    }

    @Test
    @Ignore // Fails on Java 7 for a currently unknown reason
    public void finalType() {
        checkInstantiation(FinalParamClass.class);
    }

    @Test
    public void protectedConstructor() {
        checkInstantiation(ProtectedConstructorClass.class);
    }

    @Test
    public void protectedWithPrimitiveConstructor() {
        checkInstantiation(ProtectedWithPrimitiveConstructorClass.class);
    }

    @Test
    public void object() {
        checkInstantiation(ObjectClass.class);
    }

    @Test
    @Ignore // Fails on Java 7 for a currently unknown reason
    public void objectParamRecursion() {
        checkInstantiation(ObjectParamClass.class);
    }

    @Test
    public void constructorWithCodeLimitation() {
        try {
            createMock(ConstructorWithCodeClass.class);
            fail("Shouldn't be possible to mock, code in constructor should crash");
        } catch (final Exception e) {
        }
    }

    @Test
    public void privateConstructorLimitation() {
        try {
            createMock(PrivateConstructorClass.class);
            fail("Shouldn't be able to mock a class with a private constructor using DefaultInstantiator");
        } catch (final Exception e) {
        }
    }

    @Test
    public void privateConstructor() {
        final DefaultClassInstantiator instantiator = new DefaultClassInstantiator();
        try {
            instantiator.newInstance(PrivateConstructorClass.class);
            fail("Shouldn't be able to mock a class with a private constructor using DefaultInstantiator");
        } catch (final Exception e) {
        }
    }

    @Test
    public void newInstance() {
        checkInstantiation(DefaultClassInstantiator.class);
    }

    @Test
    public void serializable() {
        checkInstantiation(SerializableClass.class);
    }

    @Test
    public void badSerializable() throws Exception {
        final DefaultClassInstantiator instantiator = new DefaultClassInstantiator();
        assertTrue(instantiator.newInstance(BadlyDoneSerializableClass.class) instanceof BadlyDoneSerializableClass);
    }

    @Test
    public void serializableWithUID() throws Exception {
        final DefaultClassInstantiator instantiator = new DefaultClassInstantiator();
        assertTrue(instantiator.newInstance(SerializableWithUIDClass.class) instanceof SerializableWithUIDClass);
    }

    private <T> void checkInstantiation(final Class<T> clazz) {
        final T mock = createMock(clazz);
        assertTrue(clazz.isAssignableFrom(mock.getClass()));
    }
}
