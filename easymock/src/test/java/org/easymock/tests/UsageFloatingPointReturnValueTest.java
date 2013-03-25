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

import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class UsageFloatingPointReturnValueTest {

    private IMethods mock;

    @Before
    public void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    public void returnFloat() {
        expect(mock.floatReturningMethod(0)).andReturn(25.0F);
        expect(mock.floatReturningMethod(anyInt())).andStubReturn(34.0F);

        replay(mock);

        assertEquals(25.0F, mock.floatReturningMethod(0), 0.0F);
        assertEquals(34.0F, mock.floatReturningMethod(-4), 0.0F);
        assertEquals(34.0F, mock.floatReturningMethod(12), 0.0F);

        verify(mock);
    }

    @Test
    public void returnDouble() {
        expect(mock.doubleReturningMethod(0)).andReturn(25.0);
        expect(mock.doubleReturningMethod(anyInt())).andStubReturn(34.0);

        replay(mock);

        assertEquals(25.0, mock.doubleReturningMethod(0), 0.0);
        assertEquals(34.0, mock.doubleReturningMethod(-4), 0.0);
        assertEquals(34.0, mock.doubleReturningMethod(12), 0.0);

        verify(mock);
    }
}
