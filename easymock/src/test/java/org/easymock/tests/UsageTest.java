/**
 * Copyright 2001-2010 the original author or authors.
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

import static org.junit.Assert.*;

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
@SuppressWarnings("deprecation")
public class UsageTest {

    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void exactCallCountByLastCall() {
        mock.oneArg(false);
        control.setReturnValue("Test");
        control.setReturnValue("Test2");

        control.replay();

        assertEquals("Test", mock.oneArg(false));
        assertEquals("Test2", mock.oneArg(false));

        boolean failed = false;
        try {
            mock.oneArg(false);
        } catch (AssertionError expected) {
            failed = true;
        }
        if (!failed)
            fail("expected AssertionError");
    }

    @Test
    public void openCallCountByLastCall() {
        mock.oneArg(false);
        control.setReturnValue("Test");
        control.setReturnValue("Test2", MockControl.ONE_OR_MORE);

        control.replay();

        assertEquals("Test", mock.oneArg(false));
        assertEquals("Test2", mock.oneArg(false));
        assertEquals("Test2", mock.oneArg(false));
    }

    @Test
    public void exactCallCountByLastThrowable() {
        mock.oneArg(false);
        control.setReturnValue("Test");
        control.setReturnValue("Test2");
        control.setThrowable(new IndexOutOfBoundsException(), 1);

        control.replay();

        assertEquals("Test", mock.oneArg(false));
        assertEquals("Test2", mock.oneArg(false));

        try {
            mock.oneArg(false);
        } catch (IndexOutOfBoundsException expected) {
        }

        boolean failed = true;
        try {
            try {
                mock.oneArg(false);
            } catch (IndexOutOfBoundsException expected) {
            }
            failed = false;
        } catch (AssertionError expected) {
        }
        if (!failed)
            fail("expected AssertionError");
    }

    @Test
    public void openCallCountByLastThrowable() {
        mock.oneArg(false);
        control.setReturnValue("Test");
        control.setReturnValue("Test2");
        control.setThrowable(new IndexOutOfBoundsException(),
                MockControl.ONE_OR_MORE);

        control.replay();

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
    public void moreThanOneArgument() {
        mock.threeArgumentMethod(1, "2", "3");
        control.setReturnValue("Test", 2);

        control.replay();

        assertEquals("Test", mock.threeArgumentMethod(1, "2", "3"));

        boolean failed = true;
        try {
            control.verify();
            failed = false;
        } catch (AssertionError expected) {
            assertEquals(
                    "\n  Expectation failure on verify:"
                            + "\n    threeArgumentMethod(1, \"2\", \"3\"): expected: 2, actual: 1",
                    expected.getMessage());
        }
        if (!failed) {
            fail("exception expected");
        }
    }

    @Test
    public void unexpectedCallWithArray() {
        control.reset();
        control.setDefaultMatcher(MockControl.ARRAY_MATCHER);
        control.replay();
        boolean failed = false;
        String[] strings = new String[] { "Test" };
        try {
            mock.arrayMethod(strings);
        } catch (AssertionError expected) {
            failed = true;
            assertEquals("\n  Unexpected method call arrayMethod("
                    + "[\"Test\"]" + "):", expected.getMessage());
        }
        if (!failed) {
            fail("exception expected");
        }

    }

    @Test
    public void wrongArguments() {
        mock.simpleMethodWithArgument("3");
        control.replay();

        try {
            mock.simpleMethodWithArgument("5");
            fail();
        } catch (AssertionError expected) {
            assertEquals(
                    "\n  Unexpected method call simpleMethodWithArgument(\"5\"):"
                            + "\n    simpleMethodWithArgument(\"3\"): expected: 1, actual: 0",
                    expected.getMessage());
        }

    }

    @Test
    public void summarizeSameObjectArguments() {
        mock.simpleMethodWithArgument("3");
        mock.simpleMethodWithArgument("3");
        control.replay();

        try {
            mock.simpleMethodWithArgument("5");
            fail();
        } catch (AssertionError expected) {
            assertEquals(
                    "\n  Unexpected method call simpleMethodWithArgument(\"5\"):"
                            + "\n    simpleMethodWithArgument(\"3\"): expected: 2, actual: 0",
                    expected.getMessage());
        }

    }

    @Test
    public void argumentsOrdered() {
        mock.simpleMethodWithArgument("4");
        mock.simpleMethodWithArgument("3");
        mock.simpleMethodWithArgument("2");
        mock.simpleMethodWithArgument("0");
        mock.simpleMethodWithArgument("1");
        control.replay();

        try {
            mock.simpleMethodWithArgument("5");
            fail();
        } catch (AssertionError expected) {
            assertEquals(
                    "\n  Unexpected method call simpleMethodWithArgument(\"5\"):"
                            + "\n    simpleMethodWithArgument(\"4\"): expected: 1, actual: 0"
                            + "\n    simpleMethodWithArgument(\"3\"): expected: 1, actual: 0"
                            + "\n    simpleMethodWithArgument(\"2\"): expected: 1, actual: 0"
                            + "\n    simpleMethodWithArgument(\"0\"): expected: 1, actual: 0"
                            + "\n    simpleMethodWithArgument(\"1\"): expected: 1, actual: 0",
                    expected.getMessage());
        }

    }

}