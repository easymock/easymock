/*
 * Copyright 2001-2018 the original author or authors.
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
package org.easymock.tests2;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.tests.IMethods;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class UsageMatchersTest {
    @Test(expected = IllegalStateException.class)
    public void additionalMatchersFailAtReplay() {

        IMethods mock = mock(IMethods.class);
        lt(5);

        replay(mock);
    }

    @Test
    public void arraysArgumentsAreMatchedWithAryEq() {

        IMethods mock = mock(IMethods.class);
        expect(mock.oneArray(new int[] { 1 })).andReturn("test");

        replay(mock);

        assertEquals("should use ArrayEquals to compare", "test", mock.oneArray(new int[] { 1 }));

        verify(mock);
    }

}
