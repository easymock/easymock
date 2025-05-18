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

import static org.easymock.EasyMock.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Henri Tremblay
 */
class MockedExceptionTest {

    @Test
    void testMockedException() {
        RuntimeException expected = createNiceMock(RuntimeException.class);
        CharSequence c = createMock(CharSequence.class);
        expect(c.length()).andStubThrow(expected);
        replay(c, expected);

        try {
            c.length(); // fillInStackTrace will be called internally here
        } catch (RuntimeException actual) {
            Assertions.assertSame(expected, actual);
        }

        verify(c, expected);
    }

    @Test
    void testExplicitFillInStackTrace() {

        RuntimeException expected = createNiceMock(RuntimeException.class);
        RuntimeException myException = new RuntimeException();
        expect(expected.fillInStackTrace()).andReturn(myException);

        CharSequence c = createMock(CharSequence.class);
        expect(c.length()).andStubThrow(expected);

        replay(c, expected);

        try {
            c.length(); // fillInStackTrace won't be called internally
        } catch (RuntimeException actual) {
            Assertions.assertSame(myException, actual.fillInStackTrace()); // so the fillInStackTrace recording is still valid
            Assertions.assertSame(expected, actual);
        }

        verify(c, expected);
    }

    private static int check = 2;

    public static class MyException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        @Override
        public synchronized Throwable fillInStackTrace() {
            check = 4;
            return super.fillInStackTrace();
        }

    }

    @Test
    void testNotMockedFillInStackTrace() {

        RuntimeException expected = createMockBuilder(MyException.class)
                .createNiceMock();

        CharSequence c = createMock(CharSequence.class);
        expect(c.length()).andStubThrow(expected);

        replay(c, expected);

        try {
            c.length(); // fillInStackTrace won't be called internally
        } catch (RuntimeException actual) {
            Assertions.assertSame(expected, actual);
            Assertions.assertSame(expected, actual.fillInStackTrace(), "fillInStackTrace should have been called normally since it isn't mocked");
            Assertions.assertEquals(4, check, "The original method was called");
        }

        verify(c, expected);
    }

    @Test
    void testRealException() {

        RuntimeException expected = new RuntimeException();

        CharSequence c = createMock(CharSequence.class);
        expect(c.length()).andStubThrow(expected);

        replay(c);

        try {
            c.length(); // fillInStackTrace will be called internally here
        } catch (RuntimeException actual) {
            Assertions.assertSame(expected, actual);
            Assertions.assertEquals("org.easymock.internal.MockInvocationHandler", actual.getStackTrace()[0].getClassName(), "fillInStackTrace should have been called normally");
        }

        verify(c);
    }
}
