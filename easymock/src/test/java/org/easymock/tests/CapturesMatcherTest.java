/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.Capture;
import org.easymock.internal.matchers.Captures;
import org.junit.Test;


public class CapturesMatcherTest {

    private Capture<String> capture = new Capture<String>();
    private Captures<String> matcher = new Captures<String>(capture);
    
    @Test
    public void test() {
        StringBuffer buffer = new StringBuffer();
        matcher.appendTo(buffer);
        assertEquals("capture(Nothing captured yet)", buffer.toString());
        
        assertTrue(matcher.matches(null));
        
        buffer.delete(0, buffer.length());        
        matcher.appendTo(buffer);
        assertEquals("capture(null)", buffer.toString());
        
        assertTrue(matcher.matches("s"));
        
        buffer.delete(0, buffer.length());        
        matcher.appendTo(buffer);
        assertEquals("capture(s)", buffer.toString());
    }
}
