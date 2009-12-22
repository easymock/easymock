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
package org.easymock.tests2;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.easymock.internal.ReflectionUtils;
import org.junit.Test;

public class ReflectionUtilsTest {

    public static class B {
        protected void foo(long l) {
        }
    }

    public static class A extends B {

        public A(boolean bool, byte b, int i, short s, char c, long l, float f,
                double d) {
        }

        public A(int i) {
        }

        protected A(long l) {
        }

        private A(byte b) {
        }

        A(char c) {
        }

        public A(CharSequence c) {
        }

        public A(StringBuilder s) {
        }

        public void foo(String s) {
        }

        public void foo(int i) {
        }
    }

    @Test
    public void testFindMethod() {
        Method m = ReflectionUtils.findMethod(String.class, "length");
        assertEquals("public int java.lang.String.length()", m.toString());
    }

    @Test
    public void testFindMethod_NotFound() {
        Method m = ReflectionUtils.findMethod(String.class, "aaa");
        assertNull(m);
    }

    @Test
    public void testFindMethod_Ambiguous() {
        try {
            ReflectionUtils.findMethod(A.class, "foo");
        }
        catch(RuntimeException e) {
            assertEquals("Ambiguous name: More than one method are named foo", e.getMessage());
        }
    }

    @Test
    public void testFindMethod_WrongParams() {
        Method m = ReflectionUtils.findMethod(A.class, "foo", int.class,
                int.class);
        assertNull(m);
    }

    @Test
    public void testFindMethod_Superclass() {
        Method m = ReflectionUtils.findMethod(A.class, "foo", long.class);
        assertEquals("protected void " + B.class.getName() + ".foo(long)", m
                .toString());
    }

    @Test
    public void testFindMethodClassOfQStringClassOfQArray() {
        Method m = ReflectionUtils.findMethod(A.class, "foo", int.class);
        assertEquals("public void " + A.class.getName() + ".foo(int)", m
                .toString());
    }

    @Test
    public void testGetConstructor_public() throws NoSuchMethodException {
        Constructor<A> c = ReflectionUtils.getConstructor(A.class, 5);
        assertArrayEquals(new Class[] { int.class }, c.getParameterTypes());
    }

    @Test
    public void testGetConstructor_protected() throws NoSuchMethodException {
        Constructor<A> c = ReflectionUtils.getConstructor(A.class, 5l);
        assertArrayEquals(new Class[] { long.class }, c.getParameterTypes());
    }

    @Test
    public void testGetConstructor_default() throws NoSuchMethodException {
        Constructor<A> c = ReflectionUtils.getConstructor(A.class, 'c');
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
        Constructor<A> c = ReflectionUtils.getConstructor(A.class, true,
                (byte) 1, 2,
                (short) 3, 'g', 5l, 4.0f, 8.0);
        assertNotNull(c);
    }
}
