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
public class RecordStateInvalidReturnValueTest {
    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void setInvalidBooleanReturnValue() {
        mock.oneArg(false);
        try {
            control.setReturnValue(false);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }

    }

    @Test
    public void setInvalidLongReturnValue() {
        mock.oneArg(false);
        try {
            control.setReturnValue((long) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidFloatReturnValue() {
        mock.oneArg(false);
        try {
            control.setReturnValue((float) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidDoubleReturnValue() {
        mock.oneArg(false);
        try {
            control.setReturnValue((double) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidObjectReturnValue() {
        mock.oneArg(false);
        try {
            control.setReturnValue(new Object());
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidBooleanReturnValueCount() {
        mock.oneArg(false);
        try {
            control.setReturnValue(false, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }

    }

    @Test
    public void setInvalidLongReturnValueCount() {
        mock.oneArg(false);
        try {
            control.setReturnValue((long) 0, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidFloatReturnValueCount() {
        mock.oneArg(false);
        try {
            control.setReturnValue((float) 0, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidDoubleReturnValueCount() {
        mock.oneArg(false);
        try {
            control.setReturnValue((double) 0, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidObjectReturnValueCount() {
        mock.oneArg(false);
        try {
            control.setReturnValue(new Object(), 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setReturnValueForVoidMethod() {
        mock.simpleMethod();
        try {
            control.setReturnValue(null);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("void method cannot return a value", e.getMessage());
        }
    }

    @Test
    public void nullForPrimitive() {
        try {
            control.expectAndReturn(mock.longReturningMethod(4), null);
            fail("null not allowed");
        } catch (IllegalStateException e) {
            assertEquals(
                    "can't return null for a method returning a primitive type",
                    e.getMessage());
        }
    }
}
