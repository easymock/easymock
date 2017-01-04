/**
 * Copyright 2001-2017 the original author or authors.
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

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.tests.IMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Henri Tremblay
 */
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

    private Capture<Integer> testCaptureType(CaptureType type) {
        IMethods mock = createMock(IMethods.class);
        Capture<Integer> captured = Capture.newInstance(type);

        expect(mock.oneArg(captureInt(captured))).andReturn("1");
        expect(mock.oneArg(anyInt())).andReturn("1");
        expect(mock.oneArg(captureInt(captured))).andReturn("2").times(2);
        mock.twoArgumentMethod(captureInt(captured), eq(5));
        mock.twoArgumentMethod(captureInt(captured), captureInt(captured));

        replay(mock);

        mock.oneArg(0);
        mock.oneArg(1);
        mock.oneArg(2);
        mock.oneArg(3);
        mock.twoArgumentMethod(4, 5);
        mock.twoArgumentMethod(6, 7);

        verify(mock);

        return captured;
    }

    @Test
    public void testCaptureFirst() {
        Capture<Integer> captured = testCaptureType(CaptureType.FIRST);
        assertEquals(0, (int) captured.getValue());
    }

    @Test
    public void testCaptureLast() {
        Capture<Integer> captured = testCaptureType(CaptureType.LAST);
        assertEquals(7, (int) captured.getValue());
    }

    @Test
    public void testCaptureAll() {
        Capture<Integer> captured = testCaptureType(CaptureType.ALL);
        assertEquals(Arrays.asList(0, 2, 3, 4, 6, 7), captured.getValues());
    }

    @Test
    public void testCaptureNone() {
        Capture<Integer> captured = testCaptureType(CaptureType.NONE);
        assertFalse(captured.hasCaptured());
    }

    // capture in thread
    // after replay issue?

    @Test
    public void testCaptureRightOne() {
        Capture<String> captured = Capture.newInstance();
        IMethods mock = createMock(IMethods.class);

        expect(mock.oneArg(and(eq("test"), capture(captured)))).andReturn("answer1");
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
        Capture<Integer> capture = Capture.newInstance();
        IMethods mock = createMock(IMethods.class);

        expect(mock.oneArg(captureInt(capture))).andReturn("answer");
        expect(mock.oneArg(capture(capture))).andReturn("answer");

        replay(mock);

        assertEquals("answer", mock.oneArg(2));
        assertEquals(2, capture.getValue().intValue());

        assertEquals("answer", mock.oneArg(Integer.valueOf(3)));
        assertEquals(3, capture.getValue().intValue());

        verify(mock);
    }

    @Test
    public void testAnd() {
        Capture<String> captured = Capture.newInstance();
        IMethods mock = createMock(IMethods.class);

        expect(mock.oneArg(and(capture(captured), eq("test")))).andReturn("answer");

        replay(mock);

        assertEquals("answer", mock.oneArg("test"));
        assertEquals("test", captured.getValue());

        verify(mock);
    }

    @Test
    public void testPrimitive() {
        Capture<Integer> captureI = Capture.newInstance();
        Capture<Long> captureL = Capture.newInstance();
        Capture<Float> captureF = Capture.newInstance();
        Capture<Double> captureD = Capture.newInstance();
        Capture<Byte> captureB = Capture.newInstance();
        Capture<Character> captureC = Capture.newInstance();
        Capture<Boolean> captureBool = Capture.newInstance();

        IMethods mock = createMock(IMethods.class);

        expect(mock.oneArg(captureInt(captureI))).andReturn("answerI");
        expect(mock.oneArg(captureLong(captureL))).andReturn("answerL");
        expect(mock.oneArg(captureFloat(captureF))).andReturn("answerF");
        expect(mock.oneArg(captureDouble(captureD))).andReturn("answerD");
        expect(mock.oneArg(captureByte(captureB))).andReturn("answerB");
        expect(mock.oneArg(captureChar(captureC))).andReturn("answerC");
        expect(mock.oneArg(captureBoolean(captureBool))).andReturn("answerZ");

        replay(mock);

        assertEquals("answerI", mock.oneArg(1));
        assertEquals("answerL", mock.oneArg(2l));
        assertEquals("answerF", mock.oneArg(3.0f));
        assertEquals("answerD", mock.oneArg(4.0));
        assertEquals("answerB", mock.oneArg((byte) 5));
        assertEquals("answerC", mock.oneArg((char) 6));
        assertEquals("answerZ", mock.oneArg(true));

        assertEquals(1, captureI.getValue().intValue());
        assertEquals(2l, captureL.getValue().longValue());
        assertEquals(3.0f, captureF.getValue().floatValue(), 0.0);
        assertEquals(4.0, captureD.getValue().doubleValue(), 0.0);
        assertEquals((byte) 5, captureB.getValue().byteValue());
        assertEquals((char) 6, captureC.getValue().charValue());
        assertEquals(true, captureBool.getValue().booleanValue());

        verify(mock);
    }

    @Test
    public void testCapture() {
        Capture<String> capture = Capture.newInstance();
        assertFalse(capture.hasCaptured());
        try {
            capture.getValue();
            fail("Should not be allowed");
        } catch (AssertionError e) {
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
            fail();
        } catch (AssertionError e) {
            assertEquals("Nothing captured yet", e.getMessage());
        }

        capture.setValue(null);
        assertTrue(capture.hasCaptured());
        assertNull(capture.getValue());
        assertEquals("null", capture.toString());
    }

    @Test
    public void testCaptureMultiple() {
        Capture<String> capture = Capture.newInstance(CaptureType.ALL);
        capture.setValue("a");
        capture.setValue("b");
        try {
            capture.getValue();
            fail();
        } catch (AssertionError e) {
            assertEquals("More than one value captured: " + capture.getValues(), e.getMessage());
        }
        assertEquals(Arrays.asList("a", "b"), capture.getValues());
    }

    @Test
    public void testCapture_2617107() {

        IMethods mock = createMock(IMethods.class);

        Capture<String> cap1 = Capture.newInstance();
        Capture<String> cap2 = Capture.newInstance();
        Capture<String> cap3 = Capture.newInstance();
        Capture<String> cap4 = Capture.newInstance();

        mock.simpleMethodWithArgument(and(isA(String.class), capture(cap1)));
        mock.simpleMethodWithArgument(and(isA(String.class), capture(cap2)));
        mock.simpleMethodWithArgument(and(isA(String.class), capture(cap3)));
        mock.simpleMethodWithArgument(and(isA(String.class), capture(cap4)));

        replay(mock);

        String[] s = { "one", "two", "three", "four" };

        for (String element : s) {
            mock.simpleMethodWithArgument(element);
        }

        assertEquals("one", cap1.getValue());
        assertEquals("two", cap2.getValue());
        assertEquals("three", cap3.getValue());
        assertEquals("four", cap4.getValue());

        verify(mock);
    }

    @Test
    public void testCaptureNonStrictControl_2133741() {
        testCaptureHelper(createMock(IMethods.class));
    }

    @Test
    public void testCaptureStrictControl_2133741() {
        testCaptureHelper(createStrictMock(IMethods.class));
    }

    protected void testCaptureHelper(IMethods mock) {
        Capture<String> capture1 = Capture.newInstance();
        Capture<String> capture2 = Capture.newInstance();

        mock.simpleMethodWithArgument(capture(capture1));
        mock.simpleMethodWithArgument(capture(capture2));

        replay(mock);
        mock.simpleMethodWithArgument("a");
        mock.simpleMethodWithArgument("b");
        verify(mock);

        assertTrue(capture1.hasCaptured());
        assertTrue(capture2.hasCaptured());
        assertFalse(capture1.getValue() == capture2.getValue());
    }

    @Test
    public void testCapture1_2446744() {
        Capture<String> capture1 = Capture.newInstance();
        Capture<String> capture2 = Capture.newInstance();
        Capture<String> capture3 = Capture.newInstance();
        IMethods mock = createMock(IMethods.class);
        expect(mock.oneArg(capture(capture1))).andReturn("1").once();
        expect(mock.oneArg(capture(capture2))).andReturn("2").once();
        expect(mock.oneArg(capture(capture3))).andReturn("3").once();

        replay(mock);

        for (int i = 0; i < 3; i++) {
            String string = "Run" + (i + 1);
            mock.oneArg(string);
        }

        assertEquals("Run3", capture3.getValue());
        assertEquals("Run2", capture2.getValue());
        assertEquals("Run1", capture1.getValue());
    }

    @Test
    public void testCapture2_2446744() {
        Capture<String> capture = Capture.newInstance(CaptureType.ALL);
        IMethods mock = createMock(IMethods.class);
        expect(mock.oneArg(capture(capture))).andReturn("1").once();
        expect(mock.oneArg(capture(capture))).andReturn("2").once();
        expect(mock.oneArg(capture(capture))).andReturn("3").once();

        replay(mock);

        for (int i = 0; i < 3; i++) {
            String string = "Run" + (i + 1);
            mock.oneArg(string);
        }

        assertEquals(Arrays.asList("Run1", "Run2", "Run3"), capture.getValues());
    }

    @Test
    public void testCaptureFromStub() {
        Capture<String> capture = Capture.newInstance(CaptureType.ALL);
        IMethods mock = createMock(IMethods.class);
        expect(mock.oneArg(capture(capture))).andStubReturn("1");

        replay(mock);

        mock.oneArg("test");

        assertEquals("test", capture.getValue());
    }

    @Test
    public void testNewInstanceForcingType() {
        // Just to test, we put it in a base class
        Capture<? extends Number> capture = newCapture();

        IMethods mock = createMock(IMethods.class);
        expect(mock.oneArg(capture(capture))).andStubReturn("1");

        replay(mock);

        mock.oneArg(Long.valueOf(0)); // note that we can capture something else than an Integer
        mock.oneArg(Long.valueOf(1));

        assertEquals(Long.valueOf(1), capture.getValue());
    }

    @Test
    public void testNewInstanceWithCaptureTypeForcingType() {
        // Just to test, we put it in a base class
        Capture<? extends Number> capture = newCapture(CaptureType.FIRST);

        IMethods mock = createMock(IMethods.class);
        expect(mock.oneArg(capture(capture))).andStubReturn("1");

        replay(mock);

        mock.oneArg(Long.valueOf(0)); // note that we can capture something else than an Integer
        mock.oneArg(Long.valueOf(1));

        assertEquals(Long.valueOf(0), capture.getValue());
    }
}
