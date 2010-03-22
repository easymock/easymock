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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class ConstructorTest {

    public static class FooClass {
        public void foo() {
            // Since it's always mocked, it should never be called
            fail();
        }
    }

    public static class EmptyConstructorClass extends FooClass {
    }

    public static class ConstructorCallingPublicMethodClass extends FooClass {

        public ConstructorCallingPublicMethodClass() {
            foo();
        }
    }

    private void testConstructor(Class<? extends FooClass> mockedClass) {
        MockControl<? extends FooClass> ctrl = MockClassControl
                .createControl(mockedClass);
        FooClass mock = ctrl.getMock();
        assertTrue(mockedClass.isAssignableFrom(mock.getClass()));
        mock.foo();
        ctrl.setVoidCallable();
        ctrl.replay();
        mock.foo();
        ctrl.verify();
    }

    /**
     * Test if a class with an empty constructor is mocked correctly.
     */
    @Test
    public void emptyConstructor() {
        testConstructor(EmptyConstructorClass.class);
    }

    /**
     * Test that a constructor calling a mocked method (in this case a public
     * one) is mocked correctly. The expected behavior is that the mocked method
     * won't be called and just be ignored
     */
    @Test
    public void constructorCallingPublicMethod() {
        testConstructor(ConstructorCallingPublicMethodClass.class);
    }
}
