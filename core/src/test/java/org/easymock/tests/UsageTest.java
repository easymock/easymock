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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author OFFIS, Tammo Freese
 */
class UsageTest {

    private IMethods mock;

    @BeforeEach
    void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    void exactCallCountByLastCall() {
        expect(mock.oneArg(false)).andReturn("Test").andReturn("Test2");

        replay(mock);

        assertEquals("Test", mock.oneArg(false));
        assertEquals("Test2", mock.oneArg(false));

        assertThrows(AssertionError.class, () -> mock.oneArg(false));
    }

    @Test
    void openCallCountByLastCall() {
        expect(mock.oneArg(false)).andReturn("Test").andReturn("Test2").atLeastOnce();

        replay(mock);

        assertEquals("Test", mock.oneArg(false));
        assertEquals("Test2", mock.oneArg(false));
        assertEquals("Test2", mock.oneArg(false));
    }

    @Test
    void exactCallCountByLastThrowable() {
        expect(mock.oneArg(false)).andReturn("Test").andReturn("Test2").andThrow(
                new IndexOutOfBoundsException()).once();

        replay(mock);

        assertEquals("Test", mock.oneArg(false));
        assertEquals("Test2", mock.oneArg(false));

        try {
            mock.oneArg(false);
        } catch (IndexOutOfBoundsException expected) {
        }

        assertThrows(AssertionError.class, () -> mock.oneArg(false));
    }

    @Test
    void openCallCountByLastThrowable() {
        expect(mock.oneArg(false)).andReturn("Test").andReturn("Test2").andThrow(
                new IndexOutOfBoundsException()).atLeastOnce();

        replay(mock);

        assertEquals("Test", mock.oneArg(false));
        assertEquals("Test2", mock.oneArg(false));

        try {
            mock.oneArg(false);
        } catch (IndexOutOfBoundsException expected) {
        }
        try {
            mock.oneArg(false);
        } catch (IndexOutOfBoundsException expected) {
        }
    }

    @Test
    void moreThanOneArgument() {
        expect(mock.threeArgumentMethod(1, "2", "3")).andReturn("Test").times(2);

        replay(mock);

        assertEquals("Test", mock.threeArgumentMethod(1, "2", "3"));

        AssertionError expected = assertThrows(AssertionError.class, () -> verify(mock));
        assertEquals("\n  Expectation failure on verify:"
                + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.threeArgumentMethod(1 (int), \"2\", \"3\"): expected: 2, actual: 1", expected
                .getMessage());
    }

    @Test
    void unexpectedCallWithArray() {
        reset(mock);
        replay(mock);
        String[] strings = new String[] { "Test" };
        AssertionError expected = assertThrows(AssertionError.class, () -> mock.arrayMethod(strings));
        assertEquals("\n  Unexpected method call EasyMock for interface org.easymock.tests.IMethods -> IMethods.arrayMethod(" + "[\"Test\"]" + ")", expected
                .getMessage());
    }

    @Test
    void wrongArguments() {
        mock.simpleMethodWithArgument("3");
        replay(mock);

        AssertionError expected = assertThrows(AssertionError.class, () -> mock.simpleMethodWithArgument("5"));
        assertEquals("\n  Unexpected method call EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethodWithArgument(\"5\"):"
                + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethodWithArgument(\"3\"): expected: 1, actual: 0", expected
                .getMessage());
    }

    @Test
    void summarizeSameObjectArguments() {
        mock.simpleMethodWithArgument("3");
        mock.simpleMethodWithArgument("3");
        replay(mock);

        AssertionError expected = assertThrows(AssertionError.class, () -> mock.simpleMethodWithArgument("5"));
        assertEquals("\n  Unexpected method call EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethodWithArgument(\"5\"):"
                + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethodWithArgument(\"3\"): expected: 2, actual: 0", expected
                .getMessage());
    }

    @Test
    void argumentsOrdered() {
        mock.simpleMethodWithArgument("4");
        mock.simpleMethodWithArgument("3");
        mock.simpleMethodWithArgument("2");
        mock.simpleMethodWithArgument("0");
        mock.simpleMethodWithArgument("1");
        replay(mock);

        AssertionError expected = assertThrows(AssertionError.class, () -> mock.simpleMethodWithArgument("5"));
        assertEquals("\n  Unexpected method call EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethodWithArgument(\"5\"):"
                + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethodWithArgument(\"4\"): expected: 1, actual: 0"
                + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethodWithArgument(\"3\"): expected: 1, actual: 0"
                + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethodWithArgument(\"2\"): expected: 1, actual: 0"
                + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethodWithArgument(\"0\"): expected: 1, actual: 0"
                + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethodWithArgument(\"1\"): expected: 1, actual: 0", expected
                .getMessage());
    }

    @Test
    void chainVoidMethodCalls() {
        mock.simpleMethodWithArgument("4");
        expectLastCall().andThrow(new RuntimeException("Test")).andVoid();
        replay(mock);
        try {
            mock.simpleMethodWithArgument("4");
        }
        catch(RuntimeException e) {
            assertEquals("Test", e.getMessage());
        }
        mock.simpleMethodWithArgument("4");
        verify(mock);
    }

    @Test
    void chainVoidMethodCallsVoidFirst() {
        mock.simpleMethodWithArgument("4");
        expectLastCall().andVoid().andThrow(new RuntimeException("Test"));
        replay(mock);
        mock.simpleMethodWithArgument("4");
        try {
            mock.simpleMethodWithArgument("4");
        }
        catch(RuntimeException e) {
            assertEquals("Test", e.getMessage());
        }
        verify(mock);
    }

    @Test
    void chainVoidWithItself() {
        mock.simpleMethodWithArgument("4");
        expectLastCall().andVoid().times(2).andVoid();
        replay(mock);
        mock.simpleMethodWithArgument("4");
        mock.simpleMethodWithArgument("4");
        mock.simpleMethodWithArgument("4");
        verify(mock);
    }

    @Test
    void boxingArgument() {
        Long value = 1L;
        expect(mock.oneLongArg(value)).andReturn("test");
        replay(mock);
        assertEquals("test", mock.oneLongArg(value));
        verify(mock);
    }
}
