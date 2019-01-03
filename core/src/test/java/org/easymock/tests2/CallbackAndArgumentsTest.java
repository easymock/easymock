/*
 * Copyright 2001-2019 the original author or authors.
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

import org.easymock.tests.IMethods;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author OFFIS, Tammo Freese
 */
public class CallbackAndArgumentsTest {

    private IMethods mock;

    @Before
    public void setUp() {
        mock = createStrictMock(IMethods.class);
    }

    @Test
    public void callbackGetsArguments() {

        final StringBuilder buffer = new StringBuilder();

        mock.simpleMethodWithArgument(notNull());
        expectLastCall().andAnswer(() -> {
            buffer.append((String) getCurrentArguments()[0]);
            return null;
        }).times(2);

        replay(mock);

        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");

        verify(mock);

        assertEquals("12", buffer.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void currentArgumentsFailsOutsideCallbacks() {
        getCurrentArguments();
    }

    @Test
    public void callbackGetsArgumentsEvenIfAMockCallsAnother() {

        final StringBuilder buffer = new StringBuilder();

        final IMethods mock2 = createStrictMock(IMethods.class);
        mock2.simpleMethod();
        expectLastCall().andAnswer(() -> {
            // empty, only needed to force deletion of arguments
            return null;
        }).times(2);

        mock.simpleMethodWithArgument(notNull());
        expectLastCall().andAnswer(() -> {
            mock2.simpleMethod();
            buffer.append((String) getCurrentArguments()[0]);
            return null;
        }).times(2);

        replay(mock);
        replay(mock2);

        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");

        verify(mock);
        verify(mock2);

        assertEquals("12", buffer.toString());
    }
}
