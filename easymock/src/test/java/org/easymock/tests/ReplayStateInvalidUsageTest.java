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

import org.easymock.EasyMock;
import org.easymock.IExpectationSetters;
import org.easymock.IMocksControl;
import org.easymock.internal.ReplayState;
import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class ReplayStateInvalidUsageTest {

    private IMethods mock;

    private Exception exception;

    private ReplayState replayState;

    private IMocksControl mocksControl;

    private IExpectationSetters<String> expectationSetters;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        exception = new Exception();
        mock = EasyMock.createMock(IMethods.class);
        EasyMock.replay(mock);
        mocksControl = EasyMock.createControl();
        mocksControl.replay();
        expectationSetters = (IExpectationSetters<String>) mocksControl;
    }

    @Test(expected = IllegalStateException.class)
    public void setVoidCallable() {
        expectLastCall();
    }

    @Test(expected = IllegalStateException.class)
    public void replay() {
        EasyMock.replay(mock);
    }

    @Test(expected = IllegalStateException.class)
    public void createMock() {
        mocksControl.createMock(IMethods.class);
    }

    @Test(expected = IllegalStateException.class)
    public void createMockWithName() {
        mocksControl.createMock("", IMethods.class);
    }

    @Test(expected = IllegalStateException.class)
    public void checkOrder() {
        mocksControl.checkOrder(true);
    }

    @Test(expected = IllegalStateException.class)
    public void makeThreadSafe() {
        mocksControl.makeThreadSafe(true);
    }

    @Test(expected = IllegalStateException.class)
    public void checkIsUsedInOneThread() {
        mocksControl.checkIsUsedInOneThread(true);
    }

    @Test(expected = IllegalStateException.class)
    public void andStubReturn() {
        expectationSetters.andStubReturn("7");
    }

    @Test(expected = IllegalStateException.class)
    public void andStubThrow() {
        expectationSetters.andStubThrow(new RuntimeException());
    }

    @Test(expected = IllegalStateException.class)
    public void asStub() {
        expectationSetters.asStub();
    }

    @Test(expected = IllegalStateException.class)
    public void times() {
        expectationSetters.times(3);
    }

    @Test(expected = IllegalStateException.class)
    public void anyTimes() {
        expectationSetters.anyTimes();
    }
}