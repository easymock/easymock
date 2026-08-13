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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author OFFIS, Tammo Freese
 */
class UsageOverloadedMethodTest {

    private IMethods mock;

    @BeforeEach
    void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    void overloading() {

        expect(mock.oneArg(true)).andReturn("true");
        expect(mock.oneArg(false)).andReturn("false");

        expect(mock.oneArg((byte) 0)).andReturn("byte 0");
        expect(mock.oneArg((byte) 1)).andReturn("byte 1");

        expect(mock.oneArg((short) 0)).andReturn("short 0");
        expect(mock.oneArg((short) 1)).andReturn("short 1");

        expect(mock.oneArg((char) 0)).andReturn("char 0");
        expect(mock.oneArg((char) 1)).andReturn("char 1");

        expect(mock.oneArg(0)).andReturn("int 0");
        expect(mock.oneArg(1)).andReturn("int 1");

        expect(mock.oneArg((long) 0)).andReturn("long 0");
        expect(mock.oneArg((long) 1)).andReturn("long 1");

        expect(mock.oneArg((float) 0)).andReturn("float 0");
        expect(mock.oneArg((float) 1)).andReturn("float 1");

        expect(mock.oneArg(0.0)).andReturn("double 0");
        expect(mock.oneArg(1.0)).andReturn("double 1");

        expect(mock.oneArg("Object 0")).andReturn("1");
        expect(mock.oneArg("Object 1")).andReturn("2");

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

        assertEquals("double 1", mock.oneArg(1.0));
        assertEquals("double 0", mock.oneArg(0.0));

        assertEquals("1", mock.oneArg("Object 0"));
        assertEquals("2", mock.oneArg("Object 1"));

        verify(mock);
    }

    @Test
    void nullReturnValue() {

        expect(mock.oneArg("Object")).andReturn(null);

        replay(mock);

        assertNull(mock.oneArg("Object"));

    }

    @Test
    void moreThanOneResultAndOpenCallCount() {
        expect(mock.oneArg(true)).andReturn("First Result").times(4).andReturn("Second Result").times(2)
                .andThrow(new RuntimeException("Third Result")).times(3).andReturn("Following Result")
                .atLeastOnce();

        replay(mock);

        assertEquals("First Result", mock.oneArg(true));
        assertEquals("First Result", mock.oneArg(true));
        assertEquals("First Result", mock.oneArg(true));
        assertEquals("First Result", mock.oneArg(true));

        assertEquals("Second Result", mock.oneArg(true));
        assertEquals("Second Result", mock.oneArg(true));

        RuntimeException expected1 = assertThrows(RuntimeException.class, () -> mock.oneArg(true));
        assertEquals("Third Result", expected1.getMessage());

        RuntimeException expected2 = assertThrows(RuntimeException.class, () -> mock.oneArg(true));
        assertEquals("Third Result", expected2.getMessage());

        RuntimeException expected3 = assertThrows(RuntimeException.class, () -> mock.oneArg(true));
        assertEquals("Third Result", expected3.getMessage());

        assertEquals("Following Result", mock.oneArg(true));
        assertEquals("Following Result", mock.oneArg(true));
        assertEquals("Following Result", mock.oneArg(true));
        assertEquals("Following Result", mock.oneArg(true));
        assertEquals("Following Result", mock.oneArg(true));

        verify(mock);
    }
}
