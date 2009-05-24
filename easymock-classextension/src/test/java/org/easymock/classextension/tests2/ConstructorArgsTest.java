/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.classextension.tests2;

import static org.junit.Assert.*;

import org.easymock.classextension.ConstructorArgs;
import org.junit.Test;

public class ConstructorArgsTest {

    public final Class<?> TYPE = null;

    public static class A {

        private static final Class<?> TYPE = null;

        public A(String s, int i) {
        }
    }

    @Test
    public void testConstructorArgs() {
        ConstructorArgs args = new ConstructorArgs(
                A.class.getConstructors()[0], "a", 4);
        checkArgs(args);
    }

    private void checkArgs(ConstructorArgs args) {
        assertEquals(2, args.getInitArgs().length);
        assertEquals("a", args.getInitArgs()[0]);
        assertEquals(4, args.getInitArgs()[1]);

        assertEquals(A.class.getConstructors()[0], args.getConstructor());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorArgs_WrongArgument() {
        new ConstructorArgs(A.class.getConstructors()[0], "a", "b");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorArgs_NullPrimitive() {
        new ConstructorArgs(A.class.getConstructors()[0], "a", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorArgs_PrimitiveForObject() {
        new ConstructorArgs(A.class.getConstructors()[0], 1, 2);
    }

    @Test
    public void testConstructorArgs_NullObject() {
        new ConstructorArgs(A.class.getConstructors()[0], null, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorArgs_WrongPrimitive() {
        new ConstructorArgs(A.class.getConstructors()[0], "a", 2.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorArgs_WrongNumberOfArgs() {
        new ConstructorArgs(A.class.getConstructors()[0], "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorArgs_TypeExistsButPrivate() {
        new ConstructorArgs(A.class.getConstructors()[0], "a", new A(null, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorArgs_TypeExistsButNotStatic() {
        new ConstructorArgs(A.class.getConstructors()[0], "a",
                new ConstructorArgsTest());
    }
}
