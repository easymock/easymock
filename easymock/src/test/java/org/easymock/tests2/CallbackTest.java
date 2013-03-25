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
package org.easymock.tests2;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.IAnswer;
import org.easymock.tests.IMethods;
import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class CallbackTest {

    private IMethods mock;

    private static class Callback<T> implements IAnswer<T> {
        private int callCount;

        private final T result;

        public Callback(final T result) {
            this.result = result;
        }

        public void run() {
        }

        public int getCallCount() {
            return callCount;
        }

        public T answer() throws Throwable {
            callCount++;
            return result;
        }
    }

    @Before
    public void setUp() {
        mock = createStrictMock(IMethods.class);
    }

    @Test
    public void callback() {
        final Callback<String> c1 = new Callback<String>("1");
        final Callback<Object> c2 = new Callback<Object>(null);
        final Callback<Object> c3 = new Callback<Object>(null);

        expect(mock.oneArg("2")).andAnswer(c1).times(2);
        mock.simpleMethodWithArgument("One");
        expectLastCall().andAnswer(c2);
        mock.simpleMethodWithArgument("Two");
        expectLastCall().andAnswer(c3).times(2);

        replay(mock);

        mock.oneArg("2");
        mock.oneArg("2");
        try {
            mock.oneArg("2");
        } catch (final AssertionError ignored) {
        }
        try {
            mock.simpleMethodWithArgument("Two");
        } catch (final AssertionError ignored) {
        }
        mock.simpleMethodWithArgument("One");
        try {
            mock.simpleMethodWithArgument("One");
        } catch (final AssertionError ignored) {
        }
        mock.simpleMethodWithArgument("Two");
        mock.simpleMethodWithArgument("Two");
        try {
            mock.simpleMethodWithArgument("Two");
        } catch (final AssertionError ignored) {
        }
        verify(mock);

        assertEquals(2, c1.getCallCount());
        assertEquals(1, c2.getCallCount());
        assertEquals(2, c3.getCallCount());
    }
}
