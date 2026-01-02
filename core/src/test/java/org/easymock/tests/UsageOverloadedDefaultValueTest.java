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

import static org.easymock.EasyMock.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        Assertions.assertEquals("true", mock.oneArg(true));
        Assertions.assertEquals("false", mock.oneArg(false));

        Assertions.assertEquals("byte 0", mock.oneArg((byte) 0));
        Assertions.assertEquals("byte 1", mock.oneArg((byte) 1));

        Assertions.assertEquals("short 0", mock.oneArg((short) 0));
        Assertions.assertEquals("short 1", mock.oneArg((short) 1));

        Assertions.assertEquals("char 0", mock.oneArg((char) 0));
        Assertions.assertEquals("char 1", mock.oneArg((char) 1));

        Assertions.assertEquals("int 0", mock.oneArg(0));
        Assertions.assertEquals("int 1", mock.oneArg(1));

        Assertions.assertEquals("long 0", mock.oneArg((long) 0));
        Assertions.assertEquals("long 1", mock.oneArg((long) 1));

        Assertions.assertEquals("float 0", mock.oneArg((float) 0.0));
        Assertions.assertEquals("float 1", mock.oneArg((float) 1.0));

        Assertions.assertEquals("double 0", mock.oneArg(0.0));
        Assertions.assertEquals("double 1", mock.oneArg(1.0));

        Assertions.assertEquals("String 0", mock.oneArg("Object 0"));
        Assertions.assertEquals("String 1", mock.oneArg("Object 1"));

        verify(mock);
    }

    @Test
    void defaultThrowable() {

        RuntimeException expected = new RuntimeException();
        expect(mock.oneArg(anyObject())).andStubThrow(expected);

        replay(mock);

        try {
            mock.oneArg("Something else");
            Assertions.fail("runtime exception expected");
        } catch (RuntimeException expectedException) {
            Assertions.assertSame(expected, expectedException);
        }
    }
}
