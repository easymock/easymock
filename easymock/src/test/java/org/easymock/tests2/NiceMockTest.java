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
package org.easymock.tests2;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.tests.IMethods;
import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class NiceMockTest {

    IMethods mock;

    @Before
    public void setup() {
        mock = createNiceMock(IMethods.class);
        replay(mock);
    }

    @Test
    public void defaultReturnValueBoolean() {
        assertEquals(false, mock.booleanReturningMethod(12));
        verify(mock);
    }

    @Test
    public void defaultReturnValueFloat() {
        assertEquals(0.0f, mock.floatReturningMethod(12), 0.0f);
        verify(mock);
    }

    @Test
    public void defaultReturnValueDouble() {
        assertEquals(0.0d, mock.doubleReturningMethod(12), 0.0d);
        verify(mock);
    }

    @Test
    public void defaultReturnValueObject() {
        assertEquals(null, mock.objectReturningMethod(12));
        verify(mock);
    }
}
