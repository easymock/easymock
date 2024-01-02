/*
 * Copyright 2001-2024 the original author or authors.
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

import org.easymock.internal.ReplayState;
import org.easymock.tests.IMethods;
import org.easymock.tests.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.*;

/**
 * @author OFFIS, Tammo Freese
 */
public class UsageStrictMockTest {
    private IMethods mock;

    @BeforeEach
    public void setup() {
        mock = createStrictMock(IMethods.class);
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");
        replay(mock);
    }

    @Test
    public void orderedCallsSuccess() {
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");
        verify(mock);
    }

    @Test
    public void unorderedCallsFailure() {
        boolean failed = false;
        try {
            mock.simpleMethodWithArgument("2");
        } catch (AssertionError expected) {
            failed = true;
        }
        if (!failed) {
            Assertions.fail("unordered calls accepted");
        }
    }

    @Test
    public void tooManyCallsFailure() {
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");

        boolean failed = false;
        try {
            mock.simpleMethodWithArgument("2");
        } catch (AssertionError expected) {
            failed = true;
        }
        if (!failed) {
            Assertions.fail("too many calls accepted");
        }
    }

    @Test
    public void tooFewCallsFailure() {
        mock.simpleMethodWithArgument("1");
        boolean failed = false;
        try {
            verify(mock);
        } catch (AssertionError expected) {
            failed = true;
            Assertions.assertEquals(Util.getStackTrace(expected).indexOf(
                ReplayState.class.getName()), -1, "stack trace must be filled in");
        }
        if (!failed) {
            Assertions.fail("too few calls accepted");
        }
    }

    @Test
    public void differentMethods() {

        reset(mock);

        mock.booleanReturningMethod(0);
        expectLastCall().andReturn(true);
        mock.simpleMethod();
        mock.booleanReturningMethod(1);
        expectLastCall().andReturn(false).times(2, 3);
        mock.simpleMethod();
        expectLastCall().atLeastOnce();

        replay(mock);
        Assertions.assertTrue(mock.booleanReturningMethod(0));
        mock.simpleMethod();

        boolean failed = false;
        try {
            verify(mock);
        } catch (AssertionError expected) {
            failed = true;
            Assertions.assertEquals("\n  Expectation failure on verify:"
                    + "\n    IMethods.simpleMethod(): expected: 1, actual: 1"
                    + "\n    IMethods.booleanReturningMethod(1 (int)): expected: between 2 and 3, actual: 0"
                    + "\n    IMethods.simpleMethod(): expected: at least 1, actual: 0", expected.getMessage());
        }
        if (!failed) {
            Assertions.fail("too few calls accepted");
        }

        Assertions.assertFalse(mock.booleanReturningMethod(1));

        failed = false;
        try {
            mock.simpleMethod();
        } catch (AssertionError expected) {
            failed = true;
            Assertions.assertEquals("\n  Unexpected method call IMethods.simpleMethod():"
                    + "\n    IMethods.booleanReturningMethod(1 (int)): expected: between 2 and 3, actual: 1", expected.getMessage());
        }
        if (!failed) {
            Assertions.fail("wrong call accepted");
        }
    }

    @Test
    public void range() {

        reset(mock);

        mock.booleanReturningMethod(0);
        expectLastCall().andReturn(true);
        mock.simpleMethod();
        mock.booleanReturningMethod(1);
        expectLastCall().andReturn(false).times(2, 3);
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
        } catch (AssertionError expected) {
            failed = true;
            Assertions.assertEquals("\n  Unexpected method call IMethods.booleanReturningMethod(1 (int)):"
                    + "\n    IMethods.booleanReturningMethod(1 (int)): expected: between 2 and 3, actual: 4"
                    + "\n    IMethods.simpleMethod(): expected: at least 1, actual: 0", expected.getMessage());
        }
        if (!failed) {
            Assertions.fail("too many calls accepted");
        }
    }

    @Test
    public void stubBehavior() {
        reset(mock);

        mock.booleanReturningMethod(1);
        expectLastCall().andReturn(true).andReturn(false).andReturn(true);
        mock.booleanReturningMethod(anyInt());
        expectLastCall().andStubReturn(true);

        replay(mock);

        Assertions.assertTrue(mock.booleanReturningMethod(2));
        Assertions.assertTrue(mock.booleanReturningMethod(3));
        Assertions.assertTrue(mock.booleanReturningMethod(1));
        Assertions.assertFalse(mock.booleanReturningMethod(1));
        Assertions.assertTrue(mock.booleanReturningMethod(3));

        boolean failed = false;
        try {
            verify(mock);
        } catch (AssertionError expected) {
            failed = true;
            Assertions.assertEquals("\n  Expectation failure on verify:"
                    + "\n    IMethods.booleanReturningMethod(1 (int)): expected: 3, actual: 2", expected
                    .getMessage());
        }
        if (!failed) {
            Assertions.fail("too few calls accepted");
        }
    }
}
