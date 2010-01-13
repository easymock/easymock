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
public class RecordStateInvalidUsageTest {

    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void setReturnValueWithoutMethodCall() {
        try {
            control.setReturnValue(false);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    @Test
    public void setExpectedVoidCallCountWithoutMethodCall() {
        try {
            control.setVoidCallable(3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting void callable",
                    expected.getMessage());
        }
    }

    @Test
    public void openVoidCallCountWithoutMethodCall() {
        try {
            control.setVoidCallable();
            fail("IllegalStateException expected");
        } catch (Exception expected) {
            assertEquals(
                    "method call on the mock needed before setting void callable",
                    expected.getMessage());
        }
    }

    @Test
    public void setWrongReturnValueBoolean() {
        mock.oneArg(false);
        try {
            control.setReturnValue(false);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    @Test
    public void setWrongReturnValueShort() {
        mock.oneArg(false);
        try {
            control.setReturnValue((short) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    @Test
    public void setWrongReturnValueChar() {
        mock.oneArg(false);
        try {
            control.setReturnValue((char) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    @Test
    public void setWrongReturnValueInt() {
        mock.oneArg(false);
        try {
            control.setReturnValue(0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    @Test
    public void setWrongReturnValueLong() {
        mock.oneArg(false);
        try {
            control.setReturnValue((long) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    @Test
    public void setWrongReturnValueFloat() {
        mock.oneArg(false);
        try {
            control.setReturnValue((float) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    @Test
    public void setWrongReturnValueDouble() {
        mock.oneArg(false);
        try {
            control.setReturnValue((double) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    @Test
    public void setWrongReturnValueObject() {
        mock.oneArg(false);
        try {
            control.setReturnValue(new Object());
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }
}