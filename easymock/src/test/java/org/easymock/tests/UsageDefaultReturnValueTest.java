/**
 * Copyright 2001-2010 the original author or authors.
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

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
@SuppressWarnings("deprecation")
public class UsageDefaultReturnValueTest {
    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void defaultReturnValue() {
        mock.threeArgumentMethod(7, "", "test");
        control.setReturnValue("test", 1);

        mock.threeArgumentMethod(8, null, "test2");
        control.setReturnValue("test2", 1);

        Object defaultValue = new Object();
        control.setDefaultReturnValue(defaultValue);

        control.replay();
        assertEquals("test", mock.threeArgumentMethod(7, "", "test"));
        assertEquals("test2", mock.threeArgumentMethod(8, null, "test2"));
        assertSame(defaultValue, mock.threeArgumentMethod(7, new Object(),
                "test"));
        assertSame(defaultValue, mock.threeArgumentMethod(7, "", "test"));
        assertSame(defaultValue, mock.threeArgumentMethod(8, null, "test"));
        assertSame(defaultValue, mock.threeArgumentMethod(9, null, "test"));

        control.verify();
    }

    @Test
    public void defaultVoidCallable() {

        mock.twoArgumentMethod(1, 2);
        control.setDefaultVoidCallable();

        mock.twoArgumentMethod(1, 1);
        RuntimeException expected = new RuntimeException();
        control.setThrowable(expected);

        control.replay();
        mock.twoArgumentMethod(2, 1);
        mock.twoArgumentMethod(1, 2);
        mock.twoArgumentMethod(3, 7);

        try {
            mock.twoArgumentMethod(1, 1);
            fail("RuntimeException expected");
        } catch (RuntimeException actual) {
            assertSame(expected, actual);
        }

    }

    @Test
    public void defaultThrowable() {
        mock.twoArgumentMethod(1, 2);
        control.setVoidCallable();
        mock.twoArgumentMethod(1, 1);
        control.setVoidCallable();

        RuntimeException expected = new RuntimeException();
        control.setDefaultThrowable(expected);

        control.replay();

        mock.twoArgumentMethod(1, 2);
        mock.twoArgumentMethod(1, 1);
        try {
            mock.twoArgumentMethod(2, 1);
            fail("RuntimeException expected");
        } catch (RuntimeException actual) {
            assertSame(expected, actual);
        }
    }

    @Test
    public void defaultReturnValueBoolean() {
        mock.booleanReturningMethod(12);
        control.setReturnValue(true);
        control.setDefaultReturnValue(false);

        control.replay();

        assertFalse(mock.booleanReturningMethod(11));
        assertTrue(mock.booleanReturningMethod(12));
        assertFalse(mock.booleanReturningMethod(13));

        control.verify();
    }

    @Test
    public void returnValueAndDefaultReturnValue() throws Exception {

        mock.oneArg("");

        expectLastCall().andReturn("1");
        control.setDefaultReturnValue("2");

        control.replay();

        assertEquals("1", mock.oneArg(""));
        assertEquals("2", mock.oneArg(""));
        assertEquals("2", mock.oneArg("X"));

        control.verify();
    }
}
