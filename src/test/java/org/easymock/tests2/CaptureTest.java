/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests2;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.Capture;
import org.easymock.tests.IMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CaptureTest {

    public static class A {
        public String foo(IMethods methods) {
            return methods.oneArg(2);
        }
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    // capture in thread
    // after replay issue?

    @Test
    public void testCaptureRightOne() {
        Capture<String> captured = new Capture<String>();
        IMethods mock = createMock(IMethods.class);

        expect(mock.oneArg(and(eq("test"), capture(captured)))).andReturn(
                "answer1");
        expect(mock.oneArg("a")).andReturn("answer2");

        replay(mock);

        assertEquals("answer2", mock.oneArg("a"));
        assertFalse(captured.hasCaptured());

        assertEquals("answer1", mock.oneArg("test"));
        assertEquals("test", captured.getValue());

        verify(mock);
    }

    @Test
    public void testPrimitiveVsObject() {
        Capture<Integer> capture = new Capture<Integer>();
        IMethods mock = createMock(IMethods.class);

        expect(mock.oneArg(capture(capture))).andReturn("answer");
        expect(mock.oneArg((Integer) capture(capture))).andReturn("answer");

        replay(mock);

        assertEquals("answer", mock.oneArg(2));
        assertEquals(2, capture.getValue().intValue());

        assertEquals("answer", mock.oneArg(Integer.valueOf(3)));
        assertEquals(3, capture.getValue().intValue());

        verify(mock);
    }

    @Test
    public void testAnd() {
        Capture<String> captured = new Capture<String>();
        IMethods mock = createMock(IMethods.class);

        expect(mock.oneArg(and(capture(captured), eq("test")))).andReturn(
                "answer");

        replay(mock);

        assertEquals("answer", mock.oneArg("test"));
        assertEquals("test", captured.getValue());

        verify(mock);
    }
    
    @Test
    public void testPrimitive() {
        Capture<Integer> captureI = new Capture<Integer>();
        Capture<Long> captureL = new Capture<Long>();
        Capture<Float> captureF = new Capture<Float>();
        Capture<Double> captureD = new Capture<Double>();
        Capture<Byte> captureB = new Capture<Byte>();
        Capture<Character> captureC = new Capture<Character>();

        IMethods mock = createMock(IMethods.class);

        expect(mock.oneArg(capture(captureI))).andReturn("answerI");
        expect(mock.oneArg(capture(captureL))).andReturn("answerL");
        expect(mock.oneArg(capture(captureF))).andReturn("answerF");
        expect(mock.oneArg(capture(captureD))).andReturn("answerD");
        expect(mock.oneArg(capture(captureB))).andReturn("answerB");
        expect(mock.oneArg(capture(captureC))).andReturn("answerC");

        replay(mock);

        assertEquals("answerI", mock.oneArg(1));
        assertEquals("answerL", mock.oneArg(2l));
        assertEquals("answerF", mock.oneArg(3.0f));
        assertEquals("answerD", mock.oneArg(4.0));
        assertEquals("answerB", mock.oneArg((byte) 5));
        assertEquals("answerC", mock.oneArg((char) 6));

        assertEquals(1, captureI.getValue().intValue());
        assertEquals(2l, captureL.getValue().longValue());
        assertEquals(3.0f, captureF.getValue().floatValue(), 0.0);
        assertEquals(4.0, captureD.getValue().doubleValue(), 0.0);
        assertEquals((byte) 5, captureB.getValue().byteValue());
        assertEquals((char) 6, captureC.getValue().charValue());
        
        verify(mock);
    }
    
    @Test
    public void testCapture() {
        Capture<String> capture = new Capture<String>();
        assertFalse(capture.hasCaptured());
        try {
            capture.getValue();
            fail("Should not be allowed");
        }
        catch(AssertionError e) {
            assertEquals("Nothing captured yet", e.getMessage());
        }
        assertEquals("Nothing captured yet", capture.toString());
        capture.setValue("s");
        assertTrue(capture.hasCaptured());
        assertEquals("s", capture.getValue());
        assertEquals("s", capture.toString());
        capture.reset();
        assertFalse(capture.hasCaptured());
        try {
            capture.getValue();
            fail("Should not be allowed");
        }
        catch(AssertionError e) {
            assertEquals("Nothing captured yet", e.getMessage());
        }
        
        capture.setValue(null);
        assertTrue(capture.hasCaptured());
        assertNull(capture.getValue());
        assertNull(capture.toString());
    }
}
