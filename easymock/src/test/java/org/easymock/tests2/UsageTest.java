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
package org.easymock.tests2;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.tests.IMethods;
import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class UsageTest {

    IMethods mock;

    @Before
    public void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    public void exactCallCountByLastCall() {
        expect(mock.oneArg(false)).andReturn("Test").andReturn("Test2");
        replay(mock);

        assertEquals("Test", mock.oneArg(false));
        assertEquals("Test2", mock.oneArg(false));

        boolean failed = false;
        try {
            mock.oneArg(false);
        } catch (final AssertionError expected) {
            failed = true;
        }
        if (!failed)
            fail("expected AssertionError");
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

        try {
            mock.oneArg(false);
            fail();
        } catch (final IndexOutOfBoundsException expected) {
        }

        boolean failed = true;
        try {
            mock.oneArg(false);
            failed = false;
        } catch (final AssertionError expected) {
        }
        if (!failed)
            fail("expected AssertionError");
    }

    @Test
    public void openCallCountByLastThrowable() {
        expect(mock.oneArg(false)).andReturn("Test").andReturn("Test2").andThrow(
                new IndexOutOfBoundsException()).atLeastOnce();

        replay(mock);

        assertEquals("Test", mock.oneArg(false));
        assertEquals("Test2", mock.oneArg(false));

        try {
            mock.oneArg(false);
        } catch (final IndexOutOfBoundsException expected) {
        }
        try {
            mock.oneArg(false);
        } catch (final IndexOutOfBoundsException expected) {
        }
    }

    @Test
    public void moreThanOneArgument() {
        expect(mock.threeArgumentMethod(1, "2", "3")).andReturn("Test").times(2);

        replay(mock);

        assertEquals("Test", mock.threeArgumentMethod(1, "2", "3"));

        boolean failed = true;
        try {
            verify(mock);
            failed = false;
        } catch (final AssertionError expected) {
            assertEquals("\n  Expectation failure on verify:"
                    + "\n    IMethods.threeArgumentMethod(1, \"2\", \"3\"): expected: 2, actual: 1", expected
                    .getMessage());
        }
        if (!failed) {
            fail("exception expected");
        }
    }

    @Test
    public void wrongArguments() {
        mock.simpleMethodWithArgument("3");
        replay(mock);

        try {
            mock.simpleMethodWithArgument("5");
            fail();
        } catch (final AssertionError expected) {
            assertEquals("\n  Unexpected method call IMethods.simpleMethodWithArgument(\"5\"):"
                    + "\n    IMethods.simpleMethodWithArgument(\"3\"): expected: 1, actual: 0", expected
                    .getMessage());
        }

    }

    @Test
    public void summarizeSameObjectArguments() {
        mock.simpleMethodWithArgument("3");
        mock.simpleMethodWithArgument("3");
        replay(mock);

        try {
            mock.simpleMethodWithArgument("5");
            fail();
        } catch (final AssertionError expected) {
            assertEquals("\n  Unexpected method call IMethods.simpleMethodWithArgument(\"5\"):"
                    + "\n    IMethods.simpleMethodWithArgument(\"3\"): expected: 2, actual: 0", expected
                    .getMessage());
        }

    }

    @Test
    public void argumentsOrdered() {
        mock.simpleMethodWithArgument("4");
        mock.simpleMethodWithArgument("3");
        mock.simpleMethodWithArgument("2");
        mock.simpleMethodWithArgument("0");
        mock.simpleMethodWithArgument("1");
        replay(mock);

        try {
            mock.simpleMethodWithArgument("5");
            fail("exception expected");
        } catch (final AssertionError expected) {
            assertEquals("\n  Unexpected method call IMethods.simpleMethodWithArgument(\"5\"):"
                    + "\n    IMethods.simpleMethodWithArgument(\"4\"): expected: 1, actual: 0"
                    + "\n    IMethods.simpleMethodWithArgument(\"3\"): expected: 1, actual: 0"
                    + "\n    IMethods.simpleMethodWithArgument(\"2\"): expected: 1, actual: 0"
                    + "\n    IMethods.simpleMethodWithArgument(\"0\"): expected: 1, actual: 0"
                    + "\n    IMethods.simpleMethodWithArgument(\"1\"): expected: 1, actual: 0", expected
                    .getMessage());
        }

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

        boolean failed = false;
        try {
            mock.simpleMethodWithArgument("4");
        } catch (final AssertionError e) {
            failed = true;
        }
        if (!failed) {
            fail();
        }

        mock.simpleMethodWithArgument("3");
        mock.simpleMethodWithArgument("4");
        mock.simpleMethodWithArgument("5");
        mock.simpleMethodWithArgument("6");
        mock.simpleMethodWithArgument("7");

        verify(mock);

    }

    @Test
    public void resumeIfFailure() {
        final IMethods mock = createMock(IMethods.class);
        expect(mock.oneArg(true)).andReturn("foo").anyTimes();
        replay(mock);

        mock.oneArg(true);

        try {
            mock.simpleMethod();
        } catch (final AssertionError error) {
        }

        mock.oneArg(true);

        verify(mock);
    }

    @Test
    public void defaultResetToNice() {
        final IMethods mock = createMock(IMethods.class);

        expect(mock.oneArg(true)).andReturn("foo");
        replay(mock);

        resetToNice(mock);

        replay(mock);

        assertNull(mock.oneArg(true));

        verify(mock);
    }

    @Test
    public void strictResetToDefault() {
        final IMethods mock = createStrictMock(IMethods.class);

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
        final IMethods mock = createNiceMock(IMethods.class);

        expect(mock.oneArg(false)).andReturn("foo");

        replay(mock);

        assertNull(mock.oneArg(true));

        resetToStrict(mock);

        expect(mock.oneArg(false)).andReturn("foo");
        expect(mock.oneArg(true)).andReturn("foo");

        replay(mock);

        try {
            mock.oneArg(true);
            fail();
        } catch (final AssertionError e) {
        }

        assertEquals("foo", mock.oneArg(false));
        assertEquals("foo", mock.oneArg(true));

        verify(mock);
    }
}
