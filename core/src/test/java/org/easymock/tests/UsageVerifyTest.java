/*
 * Copyright 2001-2018 the original author or authors.
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
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author OFFIS, Tammo Freese
 */
public class UsageVerifyTest {

    private IMethods mock;

    @Before
    public void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    public void twoReturns() {
        expect(mock.throwsNothing(true)).andReturn("Test").andReturn("Test2");

        replay(mock);

        assertEquals("Test", mock.throwsNothing(true));

        boolean failed = true;

        try {
            verify(mock);
            failed = false;
        } catch (AssertionError expected) {
            assertEquals("\n  Expectation failure on verify:"
                    + "\n    IMethods.throwsNothing(true): expected: 2, actual: 1", expected.getMessage());
            assertEquals("stack trace must be filled in", Util.getStackTrace(expected).indexOf(
                ReplayState.class.getName()), -1);
        }

        if (!failed)
            fail("AssertionError expected");

        assertEquals("Test2", mock.throwsNothing(true));

        verify(mock);

        try {
            mock.throwsNothing(true);
            fail("AssertionError expected");
        } catch (AssertionError expected) {
            assertEquals("\n  Unexpected method call IMethods.throwsNothing(true):"
                    + "\n    IMethods.throwsNothing(true): expected: 2, actual: 3", expected.getMessage());
        }
    }

    @Test
    public void atLeastTwoReturns() {
        expect(mock.throwsNothing(true)).andReturn("Test").andReturn("Test2").atLeastOnce();

        replay(mock);

        assertEquals("Test", mock.throwsNothing(true));

        try {
            verify(mock);
            fail("AssertionError expected");
        } catch (AssertionError expected) {

            assertEquals("\n  Expectation failure on verify:"
                    + "\n    IMethods.throwsNothing(true): expected: at least 2, actual: 1", expected
                    .getMessage());
        }

        assertEquals("Test2", mock.throwsNothing(true));
        assertEquals("Test2", mock.throwsNothing(true));

        verify(mock);
    }

    @Test
    public void twoThrows() throws IOException {
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
                    + "\n    IMethods.throwsIOException(0 (int)): expected: 2, actual: 1"
                    + "\n    IMethods.throwsIOException(1 (int)): expected: 1, actual: 0", expected.getMessage());
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
                    + "\n    IMethods.throwsIOException(1 (int)): expected: 1, actual: 0", expected.getMessage());
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
            assertEquals("\n  Unexpected method call IMethods.throwsIOException(0 (int)):"
                    + "\n    IMethods.throwsIOException(0 (int)): expected: 2, actual: 3", expected.getMessage());
        }
    }

    @Test
    public void manyMocks() {
        IMethods otherMock = mock(IMethods.class);
        expect(otherMock.oneArg(1)).andReturn("test");
        replay(mock, otherMock);

        try {
            verify(mock, otherMock);
            fail("Should fail on otherMock");
        } catch (AssertionError e) {
            assertEquals(AssertionError.class, e.getClass());
            assertEquals("On mock #1 (zero indexed): \n  Expectation failure on verify:\n    IMethods.oneArg(1 (int)): expected: 1, actual: 0", e.getMessage());
        }
    }

    @Test
    public void callExtraMethodsCheckedInVerify() {
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
                "\n    IMethods.simpleMethod()" +
                "\n    IMethods.oneArg(1 (int))", e.getMessage());
        }
    }

    @Test
    public void uncalledAndUnexpected() {
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
                    "\n    IMethods.oneArg(1 (int)): expected: 1, actual: 0" +
                    "\n  Unexpected method calls:" +
                    "\n    IMethods.simpleMethod()", e.getMessage());
        }
    }

    @Test
    public void verifyRecordingTest() {
        IMethods mock = mock(IMethods.class);
        expect(mock.oneArg(1)).andReturn("test");
        replay(mock);

        try {
            verifyRecording(mock);
            fail("Should see unused expectations");
        } catch(AssertionError e) {
            assertEquals(
                "\n  Expectation failure on verify:" +
                    "\n    IMethods.oneArg(1 (int)): expected: 1, actual: 0", e.getMessage());
        }
    }

    @Test
    public void verifyUnexpectedCallsTest() {
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
                    "\n    IMethods.simpleMethod()", e.getMessage());
        }
    }
}
