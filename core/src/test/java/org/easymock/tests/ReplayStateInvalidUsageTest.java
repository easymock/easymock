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

import org.easymock.EasyMock;
import org.easymock.IExpectationSetters;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * @author OFFIS, Tammo Freese
 */
class ReplayStateInvalidUsageTest {

    private IMethods mock;

    private IMocksControl mocksControl;

    private IExpectationSetters<String> expectationSetters;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        mock = EasyMock.createMock(IMethods.class);
        EasyMock.replay(mock);
        mocksControl = EasyMock.createControl();
        mocksControl.replay();
        expectationSetters = (IExpectationSetters<String>) mocksControl;
    }

    @Test
    void setVoidCallable() {
        assertThrows(IllegalStateException.class, EasyMock::expectLastCall);
    }

    @Test
    void replay() {
        assertThrows(IllegalStateException.class, () -> EasyMock.replay(mock));
    }

    @Test
    void replaySameMethod() {
        EasyMock.reset(mock);

        RuntimeException t = assertThrows(RuntimeException.class, () -> EasyMock.replay(mock, mock));
        Assertions.assertEquals(IllegalStateException.class, t.getCause().getClass());
        Assertions.assertEquals("On mock #1 (zero indexed): This method must not be called in replay state.", t.getMessage());
    }

    @Test
    void createMock() {
        assertThrows(IllegalStateException.class, () -> mocksControl.createMock(IMethods.class));
    }

    @Test
    void createMockWithName() {
        assertThrows(IllegalStateException.class, () -> mocksControl.createMock("", IMethods.class));
    }

    @Test
    void checkOrder() {
        assertThrows(IllegalStateException.class, () -> mocksControl.checkOrder(true));
    }

    @Test
    void makeThreadSafe() {
        assertThrows(IllegalStateException.class, () -> mocksControl.makeThreadSafe(true));
    }

    @Test
    void checkIsUsedInOneThread() {
        assertThrows(IllegalStateException.class, () -> mocksControl.checkIsUsedInOneThread(true));
    }

    @Test
    void andStubReturn() {
        assertThrows(IllegalStateException.class, () -> expectationSetters.andStubReturn("7"));
    }

    @Test
    void andStubThrow() {
        assertThrows(IllegalStateException.class, () -> expectationSetters.andStubThrow(new RuntimeException()));
    }

    @Test
    void asStub() {
        assertThrows(IllegalStateException.class, () -> expectationSetters.asStub());
    }

    @Test
    void times() {
        assertThrows(IllegalStateException.class, () -> expectationSetters.times(3));
    }

    @Test
    void anyTimes() {
        assertThrows(IllegalStateException.class, () -> expectationSetters.anyTimes());
    }

    @Test
    void manyMocks() {
        EasyMock.reset(mock);

        RuntimeException t = assertThrows(RuntimeException.class, () -> EasyMock.replay(mock, mock));
        Assertions.assertEquals(IllegalStateException.class, t.getCause().getClass());
        Assertions.assertEquals("On mock #1 (zero indexed): This method must not be called in replay state.", t.getMessage());
    }
}
