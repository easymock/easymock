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
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.andReturn(false));
        assertMessage("return value", expected);
    }

    @Test
    void setLongReturnValueWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.andReturn(0L));
        assertMessage("return value", expected);
    }

    @Test
    void setFloatReturnValueWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.andReturn(0.0f));
        assertMessage("return value", expected);
    }

    @Test
    void setDoubleReturnValueWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.andReturn(0.0));
        assertMessage("return value", expected);
    }

    @Test
    void setObjectReturnValueWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.andReturn(null));
        assertMessage("return value", expected);
    }

    @Test
    void setThrowableWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.andThrow(new RuntimeException()));
        assertMessage("Throwable", expected);
    }

    @Test
    void setAnswerWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.andAnswer(() -> null));
        assertMessage("answer", expected);
    }

    @Test
    void setDelegateToWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.andDelegateTo(null));
        assertMessage("delegate", expected);
    }

    @Test
    void setAnyTimesWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.anyTimes());
        assertMessage("times", expected);
    }

    @Test
    void setAtLeastOnceWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.atLeastOnce());
        assertMessage("times", expected);
    }

    @Test
    void setTimesWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.times(3));
        assertMessage("times", expected);
    }

    @Test
    void setTimesMinMaxWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.times(1, 3));
        assertMessage("times", expected);
    }

    @Test
    void setOnceWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.once());
        assertMessage("times", expected);
    }

    @Test
    void setBooleanDefaultReturnValueWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.andStubReturn(false));
        assertMessage("stub return value", expected);
    }

    @Test
    void setLongDefaultReturnValueWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.andStubReturn(0L));
        assertMessage("stub return value", expected);
    }

    @Test
    void setFloatDefaultReturnValueWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.andStubReturn(0.0f));
        assertMessage("stub return value", expected);
    }

    @Test
    void setDoubleDefaultReturnValueWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.andStubReturn(0.0));
        assertMessage("stub return value", expected);
    }

    @Test
    void setObjectDefaultReturnValueWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.andStubReturn(null));
        assertMessage("stub return value", expected);
    }

    @Test
    void setDefaultVoidCallableWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.asStub());
        assertMessage("stub behavior", expected);
    }

    @Test
    void setDefaultThrowableWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.andStubThrow(new RuntimeException()));
        assertMessage("stub Throwable", expected);
    }

    @Test
    void setStubAnswerWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.andStubAnswer(() -> null));
        assertMessage("stub answer", expected);
    }

    @Test
    void setStubDelegateToWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> control.andStubDelegateTo(null));
        assertMessage("stub delegate", expected);
    }

    @Test
    void timesWithoutReturnValue() {
        mock.booleanReturningMethod(1);
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> expectLastCall().times(3));
        assertEquals("last method called on mock is not a void method", expected.getMessage());
    }

    @Test
    void asStubWithNonVoidMethod() {
        mock.booleanReturningMethod(1);
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> expectLastCall().asStub());
        assertEquals("last method called on mock is not a void method", expected.getMessage());
    }

    @Test
    void andVoidWithNonVoidMethod() {
        mock.booleanReturningMethod(1);
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> expectLastCall().andVoid());
        assertEquals("last method called on mock is not a void method", expected.getMessage());
    }
}
