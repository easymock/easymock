/*
 * Copyright 2001-2024 the original author or authors.
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

import org.easymock.internal.MocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class RecordStateMethodCallMissingTest {

    private static final String METHOD_CALL_NEEDED = "method call on the mock needed before setting ";

    IMethods mock;

    MocksControl control;

    @BeforeEach
    public void setup() {
        control = (MocksControl) createControl(); // this cast is a hack. It will provoke the errors below, but I don't think it can happen using EasyMock normally
        mock = control.createMock(IMethods.class);
    }

    private void assertMessage(String suffix, IllegalStateException expected) {
        Assertions.assertEquals(METHOD_CALL_NEEDED + suffix, expected.getMessage());
    }

    @Test
    public void setBooleanReturnValueWithoutMethodCall() {
        try {
            control.andReturn(false);
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("return value", expected);
        }
    }

    @Test
    public void setLongReturnValueWithoutMethodCall() {
        try {
            control.andReturn(0L);
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("return value", expected);
        }
    }

    @Test
    public void setFloatReturnValueWithoutMethodCall() {
        try {
            control.andReturn(0.0f);
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("return value", expected);
        }
    }

    @Test
    public void setDoubleReturnValueWithoutMethodCall() {
        try {
            control.andReturn(0.0);
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("return value", expected);
        }
    }

    @Test
    public void setObjectReturnValueWithoutMethodCall() {
        try {
            control.andReturn(null);
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("return value", expected);
        }
    }

    @Test
    public void setThrowableWithoutMethodCall() {
        try {
            control.andThrow(new RuntimeException());
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("Throwable", expected);
        }
    }

    @Test
    public void setAnswerWithoutMethodCall() {
        try {
            control.andAnswer(() -> null);
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("answer", expected);
        }
    }

    @Test
    public void setDelegateToWithoutMethodCall() {
        try {
            control.andDelegateTo(null);
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("delegate", expected);
        }
    }

    @Test
    public void setAnyTimesWithoutMethodCall() {
        try {
            control.anyTimes();
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("times", expected);
        }
    }

    @Test
    public void setAtLeastOnceWithoutMethodCall() {
        try {
            control.atLeastOnce();
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("times", expected);
        }
    }

    @Test
    public void setTimesWithoutMethodCall() {
        try {
            control.times(3);
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("times", expected);
        }
    }

    @Test
    public void setTimesMinMaxWithoutMethodCall() {
        try {
            control.times(1, 3);
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("times", expected);
        }
    }

    @Test
    public void setOnceWithoutMethodCall() {
        try {
            control.once();
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("times", expected);
        }
    }

    @Test
    public void setBooleanDefaultReturnValueWithoutMethodCall() {
        try {
            control.andStubReturn(false);
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("stub return value", expected);
        }
    }

    @Test
    public void setLongDefaultReturnValueWithoutMethodCall() {
        try {
            control.andStubReturn(0L);
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("stub return value", expected);
        }
    }

    @Test
    public void setFloatDefaultReturnValueWithoutMethodCall() {
        try {
            control.andStubReturn(0.0f);
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("stub return value", expected);
        }
    }

    @Test
    public void setDoubleDefaultReturnValueWithoutMethodCall() {
        try {
            control.andStubReturn(0.0);
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("stub return value", expected);
        }
    }

    @Test
    public void setObjectDefaultReturnValueWithoutMethodCall() {
        try {
            control.andStubReturn(null);
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("stub return value", expected);
        }
    }

    @Test
    public void setDefaultVoidCallableWithoutMethodCall() {
        try {
            control.asStub();
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("stub behavior", expected);
        }
    }

    @Test
    public void setDefaultThrowableWithoutMethodCall() {
        try {
            control.andStubThrow(new RuntimeException());
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("stub Throwable", expected);
        }
    }

    @Test
    public void setStubAnswerWithoutMethodCall() {
        try {
            control.andStubAnswer(() -> null);
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("stub answer", expected);
        }
    }

    @Test
    public void setStubDelegateToWithoutMethodCall() {
        try {
            control.andStubDelegateTo(null);
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("stub delegate", expected);
        }
    }

    @Test
    public void timesWithoutReturnValue() {
        mock.booleanReturningMethod(1);
        try {
            expectLastCall().times(3);
            Assertions.fail();
        } catch (IllegalStateException expected) {
            Assertions.assertEquals("last method called on mock is not a void method", expected.getMessage());
        }
    }

    @Test
    public void asStubWithNonVoidMethod() {
        mock.booleanReturningMethod(1);
        try {
            expectLastCall().asStub();
            Assertions.fail();
        } catch (IllegalStateException expected) {
            Assertions.assertEquals("last method called on mock is not a void method", expected.getMessage());
        }
    }

    @Test
    public void andVoidWithNonVoidMethod() {
        mock.booleanReturningMethod(1);
        try {
            expectLastCall().andVoid();
            Assertions.fail();
        } catch (IllegalStateException expected) {
            Assertions.assertEquals("last method called on mock is not a void method", expected.getMessage());
        }
    }
}
