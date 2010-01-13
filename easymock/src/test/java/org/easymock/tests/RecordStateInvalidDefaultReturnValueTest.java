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
public class RecordStateInvalidDefaultReturnValueTest {
    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void setInvalidDefaultBooleanReturnValue() {
        mock.oneArg(false);
        try {
            control.setDefaultReturnValue(false);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidDefaultLongReturnValue() {
        mock.oneArg(false);
        try {
            control.setDefaultReturnValue((long) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidDefaultFloatReturnValue() {
        mock.oneArg(false);
        try {
            control.setDefaultReturnValue((float) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidDefaultDoubleReturnValue() {
        mock.oneArg(false);
        try {
            control.setDefaultReturnValue((double) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidObjectDefaultReturnValue() {
        mock.oneArg(false);
        try {
            control.setDefaultReturnValue(new Object());
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setDefaultReturnValueWithoutMethodCall() {
        try {
            control.setDefaultReturnValue(new Object());
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals(
                    "method call on the mock needed before setting default return value",
                    e.getMessage());
        }
    }
}
