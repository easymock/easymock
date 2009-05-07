/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.internal.Invocation;
import org.easymock.internal.LastControl;
import org.easymock.internal.matchers.Captures;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CapturesMatcherTest {

    private final Capture<String> capture = new Capture<String>(
            CaptureType.ALL);
    private final Captures<String> matcher = new Captures<String>(capture);
    
    private StringBuffer buffer;

    @Before
    public void setUp() throws Exception {
        LastControl.pushCurrentInvocation(new Invocation(this, getClass()
                .getMethod("test"), new Object[0]));
        buffer = new StringBuffer();
    }
    
    @After
    public void tearDown() {
        LastControl.popCurrentInvocation();
    }
    
    @Test
    public void test() throws Exception {
                
        matcher.appendTo(buffer);
        assertEquals("capture(Nothing captured yet)", buffer.toString());
                
        assertTrue(matcher.matches(null));
        
        matcher.validateCapture();        

        clearBuffer();
        matcher.appendTo(buffer);
        assertEquals("capture(null)", buffer.toString());        
        
        assertTrue(matcher.matches("s"));
        
        matcher.validateCapture();

        clearBuffer();        
        matcher.appendTo(buffer);
        assertEquals("capture([null, s])", buffer.toString());
    }
    
    private void clearBuffer() {
        buffer.delete(0, buffer.length());
    }
}
