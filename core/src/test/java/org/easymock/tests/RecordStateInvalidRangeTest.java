/*
 * Copyright 2001-2024 the original author or authors.
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
public class RecordStateInvalidRangeTest {

    private IMethods mock;

    @BeforeEach
    public void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    public void setOpenCallCountTwice() {
        mock.simpleMethod();
        try {
            expectLastCall().atLeastOnce().atLeastOnce();
            Assertions.fail();
        } catch (IllegalStateException expected) {
            Assertions.assertEquals("last method called on mock already has a non-fixed count set.", expected
                    .getMessage());
        }
    }

    @Test
    public void setCloseCallAfterOpenOne() {
        mock.simpleMethod();
        try {
            expectLastCall().atLeastOnce().once();
            Assertions.fail();
        } catch (IllegalStateException expected) {
            Assertions.assertEquals("last method called on mock already has a non-fixed count set.", expected
                    .getMessage());
        }
    }

    @Test
    public void setIllegalMinimumCount() {
        mock.simpleMethod();
        int NEGATIVE = -1;
        try {
            expectLastCall().times(NEGATIVE, 2);
            Assertions.fail();
        } catch (IllegalArgumentException expected) {
            Assertions.assertEquals("minimum must be >= 0", expected.getMessage());
        }
    }

    @Test
    public void setIllegalMaximumCount() {
        mock.simpleMethod();
        int NON_POSITIVE = 0;
        try {
            expectLastCall().times(0, NON_POSITIVE);
            Assertions.fail();
        } catch (IllegalArgumentException expected) {
            Assertions.assertEquals("maximum must be >= 1", expected.getMessage());
        }
    }

    @Test
    public void setMinimumBiggerThanMaximum() {
        mock.simpleMethod();
        try {
            expectLastCall().times(4, 3);
            Assertions.fail();
        } catch (IllegalArgumentException expected) {
            Assertions.assertEquals("minimum must be <= maximum", expected.getMessage());
        }
    }
}
