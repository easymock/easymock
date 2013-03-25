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

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Henri Tremblay
 */
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

    private void testConstructor(final Class<? extends FooClass> mockedClass) {
        final FooClass mock = createMock(mockedClass);
        assertTrue(mockedClass.isAssignableFrom(mock.getClass()));
        mock.foo();
        expectLastCall();
        replay(mock);
        mock.foo();
        verify(mock);
    }

    /**
     * Test if a class with an empty constructor is mocked correctly.
     */
//    @Test
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
