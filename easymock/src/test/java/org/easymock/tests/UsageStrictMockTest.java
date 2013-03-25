/**
 * Copyright 2001-2013 the original author or authors.
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

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.internal.ReplayState;
import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class UsageStrictMockTest {

    private IMethods mock;

    @Before
    public void setup() {
        mock = createStrictMock(IMethods.class);

        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");

        replay(mock);
    }

    @Test
    public void testVerify() {
        reset(mock);
        replay(mock);
        verify(mock);
    }

    @Test
    public void orderedCallsSucces() {
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");

        verify(mock);
    }

    @Test
    public void unorderedCallsFailure() {
        boolean failed = false;
        try {
            mock.simpleMethodWithArgument("2");
        } catch (final AssertionError expected) {
            failed = true;
        }
        if (!failed) {
            fail("unordered calls accepted");
        }
    }

    @Test
    public void tooManyCallsFailure() {
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");

        boolean failed = false;
        try {
            mock.simpleMethodWithArgument("2");
        } catch (final AssertionError expected) {
            failed = true;
        }
        if (!failed) {
            fail("too many calls accepted");
        }
    }

    @Test
    public void tooFewCallsFailure() {
        mock.simpleMethodWithArgument("1");
        boolean failed = false;
        try {
            verify(mock);
        } catch (final AssertionError expected) {
            failed = true;
            assertTrue("stack trace must be filled in", Util.getStackTrace(expected).indexOf(
                    ReplayState.class.getName()) == -1);
        }
        if (!failed) {
            fail("too few calls accepted");
        }
    }

    @Test
    public void differentMethods() {

        reset(mock);

        expect(mock.booleanReturningMethod(0)).andReturn(true);
        mock.simpleMethod();
        expect(mock.booleanReturningMethod(1)).andReturn(false).times(2, 3);
        mock.simpleMethod();
        expectLastCall().atLeastOnce();

        replay(mock);
        assertEquals(true, mock.booleanReturningMethod(0));
        mock.simpleMethod();

        boolean failed = false;
        try {
            verify(mock);
        } catch (final AssertionError expected) {
            failed = true;
            assertEquals("\n  Expectation failure on verify:"
                    + "\n    IMethods.simpleMethod(): expected: 1, actual: 1"
                    + "\n    IMethods.booleanReturningMethod(1): expected: between 2 and 3, actual: 0"
                    + "\n    IMethods.simpleMethod(): expected: at least 1, actual: 0", expected.getMessage());
        }
        if (!failed) {
            fail("too few calls accepted");
        }

        assertEquals(false, mock.booleanReturningMethod(1));

        failed = false;
        try {
            mock.simpleMethod();
        } catch (final AssertionError expected) {
            failed = true;
            assertEquals("\n  Unexpected method call IMethods.simpleMethod():"
                    + "\n    IMethods.booleanReturningMethod(1): expected: between 2 and 3, actual: 1",
                    expected.getMessage());
        }
        if (!failed) {
            fail("wrong call accepted");
        }
    }

    @Test
    public void range() {

        reset(mock);

        expect(mock.booleanReturningMethod(0)).andReturn(true);
        mock.simpleMethod();
        expect(mock.booleanReturningMethod(1)).andReturn(false).times(2, 3);
        mock.simpleMethod();
        expectLastCall().atLeastOnce();
        expect(mock.booleanReturningMethod(1)).andReturn(false);

        replay(mock);

        mock.booleanReturningMethod(0);
        mock.simpleMethod();

        mock.booleanReturningMethod(1);
        mock.booleanReturningMethod(1);
        mock.booleanReturningMethod(1);

        boolean failed = false;

        try {
            mock.booleanReturningMethod(1);
        } catch (final AssertionError expected) {
            failed = true;
            assertEquals("\n  Unexpected method call IMethods.booleanReturningMethod(1):"
                    + "\n    IMethods.booleanReturningMethod(1): expected: between 2 and 3, actual: 4"
                    + "\n    IMethods.simpleMethod(): expected: at least 1, actual: 0", expected.getMessage());
        }
        if (!failed) {
            fail("too many calls accepted");
        }
    }

    @Test
    public void defaultBehavior() {
        reset(mock);

        expect(mock.booleanReturningMethod(1)).andReturn(true).andReturn(false).andReturn(true);
        expect(mock.booleanReturningMethod(anyInt())).andStubReturn(true);

        replay(mock);

        assertEquals(true, mock.booleanReturningMethod(2));
        assertEquals(true, mock.booleanReturningMethod(3));
        assertEquals(true, mock.booleanReturningMethod(1));
        assertEquals(false, mock.booleanReturningMethod(1));
        assertEquals(true, mock.booleanReturningMethod(3));

        boolean failed = false;
        try {
            verify(mock);
        } catch (final AssertionError expected) {
            failed = true;
            assertEquals("\n  Expectation failure on verify:"
                    + "\n    IMethods.booleanReturningMethod(1): expected: 3, actual: 2", expected
                    .getMessage());
        }
        if (!failed) {
            fail("too few calls accepted");
        }
    }

    @Test
    public void unexpectedCallWithArray() {
        reset(mock);
        mock.arrayMethod(aryEq(new String[] { "Test", "Test 2" }));
        replay(mock);
        boolean failed = false;
        final String[] strings = new String[] { "Test" };
        try {
            mock.arrayMethod(strings);
        } catch (final AssertionError expected) {
            failed = true;
            assertEquals("\n  Unexpected method call IMethods.arrayMethod(" + "[\"Test\"]" + "):"
                    + "\n    IMethods.arrayMethod([\"Test\", \"Test 2\"]): expected: 1, actual: 0", expected
                    .getMessage());
        }
        if (!failed) {
            fail("exception expected");
        }

    }
}
