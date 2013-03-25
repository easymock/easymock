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
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class RecordStateInvalidRangeTest {

    private IMethods mock;

    @Before
    public void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    public void setOpenCallCountTwice() {
        mock.simpleMethod();
        try {
            expectLastCall().atLeastOnce().atLeastOnce();
            fail();
        } catch (final IllegalStateException expected) {
            assertEquals("last method called on mock already has a non-fixed count set.", expected
                    .getMessage());
        }
    }

    @Test
    public void setCloseCallAfterOpenOne() {
        mock.simpleMethod();
        try {
            expectLastCall().atLeastOnce().once();
            fail();
        } catch (final IllegalStateException expected) {
            assertEquals("last method called on mock already has a non-fixed count set.", expected
                    .getMessage());
        }
    }

    @Test
    public void setIllegalMinimumCount() {
        mock.simpleMethod();
        final int NEGATIVE = -1;
        try {
            expectLastCall().times(NEGATIVE, 2);
            fail();
        } catch (final IllegalArgumentException expected) {
            assertEquals("minimum must be >= 0", expected.getMessage());
        }
    }

    @Test
    public void setIllegalMaximumCount() {
        mock.simpleMethod();
        final int NON_POSITIVE = 0;
        try {
            expectLastCall().times(0, NON_POSITIVE);
            fail();
        } catch (final IllegalArgumentException expected) {
            assertEquals("maximum must be >= 1", expected.getMessage());
        }
    }

    @Test
    public void setMinimumBiggerThanMaximum() {
        mock.simpleMethod();
        try {
            expectLastCall().times(4, 3);
            fail();
        } catch (final IllegalArgumentException expected) {
            assertEquals("minimum must be <= maximum", expected.getMessage());
        }
    }
}