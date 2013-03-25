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

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.easymock.internal.ReflectionUtils;
import org.junit.Test;

/**
 * @author Henri Tremblay
 */
public class ReflectionUtilsTest {

    public static class B {
        protected void foo(final long l) {
        }
    }

    public static class A extends B {

        public A(final boolean bool, final byte b, final int i, final short s, final char c, final long l,
                final float f, final double d) {
        }

        public A(final int i) {
        }

        protected A(final long l) {
        }

        private A(final byte b) {
        }

        A(final char c) {
        }

        public A(final CharSequence c) {
        }

        public A(final StringBuilder s) {
        }

        public void foo(final String s) {
        }

        public void foo(final int i) {
        }
    }

    @Test
    public void testFindMethod() {
        final Method m = ReflectionUtils.findMethod(String.class, "length");
        assertEquals(String.class, m.getDeclaringClass());
        assertEquals("length", m.getName());
        assertEquals(int.class, m.getReturnType());
    }

    @Test
    public void testFindMethod_NotFound() {
        final Method m = ReflectionUtils.findMethod(String.class, "aaa");
        assertNull(m);
    }

    @Test
    public void testFindMethod_Ambiguous() {
        try {
            ReflectionUtils.findMethod(A.class, "foo");
        } catch (final RuntimeException e) {
            assertEquals("Ambiguous name: More than one method are named foo", e.getMessage());
        }
    }

    @Test
    public void testFindMethod_WrongParams() {
        final Method m = ReflectionUtils.findMethod(A.class, "foo", int.class, int.class);
        assertNull(m);
    }

    @Test
    public void testFindMethod_Superclass() {
        final Method m = ReflectionUtils.findMethod(A.class, "foo", long.class);
        assertEquals("protected void " + B.class.getName() + ".foo(long)", m.toString());
    }

    @Test
    public void testFindMethodClassOfQStringClassOfQArray() {
        final Method m = ReflectionUtils.findMethod(A.class, "foo", int.class);
        assertEquals("public void " + A.class.getName() + ".foo(int)", m.toString());
    }

    @Test
    public void testGetConstructor_public() throws NoSuchMethodException {
        final Constructor<A> c = ReflectionUtils.getConstructor(A.class, 5);
        assertArrayEquals(new Class[] { int.class }, c.getParameterTypes());
    }

    @Test
    public void testGetConstructor_protected() throws NoSuchMethodException {
        final Constructor<A> c = ReflectionUtils.getConstructor(A.class, 5l);
        assertArrayEquals(new Class[] { long.class }, c.getParameterTypes());
    }

    @Test
    public void testGetConstructor_default() throws NoSuchMethodException {
        final Constructor<A> c = ReflectionUtils.getConstructor(A.class, 'c');
        assertArrayEquals(new Class[] { char.class }, c.getParameterTypes());
    }

    @Test(expected = NoSuchMethodException.class)
    public void testGetConstructor_private() throws NoSuchMethodException {
        ReflectionUtils.getConstructor(A.class, (byte) 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetConstructor_twoMatching() throws NoSuchMethodException {
        ReflectionUtils.getConstructor(A.class, new StringBuilder());
    }

    @Test(expected = NoSuchMethodException.class)
    public void testGetConstructor_notFound() throws NoSuchMethodException {
        ReflectionUtils.getConstructor(A.class, true);
    }

    @Test(expected = NoSuchMethodException.class)
    public void testGetConstructor_WrongParams() throws NoSuchMethodException {
        ReflectionUtils.getConstructor(A.class, "", "");
    }

    @Test
    public void testGetConstructor_AllPrimitives() throws NoSuchMethodException {
        final Constructor<A> c = ReflectionUtils.getConstructor(A.class, true, (byte) 1, 2, (short) 3, 'g',
                5l, 4.0f, 8.0);
        assertNotNull(c);
    }

    @Test
    public void testGetDeclareMethod_Found() throws Exception {
        final Method expected = A.class.getDeclaredMethod("foo", new Class<?>[] { int.class });
        final Method actual = ReflectionUtils.getDeclaredMethod(A.class, "foo", new Class<?>[] { int.class });
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDeclareMethod_NotFound() {
        try {
            ReflectionUtils.getDeclaredMethod(A.class, "foo", new Class<?>[0]);
            fail("Method should not be found");
        } catch (final RuntimeException e) {
            assertTrue(e.getCause() instanceof NoSuchMethodException);
        }
    }
}
