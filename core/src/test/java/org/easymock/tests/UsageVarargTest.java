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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Proxy;

/**
 * @author OFFIS, Tammo Freese
 */
class UsageVarargTest {

    private IVarArgs mock;

    @BeforeEach
    void setup() {
        mock = createStrictMock(IVarArgs.class);
    }

    @Test
    void varargObjectAccepted() {
        mock.withVarargsString(1, "1");
        mock.withVarargsString(2, "1", "2");
        mock.withVarargsString(2, "1", "2");
        mock.withVarargsObject(3, "1");
        mock.withVarargsObject(4, "1", "2");

        replay(mock);
        mock.withVarargsString(1, "1");
        mock.withVarargsString(2, "1", "2");
        mock.withVarargsString(2, "1", "2");
        mock.withVarargsObject(3, "1");
        mock.withVarargsObject(4, "1", "2");
        verify(mock);
    }

    @Test
    void varargBooleanAccepted() {
        mock.withVarargsBoolean(1, true);
        mock.withVarargsBoolean(2, true, false);

        replay(mock);
        mock.withVarargsBoolean(1, true);
        mock.withVarargsBoolean(2, true, false);
        verify(mock);
    }

    @Test
    void varargByteAccepted() {
        mock.withVarargsByte(1, (byte) 1);
        mock.withVarargsByte(2, (byte) 1, (byte) 2);

        replay(mock);
        mock.withVarargsByte(1, (byte) 1);
        mock.withVarargsByte(2, (byte) 1, (byte) 2);
        verify(mock);
    }

    @Test
    void varargCharAccepted() {
        mock.withVarargsChar(1, 'a');
        mock.withVarargsChar(1, 'a', 'b');

        replay(mock);
        mock.withVarargsChar(1, 'a');
        mock.withVarargsChar(1, 'a', 'b');
        verify(mock);
    }

    @Test
    void varargDoubleAccepted() {
        mock.withVarargsDouble(1, 1.0d);
        mock.withVarargsDouble(1, 1.0d, 2.0d);

        replay(mock);
        mock.withVarargsDouble(1, 1.0d);
        mock.withVarargsDouble(1, 1.0d, 2.0d);
        verify(mock);
    }

    @Test
    void varargFloatAccepted() {
        mock.withVarargsFloat(1, 1.0f);
        mock.withVarargsFloat(1, 1.0f, 2.0f);

        replay(mock);
        mock.withVarargsFloat(1, 1.0f);
        mock.withVarargsFloat(1, 1.0f, 2.0f);
        verify(mock);
    }

    @Test
    void varargIntAccepted() {
        mock.withVarargsInt(1, 1);
        mock.withVarargsInt(1, 1, 2);

        replay(mock);
        mock.withVarargsInt(1, 1);
        mock.withVarargsInt(1, 1, 2);
        verify(mock);
    }

    @Test
    void varargLongAccepted() {
        mock.withVarargsLong(1, 1L);
        mock.withVarargsLong(1, 1, 2);

        replay(mock);
        mock.withVarargsLong(1, 1L);
        mock.withVarargsLong(1, 1, 2);
        verify(mock);
    }

    @Test
    void varargShortAccepted() {
        mock.withVarargsShort(1, (short) 1);
        mock.withVarargsShort(1, (short) 1, (short) 2);

        replay(mock);
        mock.withVarargsShort(1, (short) 1);
        mock.withVarargsShort(1, (short) 1, (short) 2);
        verify(mock);
    }

    @Test
    void varargAcceptedIfArrayIsGiven() {
        IVarArgs object = (IVarArgs) Proxy.newProxyInstance(Thread.currentThread()
                .getContextClassLoader(), new Class[] { IVarArgs.class }, (proxy, method, args) -> null);
        object.withVarargsObject(1);
        object.withVarargsObject(1, (Object) null);
        object.withVarargsObject(1, (Object[]) null);
        object.withVarargsObject(1);
        object.withVarargsObject(1, false);
        object.withVarargsObject(1, new boolean[] { true, false });
    }

    /**
     * Make sure we can validate any kind of varargs call
     */
    @Test
    void allKinds() {
        mock.withVarargsObject(eq(1), aryEq((Object[]) null));
        mock.withVarargsObject(eq(1), isNull());
        mock.withVarargsObject(1, "a", "b");
        mock.withVarargsObject(1, "a", "b");
        mock.withVarargsObject(eq(1), aryEq(new Object[] { "a", "b" }));
        mock.withVarargsObject(1);
        replay(mock);
        mock.withVarargsObject(1, (Object[]) null);
        mock.withVarargsObject(1, (Object) null);
        mock.withVarargsObject(1, "a", "b");
        mock.withVarargsObject(1, "a", "b");
        mock.withVarargsObject(1, (Object) new Object[] { "a", "b" });
        mock.withVarargsObject(1);
        verify(mock);
    }

    @Test
    void differentLength() {
        mock.withVarargsInt(1, 2, 3);
        replay(mock);
        try {
            mock.withVarargsInt(1, 2);
            fail("not the same number of params");
        } catch (AssertionError e) {

        }
    }
}
