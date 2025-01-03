/*
 * Copyright 2001-2025 the original author or authors.
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
public class UsageOverloadedMethodTest {

    private IMethods mock;

    @BeforeEach
    public void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    public void overloading() {

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

        Assertions.assertEquals("double 1", mock.oneArg(1.0));
        Assertions.assertEquals("double 0", mock.oneArg(0.0));

        Assertions.assertEquals("1", mock.oneArg("Object 0"));
        Assertions.assertEquals("2", mock.oneArg("Object 1"));

        verify(mock);
    }

    @Test
    public void nullReturnValue() {

        expect(mock.oneArg("Object")).andReturn(null);

        replay(mock);

        Assertions.assertNull(mock.oneArg("Object"));

    }

    @Test
    public void moreThanOneResultAndOpenCallCount() {
        expect(mock.oneArg(true)).andReturn("First Result").times(4).andReturn("Second Result").times(2)
                .andThrow(new RuntimeException("Third Result")).times(3).andReturn("Following Result")
                .atLeastOnce();

        replay(mock);

        Assertions.assertEquals("First Result", mock.oneArg(true));
        Assertions.assertEquals("First Result", mock.oneArg(true));
        Assertions.assertEquals("First Result", mock.oneArg(true));
        Assertions.assertEquals("First Result", mock.oneArg(true));

        Assertions.assertEquals("Second Result", mock.oneArg(true));
        Assertions.assertEquals("Second Result", mock.oneArg(true));

        try {
            mock.oneArg(true);
            Assertions.fail("expected exception");
        } catch (RuntimeException expected) {
            Assertions.assertEquals("Third Result", expected.getMessage());
        }

        try {
            mock.oneArg(true);
            Assertions.fail("expected exception");
        } catch (RuntimeException expected) {
            Assertions.assertEquals("Third Result", expected.getMessage());
        }

        try {
            mock.oneArg(true);
            Assertions.fail("expected exception");
        } catch (RuntimeException expected) {
            Assertions.assertEquals("Third Result", expected.getMessage());
        }

        Assertions.assertEquals("Following Result", mock.oneArg(true));
        Assertions.assertEquals("Following Result", mock.oneArg(true));
        Assertions.assertEquals("Following Result", mock.oneArg(true));
        Assertions.assertEquals("Following Result", mock.oneArg(true));
        Assertions.assertEquals("Following Result", mock.oneArg(true));

        verify(mock);
    }
}
