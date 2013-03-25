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

import org.easymock.ConstructorArgs;
import org.junit.Test;

/**
 * @author Henri Tremblay
 */
public class ConstructorArgsTest {

    public final Class<?> TYPE = null;

    public static class A {

        private static final Class<?> TYPE = null;

        public A(final String s, final int i) {
        }
    }

    @Test
    public void testConstructorArgs() {
        final ConstructorArgs args = new ConstructorArgs(A.class.getConstructors()[0], "a", 4);
        checkArgs(args);
    }

    private void checkArgs(final ConstructorArgs args) {
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
        new ConstructorArgs(A.class.getConstructors()[0], "a", new ConstructorArgsTest());
    }
}
