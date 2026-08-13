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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author OFFIS, Tammo Freese
 */
class RecordStateInvalidUsageTest {

    private IMethods mock;

    @BeforeEach
    void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    void notAMockPassedToExpect() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> expect(null));
        assertEquals("no last call on a mock available", expected.getMessage());
    }

    @Test
    void openVoidCallCountWithoutMethodCall() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> expectLastCall());
        assertEquals("no last call on a mock available", expected.getMessage());
    }

    @Test
    void setWrongReturnValueBoolean() {
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> expect((Object) mock.oneArg(false)).andReturn(false));
        assertEquals("incompatible return value type", expected.getMessage());
    }
}
