/*
 * Copyright 2001-2009 OFFIS, Tammo Freese
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

import org.easymock.AbstractMatcher;
import org.easymock.ArgumentsMatcher;
import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class ArgumentsMatcherTest {

    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setUp() {
        control = MockControl.createStrictControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void expectedArgumentsDelegatedToMatcher() {
        mock.twoArgumentMethod(0, 5);
        control.setMatcher(new AbstractMatcher() {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean matches(Object[] expected, Object[] actual) {
                assertEquals(0, ((Integer) expected[0]).intValue());
                assertEquals(5, ((Integer) expected[1]).intValue());
                assertEquals(1, ((Integer) actual[0]).intValue());
                assertEquals(6, ((Integer) actual[1]).intValue());
                return true;
            }
        });
        mock.simpleMethod();
        control.replay();
        mock.twoArgumentMethod(1, 6);
        mock.simpleMethod();
        control.verify();
    }

    @Test
    public void expectedArgumentsDelegatedToMatcher2() {
        mock.threeArgumentMethod(7, "", "A test");
        control.setMatcher(new AbstractMatcher() {
            
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean matches(Object[] expected, Object[] actual) {
                int expectedInt = ((Integer) expected[0]).intValue();
                int actualInt = ((Integer) actual[0]).intValue();
                return expectedInt < actualInt;
            }
        });
        control.setReturnValue("1");
        mock.threeArgumentMethod(6, "", "A test");
        control.setReturnValue("2");
        mock.threeArgumentMethod(12, "", "A test");
        control.setReturnValue("3");

        control.replay();
        mock.threeArgumentMethod(9, "test", "test");
        mock.threeArgumentMethod(8, "test", "test");
        mock.threeArgumentMethod(13, "test", "test");
        control.verify();
    }

    @Test
    public void errorString() {
        mock.twoArgumentMethod(0, 5);
        control.setMatcher(new ArgumentsMatcher() {
            public boolean matches(Object[] expected, Object[] actual) {
                return false;
            }

            public String toString(Object[] arguments) {
                return "<<" + arguments[0] + ">>";
            }
        });
        control.replay();
        boolean failed = false;
        try {
            mock.twoArgumentMethod(1, 5);
        } catch (AssertionError expected) {
            failed = true;
            assertEquals("\n  Unexpected method call twoArgumentMethod(1, 5):"
                    + "\n    twoArgumentMethod(<<0>>): expected: 1, actual: 0",
                    expected.getMessage());
        }
        if (!failed) {
            fail("exception expected");
        }
    }

    @Test
    public void settingTheSameMatcherIsOk() {
        try {
            mock.twoArgumentMethod(1, 2);
            control.setMatcher(MockControl.ARRAY_MATCHER);
            control.setMatcher(MockControl.ARRAY_MATCHER);
            mock.twoArgumentMethod(1, 2);
            control.setMatcher(MockControl.ARRAY_MATCHER);

        } catch (IllegalStateException unexpected) {
            fail("no exception should be thrown if the same matcher is set twice");
        }
    }

    @Test
    public void abstractMatcher() {
        AbstractMatcher trueMatcher = new AbstractMatcher() {
            
            private static final long serialVersionUID = 1L;
            
            protected boolean parameterMatches(Object expected, Object actual) {
                return true;
            }
        };
        Object[] arrayWithNull = new Object[] { null };
        Object[] arrayWithObject = new Object[] { new Object() };
        assertFalse(trueMatcher.matches(arrayWithNull, arrayWithObject));
        assertFalse(trueMatcher.matches(arrayWithObject, arrayWithNull));
    }

    @Test
    public void abstractMatcherToStringHandlesNullArray() {
        AbstractMatcher matcher = new AbstractMatcher() {            
            private static final long serialVersionUID = 1L;
        };
        assertEquals("", matcher.toString(null));
    }

}