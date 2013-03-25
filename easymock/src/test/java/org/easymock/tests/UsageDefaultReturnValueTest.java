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
public class UsageDefaultReturnValueTest {

    private IMethods mock;

    @Before
    public void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    public void defaultReturnValue() {
        expect(mock.threeArgumentMethod(7, "", "test")).andReturn("test");

        expect(mock.threeArgumentMethod(8, null, "test2")).andReturn("test2");

        final Object defaultValue = new Object();
        expect(mock.threeArgumentMethod(anyInt(), anyObject(), (String) anyObject())).andStubReturn(
                defaultValue);

        replay(mock);
        assertEquals("test", mock.threeArgumentMethod(7, "", "test"));
        assertEquals("test2", mock.threeArgumentMethod(8, null, "test2"));
        assertSame(defaultValue, mock.threeArgumentMethod(7, new Object(), "test"));
        assertSame(defaultValue, mock.threeArgumentMethod(7, "", "test"));
        assertSame(defaultValue, mock.threeArgumentMethod(8, null, "test"));
        assertSame(defaultValue, mock.threeArgumentMethod(9, null, "test"));

        verify(mock);
    }

    @Test
    public void defaultVoidCallable() {
        mock.twoArgumentMethod(anyInt(), anyInt());
        expectLastCall().asStub();

        mock.twoArgumentMethod(1, 1);
        final RuntimeException expected = new RuntimeException();
        expectLastCall().andThrow(expected);

        replay(mock);
        mock.twoArgumentMethod(2, 1);
        mock.twoArgumentMethod(1, 2);
        mock.twoArgumentMethod(3, 7);

        try {
            mock.twoArgumentMethod(1, 1);
            fail("RuntimeException expected");
        } catch (final RuntimeException actual) {
            assertSame(expected, actual);
        }
    }

    @Test
    public void defaultThrowable() {
        mock.twoArgumentMethod(1, 2);
        expectLastCall();
        mock.twoArgumentMethod(1, 1);
        expectLastCall();

        final RuntimeException expected = new RuntimeException();
        mock.twoArgumentMethod(anyInt(), anyInt());
        expectLastCall().andStubThrow(expected);

        replay(mock);

        mock.twoArgumentMethod(1, 2);
        mock.twoArgumentMethod(1, 1);
        try {
            mock.twoArgumentMethod(2, 1);
            fail("RuntimeException expected");
        } catch (final RuntimeException actual) {
            assertSame(expected, actual);
        }
    }

    @Test
    public void defaultReturnValueBoolean() {
        expect(mock.booleanReturningMethod(12)).andReturn(true);
        expect(mock.booleanReturningMethod(anyInt())).andStubReturn(false);

        replay(mock);

        assertFalse(mock.booleanReturningMethod(11));
        assertTrue(mock.booleanReturningMethod(12));
        assertFalse(mock.booleanReturningMethod(13));

        verify(mock);
    }

    @Test
    public void returnValueAndDefaultReturnValue() throws Exception {

        expect(mock.oneArg("")).andReturn("1");
        expect(mock.oneArg((String) anyObject())).andStubReturn("2");

        replay(mock);

        assertEquals("1", mock.oneArg(""));
        assertEquals("2", mock.oneArg(""));
        assertEquals("2", mock.oneArg("X"));

        verify(mock);
    }
}
