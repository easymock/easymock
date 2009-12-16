/**
 * Copyright 2003-2009 OFFIS, Henri Tremblay
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
package org.easymock.classextension.tests;

import static org.junit.Assert.*;

import java.io.Serializable;

import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import org.easymock.classextension.internal.ClassInstantiatorFactory;
import org.easymock.classextension.internal.DefaultClassInstantiator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Class testing the default instantiator. I'm cheating a little bit here since
 * I'm not unit testing directly the class. The reason I'm doing this is that I
 * want to make sure it works well with the cglib class and not the actual
 * mocked class.
 */
@SuppressWarnings("deprecation")
public class DefaultClassInstantiatorTest {

    public static class PrimitiveParamClass {
        public PrimitiveParamClass(int i) {
        }
    }

    public static class FinalParamClass {
        public FinalParamClass(String i) {
        }
    }

    public static class ProtectedConstructorClass {
        protected ProtectedConstructorClass() {
        }
    }

    public static class ProtectedWithPrimitiveConstructorClass {
        protected ProtectedWithPrimitiveConstructorClass(int i) {
        }
    }

    public static class ParamClass {
        public ParamClass(FinalParamClass f) {
        }
    }

    public static class ObjectParamClass {
        public ObjectParamClass(ParamClass c) {
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

    @SuppressWarnings("serial")
    public static class BadlyDoneSerializableClass implements Serializable {

        private final long serialVersionUID = 2; // not static

        public BadlyDoneSerializableClass() {
            throw new RuntimeException();
        }
    }

    private final String vendor = null;

    @BeforeClass
    public static void setUp() throws Exception {
        // Set the default instantiator
        ClassInstantiatorFactory
                .setInstantiator(new DefaultClassInstantiator());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        // Set the value back to be clean
        ClassInstantiatorFactory.setDefaultInstantiator();
    }

    @Test
    public void emptyConstructor() throws Exception {
        MockControl<DefaultClassInstantiator> ctrl = MockClassControl
                .createControl(DefaultClassInstantiator.class);
        assertTrue(ctrl.getMock() instanceof DefaultClassInstantiator);
    }

    @Test
    public void primitiveType() throws Exception {
        MockControl<PrimitiveParamClass> ctrl = MockClassControl
                .createControl(PrimitiveParamClass.class);
        assertTrue(ctrl.getMock() instanceof PrimitiveParamClass);
    }

    @Test
    public void finalType() throws Exception {
        MockControl<FinalParamClass> ctrl = MockClassControl
                .createControl(FinalParamClass.class);
        assertTrue(ctrl.getMock() instanceof FinalParamClass);
    }

    @Test
    public void protectedConstructor() throws Exception {
        MockControl<ProtectedConstructorClass> ctrl = MockClassControl
                .createControl(ProtectedConstructorClass.class);
        assertTrue(ctrl.getMock() instanceof ProtectedConstructorClass);
    }

    @Test
    public void protectedWithPrimitiveConstructor() throws Exception {
        MockControl<ProtectedWithPrimitiveConstructorClass> ctrl = MockClassControl
                .createControl(ProtectedWithPrimitiveConstructorClass.class);
        assertTrue(ctrl.getMock() instanceof ProtectedWithPrimitiveConstructorClass);
    }

    @Test
    public void objectParamRecusion() throws Exception {
        MockControl<ObjectParamClass> ctrl = MockClassControl
                .createControl(ObjectParamClass.class);
        assertTrue(ctrl.getMock() instanceof ObjectParamClass);
    }

    @Test
    public void constructorWithCodeLimitation() {
        try {
            MockClassControl.createControl(ConstructorWithCodeClass.class);
            fail("Shouldn't be possible to mock, code in constructor should crash");
        } catch (Exception e) {
        }
    }

    @Test
    public void privateConstructorLimitation() {
        try {
            MockClassControl.createControl(PrivateConstructorClass.class);
            fail("Shouldn't be able to mock a class with a private constructor using DefaultInstantiator");
        } catch (Exception e) {
        }
    }

    @Test
    public void privateConstructor() {
        DefaultClassInstantiator instantiator = new DefaultClassInstantiator();
        try {
            instantiator.newInstance(PrivateConstructorClass.class);
            fail("Shouldn't be able to mock a class with a private constructor using DefaultInstantiator");
        } catch (Exception e) {
        }
    }

    @Test
    public void newInstance() throws Exception {
        MockControl<DefaultClassInstantiator> ctrl = MockClassControl
                .createControl(DefaultClassInstantiator.class);
        assertTrue(ctrl.getMock() instanceof DefaultClassInstantiator);
    }

    @Test
    public void serializable() {
        MockControl<SerializableClass> ctrl = MockClassControl
                .createControl(SerializableClass.class);
        assertTrue(ctrl.getMock() instanceof SerializableClass);
    }

    @Test
    public void badSerializable() throws Exception {
        DefaultClassInstantiator instantiator = new DefaultClassInstantiator();
        instantiator.newInstance(BadlyDoneSerializableClass.class);
    }
}
