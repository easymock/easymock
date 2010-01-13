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

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
@SuppressWarnings("deprecation")
public class RecordStateMethodCallMissingTest {
    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void setBooleanReturnValueWithoutMethodCall() {
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
    public void setLongReturnValueWithoutMethodCall() {
        try {
            control.setReturnValue(0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    @Test
    public void setFloatReturnValueWithoutMethodCall() {
        try {
            control.setReturnValue((float) 0.0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    @Test
    public void setDoubleReturnValueWithoutMethodCall() {
        try {
            control.setReturnValue(0.0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    @Test
    public void setObjectReturnValueWithoutMethodCall() {
        try {
            control.setReturnValue(null);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    @Test
    public void setVoidCallableWithoutMethodCall() {
        try {
            control.setVoidCallable();
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting void callable",
                    expected.getMessage());
        }
    }

    @Test
    public void setThrowableWithoutMethodCall() {
        try {
            control.setThrowable(new RuntimeException());
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting Throwable",
                    expected.getMessage());
        }
    }

    @Test
    public void setBooleanReturnValueCountWithoutMethodCall() {
        try {
            control.setReturnValue(false, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    @Test
    public void setLongReturnValueCountWithoutMethodCall() {
        try {
            control.setReturnValue(0, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    @Test
    public void setFloatReturnValueCountWithoutMethodCall() {
        try {
            control.setReturnValue((float) 0.0, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    @Test
    public void setDoubleReturnValueCountWithoutMethodCall() {
        try {
            control.setReturnValue(0.0, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    @Test
    public void setObjectReturnValueCountWithoutMethodCall() {
        try {
            control.setReturnValue(null, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    @Test
    public void setVoidCallableCountWithoutMethodCall() {
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
    public void setThrowableCountWithoutMethodCall() {
        try {
            control.setThrowable(new RuntimeException(), 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting Throwable",
                    expected.getMessage());
        }
    }

    @Test
    public void setBooleanDefaultReturnValueWithoutMethodCall() {
        try {
            control.setDefaultReturnValue(false);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting default return value",
                    expected.getMessage());
        }
    }

    @Test
    public void setLongDefaultReturnValueWithoutMethodCall() {
        try {
            control.setDefaultReturnValue(0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting default return value",
                    expected.getMessage());
        }
    }

    @Test
    public void setFloatDefaultReturnValueWithoutMethodCall() {
        try {
            control.setDefaultReturnValue((float) 0.0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting default return value",
                    expected.getMessage());
        }
    }

    @Test
    public void setDoubleDefaultReturnValueWithoutMethodCall() {
        try {
            control.setDefaultReturnValue(0.0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting default return value",
                    expected.getMessage());
        }
    }

    @Test
    public void setObjectDefaultReturnValueWithoutMethodCall() {
        try {
            control.setDefaultReturnValue(null);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting default return value",
                    expected.getMessage());
        }
    }

    @Test
    public void setDefaultVoidCallableWithoutMethodCall() {
        try {
            control.setDefaultVoidCallable();
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting default void callable",
                    expected.getMessage());
        }
    }

    @Test
    public void setDefaultThrowableWithoutMethodCall() {
        try {
            control.setDefaultThrowable(new RuntimeException());
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting default Throwable",
                    expected.getMessage());
        }
    }

    @Test
    public void timesWithoutReturnValue() {
        mock.booleanReturningMethod(1);
        try {
            expectLastCall().times(3);
            fail();
        } catch (IllegalStateException expected) {
            assertEquals("last method called on mock is not a void method",
                    expected.getMessage());
        }
    }

    @Test
    public void asStubWithNonVoidMethod() {
        mock.booleanReturningMethod(1);
        try {
            expectLastCall().asStub();
            fail();
        } catch (IllegalStateException expected) {
            assertEquals("last method called on mock is not a void method",
                    expected.getMessage());
        }
    }

}
