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
public class UsageFloatingPointReturnValueTest {
    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void returnFloat() {
        mock.floatReturningMethod(0);
        control.setReturnValue(25.0F);
        control.setDefaultReturnValue(34.0F);

        control.replay();

        assertEquals(25.0F, mock.floatReturningMethod(0), 0.0F);
        assertEquals(34.0F, mock.floatReturningMethod(-4), 0.0F);
        assertEquals(34.0F, mock.floatReturningMethod(12), 0.0F);

        control.verify();
    }

    @Test
    public void returnDouble() {
        mock.doubleReturningMethod(0);
        control.setReturnValue(25.0);
        control.setDefaultReturnValue(34.0);

        control.replay();

        assertEquals(25.0, mock.doubleReturningMethod(0), 0.0);
        assertEquals(34.0, mock.doubleReturningMethod(-4), 0.0);
        assertEquals(34.0, mock.doubleReturningMethod(12), 0.0);

        control.verify();
    }
}
