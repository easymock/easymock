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

import org.easymock.tests.IMethods;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author OFFIS, Tammo Freese
 */
public class UsageTest {

    IMethods mock;

    @BeforeEach
    public void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    public void exactCallCountByLastCall() {
        expect(mock.oneArg(false)).andReturn("Test").andReturn("Test2");
        replay(mock);

        assertEquals("Test", mock.oneArg(false));
        assertEquals("Test2", mock.oneArg(false));

        AssertionError e = assertThrows(AssertionError.class, () -> mock.oneArg(false));
        assertEquals(
            "\n  Unexpected method call EasyMock for interface org.easymock.tests.IMethods -> IMethods.oneArg(false):" +
                     "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.oneArg(false): expected: 2, actual: 3", e.getMessage());
    }

    @Test
    public void openCallCountByLastCall() {
        expect(mock.oneArg(false)).andReturn("Test").andReturn("Test2").atLeastOnce();

        replay(mock);

        assertEquals("Test", mock.oneArg(false));
        assertEquals("Test2", mock.oneArg(false));
        assertEquals("Test2", mock.oneArg(false));
    }

    @Test
    public void exactCallCountByLastThrowable() {
        expect(mock.oneArg(false)).andReturn("Test").andReturn("Test2").andThrow(
                new IndexOutOfBoundsException());

        replay(mock);

        assertEquals("Test", mock.oneArg(false));
        assertEquals("Test2", mock.oneArg(false));

        IndexOutOfBoundsException actual = assertThrows(IndexOutOfBoundsException.class, () -> mock.oneArg(false));
        assertEquals(null, actual.getMessage());

        assertThrows(AssertionError.class, () -> mock.oneArg(false));
    }

    @Test
    public void openCallCountByLastThrowable() {
        expect(mock.oneArg(false)).andReturn("Test").andReturn("Test2").andThrow(
                new IndexOutOfBoundsException()).atLeastOnce();

        replay(mock);

        assertEquals("Test", mock.oneArg(false));
        assertEquals("Test2", mock.oneArg(false));

        assertThrows(IndexOutOfBoundsException.class, () -> mock.oneArg(false));
        assertThrows(IndexOutOfBoundsException.class, () -> mock.oneArg(false));
    }

    @Test
    public void moreThanOneArgument() {
        expect(mock.threeArgumentMethod(1, "2", "3")).andReturn("Test").times(2);

        replay(mock);

        assertEquals("Test", mock.threeArgumentMethod(1, "2", "3"));

        AssertionError expected = assertThrows(AssertionError.class, () -> verify(mock));
        assertEquals("\n  Expectation failure on verify:"
                + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.threeArgumentMethod(1 (int), \"2\", \"3\"): expected: 2, actual: 1", expected
                .getMessage());
    }

    @Test
    public void wrongArguments() {
        mock.simpleMethodWithArgument("3");
        replay(mock);

        AssertionError expected = assertThrows(AssertionError.class, () -> mock.simpleMethodWithArgument("5"));
        assertEquals("\n  Unexpected method call EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethodWithArgument(\"5\"):"
                + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethodWithArgument(\"3\"): expected: 1, actual: 0", expected
                .getMessage());
    }

    @Test
    public void summarizeSameObjectArguments() {
        mock.simpleMethodWithArgument("3");
        mock.simpleMethodWithArgument("3");
        replay(mock);

        AssertionError expected = assertThrows(AssertionError.class, () -> mock.simpleMethodWithArgument("5"));
        assertEquals("\n  Unexpected method call EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethodWithArgument(\"5\"):"
                + "\n    EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethodWithArgument(\"3\"): expected: 2, actual: 0", expected
                .getMessage());
    }

    @Test
    public void argumentsOrdered() {
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
    public void mixingOrderedAndUnordered() {
        mock.simpleMethodWithArgument("2");
        mock.simpleMethodWithArgument("1");
        checkOrder(mock, true);
        mock.simpleMethodWithArgument("3");
        mock.simpleMethodWithArgument("4");
        checkOrder(mock, false);
        mock.simpleMethodWithArgument("6");
        mock.simpleMethodWithArgument("7");
        mock.simpleMethodWithArgument("5");

        replay(mock);

        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");

        assertThrows(AssertionError.class, () -> mock.simpleMethodWithArgument("4"));

        mock.simpleMethodWithArgument("3");
        mock.simpleMethodWithArgument("4");
        mock.simpleMethodWithArgument("5");
        mock.simpleMethodWithArgument("6");
        mock.simpleMethodWithArgument("7");

        verifyRecording(mock);
    }

    @Test
    public void resumeIfFailure() {
        IMethods mock = createMock(IMethods.class);
        expect(mock.oneArg(true)).andReturn("foo").anyTimes();
        replay(mock);

        mock.oneArg(true);

        assertThrows(AssertionError.class, () -> mock.simpleMethod());

        mock.oneArg(true);

        verifyRecording(mock);
    }

    @Test
    public void defaultResetToNice() {
        IMethods mock = createMock(IMethods.class);

        expect(mock.oneArg(true)).andReturn("foo");
        replay(mock);

        resetToNice(mock);

        replay(mock);

        assertNull(mock.oneArg(true));

        verify(mock);
    }

    @Test
    public void strictResetToDefault() {
        IMethods mock = createStrictMock(IMethods.class);

        expect(mock.oneArg(true)).andReturn("foo");
        expect(mock.oneArg(false)).andReturn("foo");

        replay(mock);

        resetToDefault(mock);

        expect(mock.oneArg(false)).andReturn("foo");
        expect(mock.oneArg(true)).andReturn("foo");

        replay(mock);

        assertEquals("foo", mock.oneArg(false));
        assertEquals("foo", mock.oneArg(true));

        verify(mock);
    }

    @Test
    public void niceToStrict() {
        IMethods mock = createNiceMock(IMethods.class);

        expect(mock.oneArg(false)).andReturn("foo");

        replay(mock);

        assertNull(mock.oneArg(true));

        resetToStrict(mock);

        expect(mock.oneArg(false)).andReturn("foo");
        expect(mock.oneArg(true)).andReturn("foo");

        replay(mock);

        assertThrows(AssertionError.class, () -> mock.oneArg(true));

        assertEquals("foo", mock.oneArg(false));
        assertEquals("foo", mock.oneArg(true));

        verifyRecording(mock);
    }
}
