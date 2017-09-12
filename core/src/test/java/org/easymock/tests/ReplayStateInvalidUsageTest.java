/**
 * Copyright 2001-2017 the original author or authors.
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

import org.easymock.EasyMock;
import org.easymock.IExpectationSetters;
import org.easymock.IMocksControl;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.easymock.EasyMock.*;

/**
 * @author OFFIS, Tammo Freese
 */
public class ReplayStateInvalidUsageTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private IMethods mock;

    private IMocksControl mocksControl;

    private IExpectationSetters<String> expectationSetters;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        mock = EasyMock.createMock(IMethods.class);
        EasyMock.replay(mock);
        mocksControl = EasyMock.createControl();
        mocksControl.replay();
        expectationSetters = (IExpectationSetters<String>) mocksControl;
    }

    @Test
    public void setVoidCallable() {
        expectedException.expect(IllegalStateException.class);
        expectLastCall();
    }

    @Test
    public void replay() {
        expectedException.expect(IllegalStateException.class);
        EasyMock.replay(mock);
    }

    @Test
    public void replaySameMethod() {
        EasyMock.reset(mock);
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("On mock #1 (zero indexed): This method must not be called in replay state.");
        expectedException.expectCause(CoreMatchers.isA(IllegalStateException.class));
        EasyMock.replay(mock, mock);
    }

    @Test
    public void createMock() {
        expectedException.expect(IllegalStateException.class);
        mocksControl.createMock(IMethods.class);
    }

    @Test
    public void createMockWithName() {
        expectedException.expect(IllegalStateException.class);
        mocksControl.createMock("", IMethods.class);
    }

    @Test
    public void checkOrder() {
        expectedException.expect(IllegalStateException.class);
        mocksControl.checkOrder(true);
    }

    @Test
    public void makeThreadSafe() {
        expectedException.expect(IllegalStateException.class);
        mocksControl.makeThreadSafe(true);
    }

    @Test
    public void checkIsUsedInOneThread() {
        expectedException.expect(IllegalStateException.class);
        mocksControl.checkIsUsedInOneThread(true);
    }

    @Test
    public void andStubReturn() {
        expectedException.expect(IllegalStateException.class);
        expectationSetters.andStubReturn("7");
    }

    @Test
    public void andStubThrow() {
        expectedException.expect(IllegalStateException.class);
        expectationSetters.andStubThrow(new RuntimeException());
    }

    @Test
    public void asStub() {
        expectedException.expect(IllegalStateException.class);
        expectationSetters.asStub();
    }

    @Test
    public void times() {
        expectedException.expect(IllegalStateException.class);
        expectationSetters.times(3);
    }

    @Test
    public void anyTimes() {
        expectedException.expect(IllegalStateException.class);
        expectationSetters.anyTimes();
    }

    @Test
    public void manyMocks() {
        EasyMock.reset(mock);

        expectedException.expect(RuntimeException.class);
        expectedException.expectCause(CoreMatchers.isA(IllegalStateException.class));
        expectedException.expectMessage("On mock #1 (zero indexed): This method must not be called in replay state.");
        EasyMock.replay(mock, mock);
    }
}
