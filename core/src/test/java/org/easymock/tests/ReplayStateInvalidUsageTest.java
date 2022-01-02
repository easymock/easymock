/*
 * Copyright 2001-2022 the original author or authors.
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
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * @author OFFIS, Tammo Freese
 */
public class ReplayStateInvalidUsageTest {

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
        assertThrows(IllegalStateException.class, EasyMock::expectLastCall);
    }

    @Test
    public void replay() {
        assertThrows(IllegalStateException.class, () -> EasyMock.replay(mock));
    }

    @Test
    public void replaySameMethod() {
        EasyMock.reset(mock);

        RuntimeException t = assertThrows(RuntimeException.class, () -> EasyMock.replay(mock, mock));
        assertEquals(IllegalStateException.class, t.getCause().getClass());
        assertEquals("On mock #1 (zero indexed): This method must not be called in replay state.", t.getMessage());
    }

    @Test
    public void createMock() {
        assertThrows(IllegalStateException.class, () -> mocksControl.createMock(IMethods.class));
    }

    @Test
    public void createMockWithName() {
        assertThrows(IllegalStateException.class, () -> mocksControl.createMock("", IMethods.class));
    }

    @Test
    public void checkOrder() {
        assertThrows(IllegalStateException.class, () -> mocksControl.checkOrder(true));
    }

    @Test
    public void makeThreadSafe() {
        assertThrows(IllegalStateException.class, () -> mocksControl.makeThreadSafe(true));
    }

    @Test
    public void checkIsUsedInOneThread() {
        assertThrows(IllegalStateException.class, () -> mocksControl.checkIsUsedInOneThread(true));
    }

    @Test
    public void andStubReturn() {
        assertThrows(IllegalStateException.class, () -> expectationSetters.andStubReturn("7"));
    }

    @Test
    public void andStubThrow() {
        assertThrows(IllegalStateException.class, () -> expectationSetters.andStubThrow(new RuntimeException()));
    }

    @Test
    public void asStub() {
        assertThrows(IllegalStateException.class, () -> expectationSetters.asStub());
    }

    @Test
    public void times() {
        assertThrows(IllegalStateException.class, () -> expectationSetters.times(3));
    }

    @Test
    public void anyTimes() {
        assertThrows(IllegalStateException.class, () -> expectationSetters.anyTimes());
    }

    @Test
    public void manyMocks() {
        EasyMock.reset(mock);

        RuntimeException t = assertThrows(RuntimeException.class, () -> EasyMock.replay(mock, mock));
        assertEquals(IllegalStateException.class, t.getCause().getClass());
        assertEquals("On mock #1 (zero indexed): This method must not be called in replay state.", t.getMessage());
    }
}
