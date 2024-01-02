/*
 * Copyright 2001-2024 the original author or authors.
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

import org.easymock.internal.ArgumentToString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * @author Henri Tremblay
 */
public class ArgumentToStringTest {

    private StringBuffer buffer;

    @BeforeEach
    public void setUp() {
        buffer = new StringBuffer();
    }

    @Test
    public void testAppendArgument_null() {
        assertString("null", null);
    }

    @Test
    public void testAppendArgument_String() {
        assertString("\"hello\"", "hello");
    }

    @Test
    public void testAppendArgument_Character() {
        assertString("'c'", 'c');
    }

    @Test
    public void testAppendArgument_Array() {
        assertString("[\"a\", \"b\"]", new String[] { "a", "b" });
    }

    @Test
    public void testAppendArgument_Full() {
        assertString("[3 (int), 4 (int), [\"a\", \"b\"], null]", new Object[] { 3, 4, new String[] { "a", "b" }, null });
    }

    private void assertString(String expected, Object actual) {
        ArgumentToString.appendArgument(actual, buffer);
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testArgumentToString() {
        String actual = ArgumentToString.argumentToString(Boolean.TRUE);
        assertEquals(Boolean.TRUE.toString(), actual);
    }

    @Test
    public void testArgumentsToString() {
        String actual = ArgumentToString.argumentsToString(Boolean.TRUE, Boolean.FALSE);
        assertEquals("true, false", actual);
    }

    @Test
    public void testArgumentsToString_null() {
        String actual = ArgumentToString.argumentsToString((Object[]) null);
        assertEquals("", actual);
    }

    @Test
    public void testArrayToLong_100elements() {
        int[] array = IntStream.range(0, 100).toArray();
        String actual = ArgumentToString.argumentsToString((Object) array);
        String expected = IntStream.range(0, 100)
            .mapToObj(i -> i + " (int)")
            .collect(Collectors.joining(", ", "[", "]"));
        assertEquals(expected, actual);
    }

    @Test
    public void testArrayToLong_101elements() {
        int[] array = IntStream.range(0, 101).toArray();
        String actual = ArgumentToString.argumentsToString((Object) array);
        String expected = "[" + IntStream.range(0, 100)
            .mapToObj(i -> i + " (int)")
            .collect(Collectors.joining(", ")) + "... (length=101)]";
        assertEquals(expected, actual);
    }
}
