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

import static org.easymock.EasyMock.anyBoolean;
import static org.easymock.EasyMock.anyByte;
import static org.easymock.EasyMock.anyChar;
import static org.easymock.EasyMock.anyDouble;
import static org.easymock.EasyMock.anyFloat;
import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyShort;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author OFFIS, Tammo Freese
 */
class UsageOverloadedDefaultValueTest {

    private IMethods mock;

    @BeforeEach
    void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    void overloading() {

        expect(mock.oneArg(true)).andReturn("true");
        expect(mock.oneArg(anyBoolean())).andStubReturn("false");

        expect(mock.oneArg((byte) 0)).andReturn("byte 0");
        expect(mock.oneArg(anyByte())).andStubReturn("byte 1");

        expect(mock.oneArg((short) 0)).andReturn("short 0");
        expect(mock.oneArg(anyShort())).andStubReturn("short 1");

        expect(mock.oneArg((char) 0)).andReturn("char 0");
        expect(mock.oneArg(anyChar())).andStubReturn("char 1");

        expect(mock.oneArg(0)).andReturn("int 0");
        expect(mock.oneArg(anyInt())).andStubReturn("int 1");

        expect(mock.oneArg(0L)).andReturn("long 0");
        expect(mock.oneArg(anyLong())).andStubReturn("long 1");

        expect(mock.oneArg(0.0f)).andReturn("float 0");
        expect(mock.oneArg(anyFloat())).andStubReturn("float 1");

        expect(mock.oneArg(0.0)).andReturn("double 0");
        expect(mock.oneArg(anyDouble())).andStubReturn("double 1");

        expect(mock.oneArg("Object 0")).andReturn("String 0");
        expect(mock.oneArg(anyObject())).andStubReturn("String 1");

        replay(mock);

        assertEquals("true", mock.oneArg(true));
        assertEquals("false", mock.oneArg(false));

        assertEquals("byte 0", mock.oneArg((byte) 0));
        assertEquals("byte 1", mock.oneArg((byte) 1));

        assertEquals("short 0", mock.oneArg((short) 0));
        assertEquals("short 1", mock.oneArg((short) 1));

        assertEquals("char 0", mock.oneArg((char) 0));
        assertEquals("char 1", mock.oneArg((char) 1));

        assertEquals("int 0", mock.oneArg(0));
        assertEquals("int 1", mock.oneArg(1));

        assertEquals("long 0", mock.oneArg((long) 0));
        assertEquals("long 1", mock.oneArg((long) 1));

        assertEquals("float 0", mock.oneArg((float) 0.0));
        assertEquals("float 1", mock.oneArg((float) 1.0));

        assertEquals("double 0", mock.oneArg(0.0));
        assertEquals("double 1", mock.oneArg(1.0));

        assertEquals("String 0", mock.oneArg("Object 0"));
        assertEquals("String 1", mock.oneArg("Object 1"));

        verify(mock);
    }

    @Test
    void defaultThrowable() {

        RuntimeException expected = new RuntimeException();
        expect(mock.oneArg(anyObject())).andStubThrow(expected);

        replay(mock);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> mock.oneArg("Something else"));
        assertSame(expected, thrown);
    }
}
