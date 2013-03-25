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
public class UsageLongCompatibleReturnValueTest {

    private IMethods mock;

    @Before
    public void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    public void returnByte() {
        expect(mock.byteReturningMethod(0)).andReturn((byte) 25);
        expect(mock.byteReturningMethod(anyInt())).andStubReturn((byte) 34);

        replay(mock);

        assertEquals((byte) 25, mock.byteReturningMethod(0));
        assertEquals((byte) 34, mock.byteReturningMethod(-4));
        assertEquals((byte) 34, mock.byteReturningMethod(12));

        verify(mock);
    }

    @Test
    public void returnShort() {
        expect(mock.shortReturningMethod(0)).andReturn((short) 25);
        expect(mock.shortReturningMethod(anyInt())).andStubReturn((short) 34);

        replay(mock);

        assertEquals((short) 25, mock.shortReturningMethod(0));
        assertEquals((short) 34, mock.shortReturningMethod(-4));
        assertEquals((short) 34, mock.shortReturningMethod(12));

        verify(mock);
    }

    @Test
    public void returnChar() {
        expect(mock.charReturningMethod(0)).andReturn((char) 25);
        expect(mock.charReturningMethod(anyInt())).andStubReturn((char) 34);

        replay(mock);

        assertEquals((char) 25, mock.charReturningMethod(0));
        assertEquals((char) 34, mock.charReturningMethod(-4));
        assertEquals((char) 34, mock.charReturningMethod(12));

        verify(mock);
    }

    @Test
    public void returnInt() {
        expect(mock.intReturningMethod(0)).andReturn(25);
        expect(mock.intReturningMethod(anyInt())).andStubReturn(34);

        replay(mock);

        assertEquals(25, mock.intReturningMethod(0));
        assertEquals(34, mock.intReturningMethod(-4));
        assertEquals(34, mock.intReturningMethod(12));

        verify(mock);
    }

    @Test
    public void returnLong() {
        expect(mock.longReturningMethod(0)).andReturn(25L);
        expect(mock.longReturningMethod(anyInt())).andStubReturn(34L);

        replay(mock);

        assertEquals(25, mock.longReturningMethod(0));
        assertEquals(34, mock.longReturningMethod(-4));
        assertEquals(34, mock.longReturningMethod(12));

        verify(mock);
    }
}
