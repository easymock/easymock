/*
 * Copyright 2001-2025 the original author or authors.
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

import org.easymock.ConstructorArgs;
import org.junit.jupiter.api.Test;

import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Henri Tremblay
 */
public class ConstructorArgsTest {

    public final Class<?> TYPE = null;

    public static class A {

        private static final Class<?> TYPE = null;

        public A(String s, int i) {
        }

        @Override
        public String toString() {
            return "ConstructorArgsTest$A";
        }
    }

    @Override
    public String toString() {
        return "ConstructorArgsTest";
    }

    @Test
    void testConstructorArgs() {
        ConstructorArgs args = new ConstructorArgs(A.class.getConstructors()[0], "a", 4);
        checkArgs(args);
    }

    private void checkArgs(ConstructorArgs args) {
        assertEquals(2, args.getInitArgs().length);
        assertEquals("a", args.getInitArgs()[0]);
        assertEquals(4, args.getInitArgs()[1]);

        assertEquals(A.class.getConstructors()[0], args.getConstructor());
    }

    @Test
    void testConstructorArgs_WrongArgument() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new ConstructorArgs(A.class.getConstructors()[0], "a", "b"));
        assertEquals("b isn't of type int", e.getMessage());
    }

    @Test
    void testConstructorArgs_NullPrimitive() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new ConstructorArgs(A.class.getConstructors()[0], "a", null));
        assertEquals("Null argument for primitive param 1", e.getMessage());
    }

    @Test
    void testConstructorArgs_PrimitiveForObject() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new ConstructorArgs(A.class.getConstructors()[0], 1, 2));
        assertEquals("1 isn't of type class java.lang.String", e.getMessage());
    }

    @Test
    void testConstructorArgs_NullObject() {
        new ConstructorArgs(A.class.getConstructors()[0], null, 2);
    }

    @Test
    void testConstructorArgs_WrongPrimitive() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new ConstructorArgs(A.class.getConstructors()[0], "a", 2.0f));
        assertEquals("2.0 isn't of type int", e.getMessage());
    }

    @Test
    void testConstructorArgs_WrongNumberOfArgs() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new ConstructorArgs(A.class.getConstructors()[0], "a"));
        assertEquals("Number of provided arguments doesn't match constructor ones", e.getMessage());
    }

    @Test
    void testConstructorArgs_TypeExistsButPrivate() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new ConstructorArgs(A.class.getConstructors()[0], "a", new A(null, 1)));
        assertEquals("ConstructorArgsTest$A isn't of type int", e.getMessage());
    }

    @Test
    void testConstructorArgs_TypeExistsButNotStatic() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new ConstructorArgs(A.class.getConstructors()[0], "a", new ConstructorArgsTest()));
        assertEquals("ConstructorArgsTest isn't of type int", e.getMessage());
    }
}
