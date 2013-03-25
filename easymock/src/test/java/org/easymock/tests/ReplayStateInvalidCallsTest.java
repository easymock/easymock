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

import org.easymock.internal.MocksBehavior;
import org.easymock.internal.Range;
import org.easymock.internal.ReplayState;
import org.easymock.internal.RuntimeExceptionWrapper;
import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class ReplayStateInvalidCallsTest {

    private ReplayState control;

    private Exception exception;

    @Before
    public void setUp() {
        exception = new Exception();
        control = new ReplayState(new MocksBehavior(false));
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void expectAndThrowLongWithMinMax() {
        control.andThrow(exception);
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void expectAndReturnObjectWithMinMax() {
        control.andReturn("");
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void asStub() {
        control.asStub();
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void setDefaultReturnValue() {
        control.setDefaultReturnValue("");
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void setDefaultThrowable() {
        control.setDefaultThrowable(exception);
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void setDefaultVoidCallable() {
        control.setDefaultVoidCallable();
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void replay() {
        control.replay();
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void checkOrder() {
        control.checkOrder(true);
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void makeThreadSafe() {
        control.makeThreadSafe(true);
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void andStubReturn() {
        control.andStubReturn("7");
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void andStubThrow() {
        control.andStubThrow(new RuntimeException());
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void andStubAnswer() {
        control.andStubAnswer(null);
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void andStubDelegateTo() {
        control.andStubDelegateTo(null);
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void times() {
        control.times(new Range(0, 1));
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void callback() {
        control.callback(new Runnable() {
            public void run() {
            };
        });
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void andReturn() {
        control.andReturn(null);
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void andThrow() {
        control.andThrow(new RuntimeException());
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void andAnswer() {
        control.andAnswer(null);
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void andDelegateTo() {
        control.andDelegateTo(null);
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void defaultThrowable() {
        control.setDefaultThrowable(new RuntimeException());
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void defaultReturnValue() {
        control.setDefaultReturnValue(null);
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void defaultVoidCallable() {
        control.setDefaultVoidCallable();
    }
}