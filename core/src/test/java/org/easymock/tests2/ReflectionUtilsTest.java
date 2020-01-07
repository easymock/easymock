/*
 * Copyright 2001-2020 the original author or authors.
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

import org.easymock.internal.ReflectionUtils;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.easymock.internal.ReflectionUtils.*;
import static org.junit.Assert.*;

/**
 * @author Henri Tremblay
 */
public class ReflectionUtilsTest {

    private static final Class[] NO_PARAMS = new Class[0];

    public static class B {
        protected void foo(long l) { }

        public void parentMethod() {}
    }

    public static class A extends B {

        public A(boolean bool, byte b, int i, short s, char c, long l, float f, double d) { }

        public A(int i) { }

        protected A(long l) { }

        private A(byte b) { }

        A(char c) { }

        public A(CharSequence c) { }

        public A(StringBuilder s) { }

        public void foo(int i) {}

        public static void staticMethod() {}

        private void privateMethod() {}

        protected void protectedMethod() {}

        void packageMethod() {}
    }

    @Test
    public void testGetConstructor_public() throws NoSuchMethodException {
        Constructor<A> c = ReflectionUtils.getConstructor(A.class, 5);
        assertArrayEquals(new Class[] { int.class }, c.getParameterTypes());
    }

    @Test
    public void testGetConstructor_protected() throws NoSuchMethodException {
        Constructor<A> c = ReflectionUtils.getConstructor(A.class, 5L);
        assertArrayEquals(new Class[] { long.class }, c.getParameterTypes());
    }

    @Test
    public void testGetConstructor_default() throws NoSuchMethodException {
        Constructor<A> c = ReflectionUtils.getConstructor(A.class, 'c');
        assertArrayEquals(new Class[] { char.class }, c.getParameterTypes());
    }

    @Test
    public void testGetConstructor_private() {
        assertThrows(NoSuchMethodException.class, () -> ReflectionUtils.getConstructor(A.class, (byte) 5));
    }

    @Test
    public void testGetConstructor_twoMatching() {
        assertThrows(IllegalArgumentException.class, () -> ReflectionUtils.getConstructor(A.class, new StringBuilder(0)));
    }

    @Test
    public void testGetConstructor_notFound() {
        assertThrows(NoSuchMethodException.class, () -> ReflectionUtils.getConstructor(A.class, true));
    }

    @Test
    public void testGetConstructor_WrongParams() {
        assertThrows(NoSuchMethodException.class, () -> ReflectionUtils.getConstructor(A.class, "", ""));
    }

    @Test
    public void testGetConstructor_AllPrimitives() throws NoSuchMethodException {
        Constructor<A> c = ReflectionUtils.getConstructor(A.class, true, (byte) 1, 2, (short) 3, 'g',
            5L, 4.0f, 8.0);
        assertNotNull(c);
    }

    @Test
    public void testGetDeclareMethod_Found() throws Exception {
        Method expected = A.class.getDeclaredMethod("foo", int.class);
        Method actual = ReflectionUtils.getDeclaredMethod(A.class, "foo", Integer.TYPE);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDeclareMethod_NotFound() {
        RuntimeException t = assertThrows(RuntimeException.class, () -> ReflectionUtils.getDeclaredMethod(A.class, "foo"));
        assertEquals(NoSuchMethodException.class, t.getCause().getClass());
    }

    @Test
    public void testIsClassMockingPossible() {
        assertTrue(ReflectionUtils.isClassAvailable("org.easymock.EasyMock"));
        assertFalse(ReflectionUtils.isClassAvailable("org.easymock.NotThere"));
    }

    @Test
    public void testFindMethodWithParam_notFound() {
        assertNull(ReflectionUtils.findMethod(getClass(), "xxx", NOT_PRIVATE, int.class));
    }

    @Test
    public void testFindMethodWithParam_foundDirectlyOnClass() {
        Method method = ReflectionUtils.findMethod(A.class, "foo", NOT_PRIVATE, int.class);
        assertEquals("foo", method.getName());
        assertEquals(A.class, method.getDeclaringClass());
    }

    @Test
    public void testFindMethodWithParam_foundDirectlyOnClassButWithDifferentParams() {
        assertNull(ReflectionUtils.findMethod(getClass(), "foo", NOT_PRIVATE, double.class));
        assertNull(ReflectionUtils.findMethod(getClass(), "foo", NOT_PRIVATE, int.class, int.class));
    }

    @Test
    public void testFindMethodWithParam_privateMethodsIgnored() {
        assertNull(ReflectionUtils.findMethod(A.class, "privateMethod", NOT_PRIVATE, NO_PARAMS));
    }

    @Test
    public void testFindMethodWithParam_protectedMethodsFound() {
        Method method = ReflectionUtils.findMethod(A.class, "protectedMethod", NOT_PRIVATE, NO_PARAMS);
        assertEquals("protectedMethod", method.getName());
        assertEquals(A.class, method.getDeclaringClass());
    }

    @Test
    public void testFindMethodWithParam_packageMethodsFound() {
        Method method = ReflectionUtils.findMethod(A.class, "packageMethod", NOT_PRIVATE, NO_PARAMS);
        assertEquals("packageMethod", method.getName());
        assertEquals(A.class, method.getDeclaringClass());
    }

    @Test
    public void testFindMethodWithParam_parentMethodsFound() {
        Method method = ReflectionUtils.findMethod(A.class, "parentMethod", NOT_PRIVATE, NO_PARAMS);
        assertEquals("parentMethod", method.getName());
        assertEquals(B.class, method.getDeclaringClass());
    }
}
