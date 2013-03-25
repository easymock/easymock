/**
 * Copyright 2001-2013 the original author or authors.
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

import org.easymock.IAnswer;
import org.easymock.internal.MocksControl;
import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class RecordStateMethodCallMissingTest {

    private static final String METHOD_CALL_NEEDED = "method call on the mock needed before setting ";

    IMethods mock;

    MocksControl control;

    @Before
    public void setup() {
        control = (MocksControl) createControl(); // this cast is a hack. It will provoke the errors below but I don't think it can happen using EasyMock normally
        mock = control.createMock(IMethods.class);
    }

    private void assertMessage(final String suffix, final IllegalStateException expected) {
        assertEquals(METHOD_CALL_NEEDED + suffix, expected.getMessage());
    }

    @Test
    public void setBooleanReturnValueWithoutMethodCall() {
        try {
            control.andReturn(false);
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("return value", expected);
        }
    }

    @Test
    public void setLongReturnValueWithoutMethodCall() {
        try {
            control.andReturn(0L);
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("return value", expected);
        }
    }

    @Test
    public void setFloatReturnValueWithoutMethodCall() {
        try {
            control.andReturn(0.0f);
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("return value", expected);
        }
    }

    @Test
    public void setDoubleReturnValueWithoutMethodCall() {
        try {
            control.andReturn(0.0);
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("return value", expected);
        }
    }

    @Test
    public void setObjectReturnValueWithoutMethodCall() {
        try {
            control.andReturn(null);
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("return value", expected);
        }
    }

    @Test
    public void setThrowableWithoutMethodCall() {
        try {
            control.andThrow(new RuntimeException());
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("Throwable", expected);
        }
    }

    @Test
    public void setAnswerWithoutMethodCall() {
        try {
            control.andAnswer(new IAnswer<Object>() {
                public Object answer() throws Throwable {
                    return null;
                }

            });
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("answer", expected);
        }
    }

    @Test
    public void setDelegateToWithoutMethodCall() {
        try {
            control.andDelegateTo(null);
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("delegate", expected);
        }
    }

    @Test
    public void setAnyTimesWithoutMethodCall() {
        try {
            control.anyTimes();
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("times", expected);
        }
    }

    @Test
    public void setAtLeastOnceWithoutMethodCall() {
        try {
            control.atLeastOnce();
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("times", expected);
        }
    }

    @Test
    public void setTimesWithoutMethodCall() {
        try {
            control.times(3);
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("times", expected);
        }
    }

    @Test
    public void setTimesMinMaxWithoutMethodCall() {
        try {
            control.times(1, 3);
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("times", expected);
        }
    }

    @Test
    public void setOnceWithoutMethodCall() {
        try {
            control.once();
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("times", expected);
        }
    }

    @Test
    public void setBooleanDefaultReturnValueWithoutMethodCall() {
        try {
            control.andStubReturn(false);
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("stub return value", expected);
        }
    }

    @Test
    public void setLongDefaultReturnValueWithoutMethodCall() {
        try {
            control.andStubReturn(0L);
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("stub return value", expected);
        }
    }

    @Test
    public void setFloatDefaultReturnValueWithoutMethodCall() {
        try {
            control.andStubReturn(0.0f);
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("stub return value", expected);
        }
    }

    @Test
    public void setDoubleDefaultReturnValueWithoutMethodCall() {
        try {
            control.andStubReturn(0.0);
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("stub return value", expected);
        }
    }

    @Test
    public void setObjectDefaultReturnValueWithoutMethodCall() {
        try {
            control.andStubReturn(null);
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("stub return value", expected);
        }
    }

    @Test
    public void setDefaultVoidCallableWithoutMethodCall() {
        try {
            control.asStub();
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("stub behavior", expected);
        }
    }

    @Test
    public void setDefaultThrowableWithoutMethodCall() {
        try {
            control.andStubThrow(new RuntimeException());
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("stub Throwable", expected);
        }
    }

    @Test
    public void setStubAnswerWithoutMethodCall() {
        try {
            control.andStubAnswer(new IAnswer<Object>() {
                public Object answer() throws Throwable {
                    return null;
                }

            });
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("stub answer", expected);
        }
    }

    @Test
    public void setStubDelegateToWithoutMethodCall() {
        try {
            control.andStubDelegateTo(null);
            fail("IllegalStateException expected");
        } catch (final IllegalStateException expected) {
            assertMessage("stub delegate", expected);
        }
    }

    @Test
    public void timesWithoutReturnValue() {
        mock.booleanReturningMethod(1);
        try {
            expectLastCall().times(3);
            fail();
        } catch (final IllegalStateException expected) {
            assertEquals("last method called on mock is not a void method", expected.getMessage());
        }
    }

    @Test
    public void asStubWithNonVoidMethod() {
        mock.booleanReturningMethod(1);
        try {
            expectLastCall().asStub();
            fail();
        } catch (final IllegalStateException expected) {
            assertEquals("last method called on mock is not a void method", expected.getMessage());
        }
    }

}
