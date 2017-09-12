/**
 * Copyright 2001-2017 the original author or authors.
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

import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author OFFIS, Tammo Freese
 */
public class UsageTest {

    private IMethods mock;

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
        } catch (AssertionError expected) {
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
                new IndexOutOfBoundsException()).once();

        replay(mock);

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
    public void moreThanOneArgument() {
        expect(mock.threeArgumentMethod(1, "2", "3")).andReturn("Test").times(2);

        replay(mock);

        assertEquals("Test", mock.threeArgumentMethod(1, "2", "3"));

        boolean failed = true;
        try {
            verify(mock);
            failed = false;
        } catch (AssertionError expected) {
            assertEquals("\n  Expectation failure on verify:"
                    + "\n    IMethods.threeArgumentMethod(1 (int), \"2\", \"3\"): expected: 2, actual: 1", expected
                    .getMessage());
        }
        if (!failed) {
            fail("exception expected");
        }
    }

    @Test
    public void unexpectedCallWithArray() {
        reset(mock);
        replay(mock);
        String[] strings = new String[] { "Test" };
        try {
            mock.arrayMethod(strings);
            fail("exception expected");
        } catch (AssertionError expected) {
            assertEquals("\n  Unexpected method call IMethods.arrayMethod(" + "[\"Test\"]" + "):", expected
                    .getMessage());
        }
    }

    @Test
    public void wrongArguments() {
        mock.simpleMethodWithArgument("3");
        replay(mock);

        try {
            mock.simpleMethodWithArgument("5");
            fail();
        } catch (AssertionError expected) {
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
        } catch (AssertionError expected) {
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
            fail();
        } catch (AssertionError expected) {
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
    public void chainVoidMethodCalls() {
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
    public void chainVoidMethodCallsVoidFirst() {
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
    public void chainVoidWithItself() {
        mock.simpleMethodWithArgument("4");
        expectLastCall().andVoid().times(2).andVoid();
        replay(mock);
        mock.simpleMethodWithArgument("4");
        mock.simpleMethodWithArgument("4");
        mock.simpleMethodWithArgument("4");
        verify(mock);
    }

    @Test
    public void boxingArgument() {
        Long value = Long.valueOf(1);
        expect(mock.oneLongArg(value)).andReturn("test");
        replay(mock);
        assertEquals("test", mock.oneLongArg(value));
        verify(mock);
    }
}
