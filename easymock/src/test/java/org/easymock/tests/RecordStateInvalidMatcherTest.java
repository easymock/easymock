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

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
@SuppressWarnings("deprecation")
public class RecordStateInvalidMatcherTest {
    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void setMatcherBeforeCallingMethods() {
        try {
            control.setMatcher(MockControl.ARRAY_MATCHER);
            fail();
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting matcher",
                    expected.getMessage());
        }
    }

    @Test
    public void setMatcherTwice() {
        mock.simpleMethod();
        control.setMatcher(MockControl.ARRAY_MATCHER);
        try {
            control.setMatcher(MockControl.EQUALS_MATCHER);
            fail();
        } catch (IllegalStateException expected) {
            assertEquals(
                    "for method simpleMethod(), a matcher has already been set",
                    expected.getMessage());
        }
    }

    @Test
    public void setMatcherTwice2() {
        mock.simpleMethodWithArgument("");
        control.setMatcher(MockControl.ARRAY_MATCHER);
        try {
            control.setMatcher(MockControl.EQUALS_MATCHER);
            fail();
        } catch (IllegalStateException expected) {
            assertEquals(
                    "for method simpleMethodWithArgument(...), a matcher has already been set",
                    expected.getMessage());
        }
    }

    @Test
    public void setSameMatcherTwice() {
        mock.simpleMethod();
        control.setMatcher(MockControl.ARRAY_MATCHER);
        try {
            control.setMatcher(MockControl.ARRAY_MATCHER);
        } catch (IllegalStateException unexpected) {
            fail("setting the same matcher should work");
        }
    }
}
