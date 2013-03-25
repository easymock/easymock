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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class UsageVarargTest {

    private IVarArgs mock;

    @Before
    public void setup() {
        mock = createStrictMock(IVarArgs.class);
    }

    @Test
    public void varargObjectAccepted() {
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
    public void varargBooleanAccepted() {
        mock.withVarargsBoolean(1, true);
        mock.withVarargsBoolean(2, true, false);

        replay(mock);
        mock.withVarargsBoolean(1, true);
        mock.withVarargsBoolean(2, true, false);
        verify(mock);
    }

    @Test
    public void varargByteAccepted() {
        mock.withVarargsByte(1, (byte) 1);
        mock.withVarargsByte(2, (byte) 1, (byte) 2);

        replay(mock);
        mock.withVarargsByte(1, (byte) 1);
        mock.withVarargsByte(2, (byte) 1, (byte) 2);
        verify(mock);
    }

    @Test
    public void varargCharAccepted() {
        mock.withVarargsChar(1, 'a');
        mock.withVarargsChar(1, 'a', 'b');

        replay(mock);
        mock.withVarargsChar(1, 'a');
        mock.withVarargsChar(1, 'a', 'b');
        verify(mock);
    }

    @Test
    public void varargDoubleAccepted() {
        mock.withVarargsDouble(1, 1.0d);
        mock.withVarargsDouble(1, 1.0d, 2.0d);

        replay(mock);
        mock.withVarargsDouble(1, 1.0d);
        mock.withVarargsDouble(1, 1.0d, 2.0d);
        verify(mock);
    }

    @Test
    public void varargFloatAccepted() {
        mock.withVarargsFloat(1, 1.0f);
        mock.withVarargsFloat(1, 1.0f, 2.0f);

        replay(mock);
        mock.withVarargsFloat(1, 1.0f);
        mock.withVarargsFloat(1, 1.0f, 2.0f);
        verify(mock);
    }

    @Test
    public void varargIntAccepted() {
        mock.withVarargsInt(1, 1);
        mock.withVarargsInt(1, 1, 2);

        replay(mock);
        mock.withVarargsInt(1, 1);
        mock.withVarargsInt(1, 1, 2);
        verify(mock);
    }

    @Test
    public void varargLongAccepted() {
        mock.withVarargsLong(1, (long) 1);
        mock.withVarargsLong(1, 1, 2);

        replay(mock);
        mock.withVarargsLong(1, (long) 1);
        mock.withVarargsLong(1, 1, 2);
        verify(mock);
    }

    @Test
    public void varargShortAccepted() {
        mock.withVarargsShort(1, (short) 1);
        mock.withVarargsShort(1, (short) 1, (short) 2);

        replay(mock);
        mock.withVarargsShort(1, (short) 1);
        mock.withVarargsShort(1, (short) 1, (short) 2);
        verify(mock);
    }

    @Test
    public void varargAcceptedIfArrayIsGiven() {
        final IVarArgs object = (IVarArgs) Proxy.newProxyInstance(Thread.currentThread()
                .getContextClassLoader(), new Class[] { IVarArgs.class }, new InvocationHandler() {

            public Object invoke(final Object proxy, final Method method, final Object[] args)
                    throws Throwable {
                return null;
            }
        });
        object.withVarargsObject(1);
        object.withVarargsObject(1, (Object) null);
        object.withVarargsObject(1, (Object[]) null);
        object.withVarargsObject(1, new Object[0]);
        object.withVarargsObject(1, false);
        object.withVarargsObject(1, new boolean[] { true, false });
    }

    /**
     * Make sure we can validate any kind of varargs call
     */
    @Test
    public void allKinds() {
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
        mock.withVarargsObject(1, new Object[] { "a", "b" });
        mock.withVarargsObject(1, (Object) new Object[] { "a", "b" });
        mock.withVarargsObject(1);
        verify(mock);
    }

    @Test
    public void differentLength() {
        mock.withVarargsInt(1, 2, 3);
        replay(mock);
        try {
            mock.withVarargsInt(1, 2);
            fail("not the same number of params");
        } catch (final AssertionError e) {

        }
    }
}