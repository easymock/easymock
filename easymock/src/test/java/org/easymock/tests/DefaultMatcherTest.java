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

@SuppressWarnings("deprecation")
public class DefaultMatcherTest {

    public static interface ArrayInterface {
        void methodA(int[] argument);

        void methodB(int[] argument);
    }

    private MockControl<ArrayInterface> control;

    private ArrayInterface mock;

    @Before
    public void setup() {
        control = MockControl.createControl(ArrayInterface.class);
        mock = control.getMock();
    }

    @Test
    public void defaultMatcher() {
        control.setDefaultMatcher(MockControl.ARRAY_MATCHER);

        mock.methodA(new int[] { 1, 1 });
        mock.methodB(new int[] { 2, 2 });

        control.replay();

        mock.methodA(new int[] { 1, 1 });
        mock.methodB(new int[] { 2, 2 });

        control.verify();
    }

    @Test
    public void failInReplayState() {
        control.replay();
        try {
            control.setDefaultMatcher(MockControl.ARRAY_MATCHER);
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    public void failIfDefaultMatcherSetTwice() {
        control.setDefaultMatcher(MockControl.ARRAY_MATCHER);
        try {
            control.setDefaultMatcher(MockControl.ARRAY_MATCHER);
            fail();
        } catch (IllegalStateException expected) {
            assertEquals(
                    "default matcher can only be set once directly after creation of the MockControl",
                    expected.getMessage());
        }
    }

    @Test
    public void defaultMatcherSetTooLate() {
        int[] integers = new int[] { 1, 1 };
        int[] integers2 = new int[] { 2, 2 };
        mock.methodA(integers);
        control.setVoidCallable();
        control.setDefaultMatcher(MockControl.ARRAY_MATCHER);
        mock.methodA(integers2);
        control.setVoidCallable();
        control.replay();

        boolean failed = true;
        try {
            mock.methodA(new int[] { 1, 1 });
            failed = false;
        } catch (AssertionError expected) {
        }
        if (!failed) {
            fail();
        }
        mock.methodA(integers);
        mock.methodA(new int[] { 2, 2 });
        control.verify();
    }
}
