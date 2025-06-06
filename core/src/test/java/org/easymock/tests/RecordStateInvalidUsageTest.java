/*
 * Copyright 2001-2025 the original author or authors.
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        try {
            expect(null);
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            Assertions.assertEquals("no last call on a mock available", expected.getMessage());
        }
    }

    @Test
    void openVoidCallCountWithoutMethodCall() {
        try {
            expectLastCall();
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            Assertions.assertEquals("no last call on a mock available", expected.getMessage());
        }
    }

    @Test
    void setWrongReturnValueBoolean() {
        try {
            expect((Object) mock.oneArg(false)).andReturn(false);
            Assertions.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            Assertions.assertEquals("incompatible return value type", expected.getMessage());
        }
    }
}
