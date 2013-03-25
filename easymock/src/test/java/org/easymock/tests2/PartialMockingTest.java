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
package org.easymock.tests2;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import org.easymock.ConstructorArgs;
import org.junit.Test;

/**
 * @author Henri Tremblay
 */
public class PartialMockingTest {

    public static abstract class A {

        public String s;

        public int i;

        protected A(final String s) {
            this.s = s;
        }

        private A(final int i) {
            this.i = i;
        }

        protected abstract int foo();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testPartialMock_PublicConstructor() throws Exception {
        final ArrayList<String> list = createMockBuilder(ArrayList.class).withConstructor(3).createMock();
        list.add("test"); // shouldn't crash since constructor was called
    }

    @Test
    public void testPartialMock_ProtectedConstructor() throws Exception {
        final A a = createMockBuilder(A.class).withConstructor("test").createMock();
        assertEquals("test", a.s); // make sure constructor was called

        // Check that abstract method is mocked by default
        expect(a.foo()).andReturn(3);
        replay(a);
        assertEquals(3, a.foo());
        verify(a);
    }

    @Test(expected = RuntimeException.class)
    public void testPartialMock_ConstructorNotFound() throws Exception {
        final Constructor<?> cstr = ArrayList.class.getConstructor(Integer.TYPE);
        final ConstructorArgs constructorArgs = new ConstructorArgs(cstr, 2.0);
        try {
            createMockBuilder(ArrayList.class).withConstructor(Integer.TYPE).withArgs(2.0).createMock();
        } catch (final RuntimeException e) {
            assertEquals("Failed to find constructor for param types", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPartialMock_InvalidParams() throws Exception {
        final Constructor<?> cstr = ArrayList.class.getConstructor(Integer.TYPE);
        final ConstructorArgs constructorArgs = new ConstructorArgs(cstr, "test");
        createMockBuilder(ArrayList.class).withConstructor(Integer.TYPE).withArgs("test");
    }

    @Test(expected = RuntimeException.class)
    public void testPartialMock_ExceptionInConstructor() throws Exception {
        final Constructor<?> cstr = ArrayList.class.getConstructor(Integer.TYPE);
        final ConstructorArgs constructorArgs = new ConstructorArgs(cstr, -5);
        createMockBuilder(ArrayList.class).withConstructor(-5).createMock();
    }
}
