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
 * Same as UsageExpectAndReturnTest except that each mocked method is called
 * twice to make sure the defaulting works fine.
 * 
 * @author OFFIS, Tammo Freese
 */
public class UsageExpectAndDefaultReturnTest {

    private IMethods mock;

    @Before
    public void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    public void booleanType() {
        expect(mock.booleanReturningMethod(4)).andStubReturn(true);
        replay(mock);
        assertEquals(true, mock.booleanReturningMethod(4));
        assertEquals(true, mock.booleanReturningMethod(4));
        verify(mock);
    }

    @Test
    public void longType() {
        expect(mock.longReturningMethod(4)).andStubReturn(12l);
        replay(mock);
        assertEquals(12l, mock.longReturningMethod(4));
        assertEquals(12l, mock.longReturningMethod(4));
        verify(mock);
    }

    @Test
    public void floatType() {
        expect(mock.floatReturningMethod(4)).andStubReturn(12f);
        replay(mock);
        assertEquals(12f, mock.floatReturningMethod(4), 0f);
        assertEquals(12f, mock.floatReturningMethod(4), 0f);
        verify(mock);
    }

    @Test
    public void doubleType() {
        expect(mock.doubleReturningMethod(4)).andStubReturn(12.0);
        replay(mock);
        assertEquals(12.0, mock.doubleReturningMethod(4), 0.0);
        assertEquals(12.0, mock.doubleReturningMethod(4), 0.0);
        verify(mock);
    }

    @Test
    public void objectType() {
        expect(mock.objectReturningMethod(4)).andStubReturn("12");
        replay(mock);
        assertEquals("12", mock.objectReturningMethod(4));
        assertEquals("12", mock.objectReturningMethod(4));
        verify(mock);
    }

}
