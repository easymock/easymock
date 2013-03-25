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

import java.util.AbstractList;

import org.junit.Test;

/**
 * Test the limitations of class mocking
 * 
 * @author Henri Tremblay
 */
public class LimitationsTest {

    public static class MyClass {
        public final int foo() {
            throw new RuntimeException();
        }
    }

    public static class PrivateClass {
        private PrivateClass() {
        }
    }

    public static class NativeClass {
        public native int foo();
    }

    public void finalClass() {
        try {
            createMock(String.class);
            fail("Magic, we can mock a final class");
        } catch (final Exception e) {
        }
    }

    @Test
    public void abstractClass() {
        final Object o = createMock(AbstractList.class);
        assertTrue(o instanceof AbstractList<?>);
    }

    @Test
    public void mockFinalMethod() {
        final MyClass c = createMock(MyClass.class);

        try {
            c.foo();
            fail("Final method shouldn't be mocked");
        } catch (final Exception e) {
        }
    }

    @Test
    public void privateConstructor() {
        createMock(PrivateClass.class);
    }

    @Test
    public void mockNativeMethod() {
        final NativeClass mock = createMock(NativeClass.class);
        expect(mock.foo()).andReturn(1);
        replay(mock);
        assertEquals(1, mock.foo());
        verify(mock);
    }
}
