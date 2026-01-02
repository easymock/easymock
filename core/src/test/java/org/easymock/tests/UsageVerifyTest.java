/*
 * Copyright 2001-2026 the original author or authors.
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

import org.easymock.internal.ReplayState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author OFFIS, Tammo Freese
 */
class UsageVerifyTest {

    private IMethods mock;

    @BeforeEach
    void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    void twoReturns() {
        expect(mock.throwsNothing(true)).andReturn("Test").andReturn("Test2");

        replay(mock);

        assertEquals("Test", mock.throwsNothing(true));

        boolean failed = true;

        try {
            verify(mock);
            failed = false;
        } catch (AssertionError expected) {
            assertEquals("\n  Expectation failure on verify:"
                    + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.throwsNothing(true): expected: 2, actual: 1", expected.getMessage());
            assertEquals(Util.getStackTrace(expected).indexOf(
                ReplayState.class.getName()), -1, "stack trace must be filled in");
        }

        if (!failed)
            fail("AssertionError expected");

        assertEquals("Test2", mock.throwsNothing(true));

        verify(mock);

        try {
            mock.throwsNothing(true);
            fail("AssertionError expected");
        } catch (AssertionError expected) {
            assertEquals("\n  Unexpected method call EasyMock for interface org.easymock.tests.IMethods -> IMethods.throwsNothing(true):"
                    + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.throwsNothing(true): expected: 2, actual: 3", expected.getMessage());
        }
    }

    @Test
    void atLeastTwoReturns() {
        expect(mock.throwsNothing(true)).andReturn("Test").andReturn("Test2").atLeastOnce();

        replay(mock);

        assertEquals("Test", mock.throwsNothing(true));

        try {
            verify(mock);
            fail("AssertionError expected");
        } catch (AssertionError expected) {

            assertEquals("\n  Expectation failure on verify:"
                    + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.throwsNothing(true): expected: at least 2, actual: 1", expected
                    .getMessage());
        }

        assertEquals("Test2", mock.throwsNothing(true));
        assertEquals("Test2", mock.throwsNothing(true));

        verify(mock);
    }

    @Test
    void twoThrows() throws IOException {
        expect(mock.throwsIOException(0)).andThrow(new IOException()).andThrow(new IOException());
        expect(mock.throwsIOException(1)).andThrow(new IOException());

        replay(mock);

        try {
            mock.throwsIOException(0);
            fail("IOException expected");
        } catch (IOException expected) {
        }

        try {
            verify(mock);
            fail("AssertionError expected");
        } catch (AssertionError expected) {
            assertEquals("\n  Expectation failure on verify:"
                    + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.throwsIOException(0 (int)): expected: 2, actual: 1"
                    + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.throwsIOException(1 (int)): expected: 1, actual: 0", expected.getMessage());
        }

        try {
            mock.throwsIOException(0);
            fail("IOException expected");
        } catch (IOException expected) {
        }

        try {
            verify(mock);
            fail("AssertionError expected");
        } catch (AssertionError expected) {
            assertEquals("\n  Expectation failure on verify:"
                    + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.throwsIOException(1 (int)): expected: 1, actual: 0", expected.getMessage());
        }

        try {
            mock.throwsIOException(1);
            fail("IOException expected");
        } catch (IOException expected) {
        }

        verify(mock);

        try {
            mock.throwsIOException(0);
            fail("AssertionError expected");
        } catch (AssertionError expected) {
            assertEquals("\n  Unexpected method call EasyMock for interface org.easymock.tests.IMethods -> IMethods.throwsIOException(0 (int)):"
                    + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.throwsIOException(0 (int)): expected: 2, actual: 3", expected.getMessage());
        }
    }

    @Test
    void manyMocks() {
        IMethods otherMock = mock(IMethods.class);
        expect(otherMock.oneArg(1)).andReturn("test");
        replay(mock, otherMock);

        try {
            verify(mock, otherMock);
            fail("Should fail on otherMock");
        } catch (AssertionError e) {
            assertEquals(AssertionError.class, e.getClass());
            assertEquals("On mock #1 (zero indexed): \n  Expectation failure on verify:\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.oneArg(1 (int)): expected: 1, actual: 0", e.getMessage());
        }
    }

    @Test
    void callExtraMethodsCheckedInVerify() {
        IMethods mock = mock(IMethods.class);
        replay(mock);

        try {
            mock.simpleMethod();
        } catch(AssertionError e) {
            // eat the exception and so prevent the test from failing
        }

        try {
            mock.oneArg(1);
        } catch(AssertionError e) {
            // eat the exception and so prevent the test from failing
        }

        // the verify should notice an assertion failed earlier
        try {
            verify(mock);
            fail("Should find unexpected calls");
        } catch(AssertionError e) {
            assertEquals("\n  Unexpected method calls:" +
                "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethod()" +
                "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.oneArg(1 (int))", e.getMessage());
        }
    }

    @Test
    void uncalledAndUnexpected() {
        IMethods mock = mock(IMethods.class);
        expect(mock.oneArg(1)).andReturn("test");
        replay(mock);

        try {
            mock.simpleMethod();
        } catch(AssertionError e) {
            // eat the exception and so prevent the test from failing
        }

        // the verify should notice an assertion failed earlier
        try {
            verify(mock);
            fail("Should find unexpected calls");
        } catch(AssertionError e) {
            assertEquals(
                "\n  Expectation failure on verify:" +
                    "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.oneArg(1 (int)): expected: 1, actual: 0" +
                    "\n  Unexpected method calls:" +
                    "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethod()", e.getMessage());
        }
    }

    @Test
    void verifyRecordingTest() {
        IMethods mock = mock(IMethods.class);
        expect(mock.oneArg(1)).andReturn("test");
        replay(mock);

        try {
            verifyRecording(mock);
            fail("Should see unused expectations");
        } catch(AssertionError e) {
            assertEquals(
                "\n  Expectation failure on verify:" +
                    "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.oneArg(1 (int)): expected: 1, actual: 0", e.getMessage());
        }
    }

    @Test
    void verifyUnexpectedCallsTest() {
        IMethods mock = mock(IMethods.class);
        replay(mock);

        try {
            mock.simpleMethod();
        } catch(AssertionError e) {
            // eat the exception and so prevent the test from failing
        }

        // the verify should notice an assertion failed earlier
        try {
            verifyUnexpectedCalls(mock);
            fail("Should find unexpected calls");
        } catch(AssertionError e) {
            assertEquals("\n  Unexpected method calls:" +
                    "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethod()", e.getMessage());
        }
    }
}
