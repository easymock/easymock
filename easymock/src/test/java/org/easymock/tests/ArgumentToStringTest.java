/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.internal.ArgumentToString;
import org.junit.Before;
import org.junit.Test;

public class ArgumentToStringTest {

    private StringBuffer buffer;

    @Before
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
        assertString("'c'", Character.valueOf('c'));
    }

    @Test
    public void testAppendArgument_Array() {
        assertString("[\"a\", \"b\"]", new String[] { "a", "b" });
    }

    @Test
    public void testAppendArgument_Full() {
        assertString("[3, 4, [\"a\", \"b\"], null]", new Object[] { 3, 4,
                new String[] { "a", "b" }, null });
    }
    
    private void assertString(String expected, Object actual) {
        ArgumentToString.appendArgument(actual, buffer);
        assertEquals(expected, buffer.toString());
    }
}
