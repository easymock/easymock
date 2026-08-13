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
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author OFFIS, Tammo Freese
 */
class RecordStateInvalidRangeTest {

    private IMethods mock;

    @BeforeEach
    void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    void setOpenCallCountTwice() {
        mock.simpleMethod();
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> expectLastCall().atLeastOnce().atLeastOnce());
        assertEquals("last method called on mock already has a non-fixed count set.", expected
                .getMessage());
    }

    @Test
    void setCloseCallAfterOpenOne() {
        mock.simpleMethod();
        IllegalStateException expected = assertThrows(IllegalStateException.class, () -> expectLastCall().atLeastOnce().once());
        assertEquals("last method called on mock already has a non-fixed count set.", expected
                .getMessage());
    }

    @Test
    void setIllegalMinimumCount() {
        mock.simpleMethod();
        int NEGATIVE = -1;
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> expectLastCall().times(NEGATIVE, 2));
        assertEquals("minimum must be >= 0", expected.getMessage());
    }

    @Test
    void setIllegalMaximumCount() {
        mock.simpleMethod();
        int NON_POSITIVE = 0;
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> expectLastCall().times(0, NON_POSITIVE));
        assertEquals("maximum must be >= 1", expected.getMessage());
    }

    @Test
    void setMinimumBiggerThanMaximum() {
        mock.simpleMethod();
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> expectLastCall().times(4, 3));
        assertEquals("minimum must be <= maximum", expected.getMessage());
    }
}
