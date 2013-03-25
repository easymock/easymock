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

import org.easymock.tests.IMethods;
import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class StubTest {
    private IMethods mock;

    @Before
    public void setup() {
        mock = createStrictMock(IMethods.class);
    }

    @Test
    public void stub() {
        mock.simpleMethodWithArgument("1");
        expectLastCall().anyTimes();
        mock.simpleMethodWithArgument("2");
        expectLastCall().anyTimes();
        mock.simpleMethodWithArgument("3");
        expectLastCall().asStub();

        replay(mock);

        mock.simpleMethodWithArgument("3");
        mock.simpleMethodWithArgument("3");
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");
        mock.simpleMethodWithArgument("3");
        mock.simpleMethodWithArgument("3");

        verify(mock);

    }

    @Test
    public void stubWithReturnValue() {
        expect(mock.oneArg("1")).andReturn("A").andStubReturn("B");
        expect(mock.oneArg("2")).andThrow(new IllegalArgumentException()).andStubThrow(
                new IllegalStateException());

        replay(mock);

        assertEquals("A", mock.oneArg("1"));
        assertEquals("B", mock.oneArg("1"));
        assertEquals("B", mock.oneArg("1"));
        try {
            mock.oneArg("2");
        } catch (final IllegalArgumentException ignored) {
        }
        assertEquals("B", mock.oneArg("1"));
        try {
            mock.oneArg("2");
        } catch (final IllegalStateException ignored) {
        }
        assertEquals("B", mock.oneArg("1"));
        try {
            mock.oneArg("2");
        } catch (final IllegalStateException ignored) {
        }
        verify(mock);
    }

}
