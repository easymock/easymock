/*
 * Copyright 2001-2009 OFFIS, Tammo Freese
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

import static org.junit.Assert.*;

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class UsageLongCompatibleReturnValueTest {
    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void returnByte() {
        mock.byteReturningMethod(0);
        control.setReturnValue(25);
        control.setDefaultReturnValue(34);

        control.replay();

        assertEquals((byte) 25, mock.byteReturningMethod(0));
        assertEquals((byte) 34, mock.byteReturningMethod(-4));
        assertEquals((byte) 34, mock.byteReturningMethod(12));

        control.verify();
    }

    @Test
    public void returnShort() {
        mock.shortReturningMethod(0);
        control.setReturnValue(25);
        control.setDefaultReturnValue(34);

        control.replay();

        assertEquals((short) 25, mock.shortReturningMethod(0));
        assertEquals((short) 34, mock.shortReturningMethod(-4));
        assertEquals((short) 34, mock.shortReturningMethod(12));

        control.verify();
    }

    @Test
    public void returnChar() {
        mock.charReturningMethod(0);
        control.setReturnValue(25);
        control.setDefaultReturnValue(34);

        control.replay();

        assertEquals((char) 25, mock.charReturningMethod(0));
        assertEquals((char) 34, mock.charReturningMethod(-4));
        assertEquals((char) 34, mock.charReturningMethod(12));

        control.verify();
    }

    @Test
    public void returnInt() {
        mock.intReturningMethod(0);
        control.setReturnValue(25);
        control.setDefaultReturnValue(34);

        control.replay();

        assertEquals(25, mock.intReturningMethod(0));
        assertEquals(34, mock.intReturningMethod(-4));
        assertEquals(34, mock.intReturningMethod(12));

        control.verify();
    }

    @Test
    public void returnLong() {
        mock.longReturningMethod(0);
        control.setReturnValue(25);
        control.setDefaultReturnValue(34);

        control.replay();

        assertEquals((long) 25, mock.longReturningMethod(0));
        assertEquals((long) 34, mock.longReturningMethod(-4));
        assertEquals((long) 34, mock.longReturningMethod(12));

        control.verify();
    }
}
