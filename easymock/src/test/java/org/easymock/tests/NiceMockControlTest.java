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

/**
 * @author OFFIS, Tammo Freese
 */
@SuppressWarnings("deprecation")
public class NiceMockControlTest {
    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createNiceControl(IMethods.class);
        mock = control.getMock();
        control.replay();
    }

    @Test
    public void defaultReturnValueBoolean() {
        assertEquals(false, mock.booleanReturningMethod(12));
        control.verify();
    }

    @Test
    public void defaultReturnValueFloat() {
        assertEquals(0.0f, mock.floatReturningMethod(12), 0.0f);
        control.verify();
    }

    @Test
    public void defaultReturnValueDouble() {
        assertEquals(0.0d, mock.doubleReturningMethod(12), 0.0d);
        control.verify();
    }

    @Test
    public void defaultReturnValueObject() {
        assertEquals(null, mock.objectReturningMethod(12));
        control.verify();
    }
}
