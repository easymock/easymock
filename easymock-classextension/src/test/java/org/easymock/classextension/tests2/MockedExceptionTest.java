/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.classextension.tests2;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class MockedExceptionTest {

    @Test
    public void testMockedException() {
        RuntimeException expected = createNiceMock(RuntimeException.class);
        CharSequence c = createMock(CharSequence.class);
        expect(c.length()).andStubThrow(expected);
        replay(c, expected);

        try {
            c.length(); // fillInStackTrace will be called internally here
        } catch (RuntimeException actual) {
            assertSame(expected, actual);
        }

        verify(c, expected);
    }

    @Test
    public void testExplicitFillInStackTrace() {

        RuntimeException expected = createNiceMock(RuntimeException.class);
        RuntimeException myException = new RuntimeException();
        expect(expected.fillInStackTrace()).andReturn(myException);

        CharSequence c = createMock(CharSequence.class);
        expect(c.length()).andStubThrow(expected);

        replay(c, expected);

        try {
            c.length(); // fillInStackTrace will be called internally here
        } catch (RuntimeException actual) {
            assertSame(myException, actual.fillInStackTrace());
            assertSame(expected, actual);
        }

        verify(c, expected);
    }

    @Test
    public void testNotMockedFillInStackTrace() {

        RuntimeException expected = createMockBuilder(RuntimeException.class)
                .createNiceMock();

        CharSequence c = createMock(CharSequence.class);
        expect(c.length()).andStubThrow(expected);

        replay(c, expected);

        try {
            c.length(); // fillInStackTrace will be called internally here
        } catch (RuntimeException actual) {
            assertSame(expected, actual);
            assertEquals("fillInStackTrace should have been called normally",
                    actual.getClass().getName(), actual
                            .getStackTrace()[0].getClassName());
        }

        verify(c, expected);
    }

    @Test
    public void testRealException() {

        RuntimeException expected = new RuntimeException();

        CharSequence c = createMock(CharSequence.class);
        expect(c.length()).andStubThrow(expected);

        replay(c);

        try {
            c.length(); // fillInStackTrace will be called internally here
        } catch (RuntimeException actual) {
            assertSame(expected, actual);
            assertEquals("fillInStackTrace should have been called normally",
                    "org.easymock.internal.MockInvocationHandler", actual
                            .getStackTrace()[0].getClassName());
        }

        verify(c);
    }
}
