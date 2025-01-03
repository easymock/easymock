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
public class UsageDefaultReturnValueTest {

    private IMethods mock;

    @BeforeEach
    public void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    public void defaultReturnValue() {
        expect(mock.threeArgumentMethod(7, "", "test")).andReturn("test");

        expect(mock.threeArgumentMethod(8, null, "test2")).andReturn("test2");

        Object defaultValue = new Object();
        expect(mock.threeArgumentMethod(anyInt(), anyObject(), anyObject())).andStubReturn(
                defaultValue);

        replay(mock);
        Assertions.assertEquals("test", mock.threeArgumentMethod(7, "", "test"));
        Assertions.assertEquals("test2", mock.threeArgumentMethod(8, null, "test2"));
        Assertions.assertSame(defaultValue, mock.threeArgumentMethod(7, new Object(), "test"));
        Assertions.assertSame(defaultValue, mock.threeArgumentMethod(7, "", "test"));
        Assertions.assertSame(defaultValue, mock.threeArgumentMethod(8, null, "test"));
        Assertions.assertSame(defaultValue, mock.threeArgumentMethod(9, null, "test"));

        verify(mock);
    }

    @Test
    public void defaultVoidCallable() {
        mock.twoArgumentMethod(anyInt(), anyInt());
        expectLastCall().asStub();

        mock.twoArgumentMethod(1, 1);
        RuntimeException expected = new RuntimeException();
        expectLastCall().andThrow(expected);

        replay(mock);
        mock.twoArgumentMethod(2, 1);
        mock.twoArgumentMethod(1, 2);
        mock.twoArgumentMethod(3, 7);

        try {
            mock.twoArgumentMethod(1, 1);
            Assertions.fail("RuntimeException expected");
        } catch (RuntimeException actual) {
            Assertions.assertSame(expected, actual);
        }
    }

    @Test
    public void defaultThrowable() {
        mock.twoArgumentMethod(1, 2);
        expectLastCall();
        mock.twoArgumentMethod(1, 1);
        expectLastCall();

        RuntimeException expected = new RuntimeException();
        mock.twoArgumentMethod(anyInt(), anyInt());
        expectLastCall().andStubThrow(expected);

        replay(mock);

        mock.twoArgumentMethod(1, 2);
        mock.twoArgumentMethod(1, 1);
        try {
            mock.twoArgumentMethod(2, 1);
            Assertions.fail("RuntimeException expected");
        } catch (RuntimeException actual) {
            Assertions.assertSame(expected, actual);
        }
    }

    @Test
    public void defaultReturnValueBoolean() {
        expect(mock.booleanReturningMethod(12)).andReturn(true);
        expect(mock.booleanReturningMethod(anyInt())).andStubReturn(false);

        replay(mock);

        Assertions.assertFalse(mock.booleanReturningMethod(11));
        Assertions.assertTrue(mock.booleanReturningMethod(12));
        Assertions.assertFalse(mock.booleanReturningMethod(13));

        verify(mock);
    }

    @Test
    public void returnValueAndDefaultReturnValue() {

        expect(mock.oneArg("")).andReturn("1");
        expect(mock.oneArg(anyObject())).andStubReturn("2");

        replay(mock);

        Assertions.assertEquals("1", mock.oneArg(""));
        Assertions.assertEquals("2", mock.oneArg(""));
        Assertions.assertEquals("2", mock.oneArg("X"));

        verify(mock);
    }
}
