/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.internal.ErrorMessage;
import org.junit.Test;

public class ErrorMessageTest {

    @Test
    public void testGetters() {
        ErrorMessage m = new ErrorMessage(true, "error", 3);
        assertTrue(m.isMatching());
        assertEquals("error", m.getMessage());
        assertEquals(3, m.getActualCount());
    }

    @Test
    public void testAppendTo_matchingOne() {
        StringBuilder sb = new StringBuilder(20);
        ErrorMessage m = new ErrorMessage(true, "error()", 2);
        m.appendTo(sb, 1);
        assertEquals("\n    error(), actual: 3", sb.toString());
    }
    
    @Test
    public void testAppendTo_matchingNone() {
        StringBuilder sb = new StringBuilder(20);
        ErrorMessage m = new ErrorMessage(false, "error()", 2);
        m.appendTo(sb, 0);
        assertEquals("\n    error(), actual: 2", sb.toString());
    }

    @Test
    public void testAppendTo_matchingMultiple() {
        StringBuilder sb = new StringBuilder(20);
        ErrorMessage m = new ErrorMessage(true, "error()", 2);
        m.appendTo(sb, 2);
        assertEquals("\n    error(), actual: 2 (+1)", sb.toString());
    }    
}
