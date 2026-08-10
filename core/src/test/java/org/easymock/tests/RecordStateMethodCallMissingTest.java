/*
 * Copyright 2001-2026 the original author or authors.
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

import org.easymock.internal.MocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.createControl;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author OFFIS, Tammo Freese
 */
class RecordStateMethodCallMissingTest {

    private static final String METHOD_CALL_NEEDED = "method call on the mock needed before setting ";

    IMethods mock;

    MocksControl control;

    @BeforeEach
    void setup() {
        control = (MocksControl) createControl(); // this cast is a hack. It will provoke the errors below, but I don't think it can happen using EasyMock normally
        mock = control.createMock(IMethods.class);
    }

    private void assertMessage(String suffix, IllegalStateException expected) {
        assertEquals(METHOD_CALL_NEEDED + suffix, expected.getMessage());
    }

    @Test
    void setBooleanReturnValueWithoutMethodCall() {
        try {
            control.andReturn(false);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("return value", expected);
        }
    }

    @Test
    void setLongReturnValueWithoutMethodCall() {
        try {
            control.andReturn(0L);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("return value", expected);
        }
    }

    @Test
    void setFloatReturnValueWithoutMethodCall() {
        try {
            control.andReturn(0.0f);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("return value", expected);
        }
    }

    @Test
    void setDoubleReturnValueWithoutMethodCall() {
        try {
            control.andReturn(0.0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("return value", expected);
        }
    }

    @Test
    void setObjectReturnValueWithoutMethodCall() {
        try {
            control.andReturn(null);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("return value", expected);
        }
    }

    @Test
    void setThrowableWithoutMethodCall() {
        try {
            control.andThrow(new RuntimeException());
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("Throwable", expected);
        }
    }

    @Test
    void setAnswerWithoutMethodCall() {
        try {
            control.andAnswer(() -> null);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("answer", expected);
        }
    }

    @Test
    void setDelegateToWithoutMethodCall() {
        try {
            control.andDelegateTo(null);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("delegate", expected);
        }
    }

    @Test
    void setAnyTimesWithoutMethodCall() {
        try {
            control.anyTimes();
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("times", expected);
        }
    }

    @Test
    void setAtLeastOnceWithoutMethodCall() {
        try {
            control.atLeastOnce();
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("times", expected);
        }
    }

    @Test
    void setTimesWithoutMethodCall() {
        try {
            control.times(3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("times", expected);
        }
    }

    @Test
    void setTimesMinMaxWithoutMethodCall() {
        try {
            control.times(1, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("times", expected);
        }
    }

    @Test
    void setOnceWithoutMethodCall() {
        try {
            control.once();
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("times", expected);
        }
    }

    @Test
    void setBooleanDefaultReturnValueWithoutMethodCall() {
        try {
            control.andStubReturn(false);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("stub return value", expected);
        }
    }

    @Test
    void setLongDefaultReturnValueWithoutMethodCall() {
        try {
            control.andStubReturn(0L);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("stub return value", expected);
        }
    }

    @Test
    void setFloatDefaultReturnValueWithoutMethodCall() {
        try {
            control.andStubReturn(0.0f);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("stub return value", expected);
        }
    }

    @Test
    void setDoubleDefaultReturnValueWithoutMethodCall() {
        try {
            control.andStubReturn(0.0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("stub return value", expected);
        }
    }

    @Test
    void setObjectDefaultReturnValueWithoutMethodCall() {
        try {
            control.andStubReturn(null);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("stub return value", expected);
        }
    }

    @Test
    void setDefaultVoidCallableWithoutMethodCall() {
        try {
            control.asStub();
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("stub behavior", expected);
        }
    }

    @Test
    void setDefaultThrowableWithoutMethodCall() {
        try {
            control.andStubThrow(new RuntimeException());
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("stub Throwable", expected);
        }
    }

    @Test
    void setStubAnswerWithoutMethodCall() {
        try {
            control.andStubAnswer(() -> null);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("stub answer", expected);
        }
    }

    @Test
    void setStubDelegateToWithoutMethodCall() {
        try {
            control.andStubDelegateTo(null);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertMessage("stub delegate", expected);
        }
    }

    @Test
    void timesWithoutReturnValue() {
        mock.booleanReturningMethod(1);
        try {
            expectLastCall().times(3);
            fail();
        } catch (IllegalStateException expected) {
            assertEquals("last method called on mock is not a void method", expected.getMessage());
        }
    }

    @Test
    void asStubWithNonVoidMethod() {
        mock.booleanReturningMethod(1);
        try {
            expectLastCall().asStub();
            fail();
        } catch (IllegalStateException expected) {
            assertEquals("last method called on mock is not a void method", expected.getMessage());
        }
    }

    @Test
    void andVoidWithNonVoidMethod() {
        mock.booleanReturningMethod(1);
        try {
            expectLastCall().andVoid();
            fail();
        } catch (IllegalStateException expected) {
            assertEquals("last method called on mock is not a void method", expected.getMessage());
        }
    }
}
