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
import org.easymock.internal.RecordState;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class RecordStateInvalidStateChangeTest {
    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void activateWithoutReturnValue() {
        mock.oneArg(false);
        try {
            control.replay();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals(
                    "missing behavior definition for the preceding method call oneArg(false)",
                    e.getMessage());
            assertTrue("stack trace must be cut", Util.getStackTrace(e)
                    .indexOf(RecordState.class.getName()) == -1);
        }
    }

    @Test
    public void secondCallWithoutReturnValue() {
        mock.oneArg(false);
        try {
            mock.oneArg(false);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals(
                    "missing behavior definition for the preceding method call oneArg(false)",
                    e.getMessage());
            assertTrue("stack trace must be cut", Util.getStackTrace(e)
                    .indexOf(RecordState.class.getName()) == -1);
        }
    }

    @Test
    public void verifyWithoutActivation() {
        try {
            control.verify();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("calling verify is not allowed in record state", e
                    .getMessage());
        }
    }
}
