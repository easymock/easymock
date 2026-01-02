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
package org.easymock.tests2;

import org.easymock.internal.ReplayState;
import org.easymock.tests.IMethods;
import org.easymock.tests.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author OFFIS, Tammo Freese
 */
class UsageStrictMockTest {
    private IMethods mock;

    @BeforeEach
    void setup() {
        mock = createStrictMock(IMethods.class);
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");
        replay(mock);
    }

    @Test
    void orderedCallsSuccess() {
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");
        verify(mock);
    }

    @Test
    void unorderedCallsFailure() {
        assertThrows(AssertionError.class, () -> mock.simpleMethodWithArgument("2"), "unordered calls accepted");
    }

    @Test
    void tooManyCallsFailure() {
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");

        assertThrows(AssertionError.class, () -> mock.simpleMethodWithArgument("2"), "too many calls accepted");
    }

    @Test
    void tooFewCallsFailure() {
        mock.simpleMethodWithArgument("1");
        AssertionError expected = assertThrows(AssertionError.class, () -> verify(mock), "too few calls accepted");
        assertEquals(Util.getStackTrace(expected).indexOf(ReplayState.class.getName()), -1, "stack trace must be filled in");
    }

    @Test
    void differentMethods() {

        reset(mock);

        mock.booleanReturningMethod(0);
        expectLastCall().andReturn(true);
        mock.simpleMethod();
        mock.booleanReturningMethod(1);
        expectLastCall().andReturn(false).times(2, 3);
        mock.simpleMethod();
        expectLastCall().atLeastOnce();

        replay(mock);
        assertTrue(mock.booleanReturningMethod(0));
        mock.simpleMethod();

        AssertionError expected = assertThrows(AssertionError.class, () -> verify(mock), "too few calls accepted");
        assertEquals("\n  Expectation failure on verify:"
            + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethod(): expected: 1, actual: 1"
            + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.booleanReturningMethod(1 (int)): expected: between 2 and 3, actual: 0"
            + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethod(): expected: at least 1, actual: 0", expected.getMessage());

        assertFalse(mock.booleanReturningMethod(1));

        expected = assertThrows(AssertionError.class, () -> mock.simpleMethod(), "wrong call accepted");
        assertEquals("\n  Unexpected method call EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethod():"
            + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.booleanReturningMethod(1 (int)): expected: between 2 and 3, actual: 1", expected.getMessage());
    }

    @Test
    void range() {

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

        AssertionError expected = assertThrows(AssertionError.class, () -> mock.booleanReturningMethod(1), "too many calls accepted");
        assertEquals("\n  Unexpected method call EasyMock for interface org.easymock.tests.IMethods -> IMethods.booleanReturningMethod(1 (int)):"
            + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.booleanReturningMethod(1 (int)): expected: between 2 and 3, actual: 4"
            + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethod(): expected: at least 1, actual: 0", expected.getMessage());
    }

    @Test
    void stubBehavior() {
        reset(mock);

        mock.booleanReturningMethod(1);
        expectLastCall().andReturn(true).andReturn(false).andReturn(true);
        mock.booleanReturningMethod(anyInt());
        expectLastCall().andStubReturn(true);

        replay(mock);

        assertTrue(mock.booleanReturningMethod(2));
        assertTrue(mock.booleanReturningMethod(3));
        assertTrue(mock.booleanReturningMethod(1));
        assertFalse(mock.booleanReturningMethod(1));
        assertTrue(mock.booleanReturningMethod(3));

        AssertionError expected = assertThrows(AssertionError.class, () -> verify(mock), "too few calls accepted");
        assertEquals("\n  Expectation failure on verify:"
            + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.booleanReturningMethod(1 (int)): expected: 3, actual: 2", expected
            .getMessage());
    }
}
