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

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.internal.RecordState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author OFFIS, Tammo Freese
 */
class RecordStateInvalidStateChangeTest {

    private IMethods mock;

    @BeforeEach
    void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    void activateWithoutReturnValue() {
        expect(mock.oneArg(false));
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> replay(mock));
        assertEquals("missing behavior definition for the preceding method call:\nEasyMock for interface org.easymock.tests.IMethods -> IMethods.oneArg(false)"
                    + "\nUsage is: expect(a.foo()).andXXX()", e.getMessage());
        assertEquals(Util.getStackTrace(e).indexOf(RecordState.class.getName()), -1, "stack trace must be cut");
    }

    @Test
    void secondCallWithoutReturnValue() {
        mock.oneArg(false);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> mock.oneArg(false));
        assertEquals("missing behavior definition for the preceding method call:\nEasyMock for interface org.easymock.tests.IMethods -> IMethods.oneArg(false)"
                    + "\nUsage is: expect(a.foo()).andXXX()", e.getMessage());
        assertEquals(Util.getStackTrace(e).indexOf(RecordState.class.getName()), -1, "stack trace must be cut");
    }

    @Test
    void verifyWithoutActivation() {
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> verify(mock));
        assertEquals("calling verify is not allowed in record state", e.getMessage());
    }
}
