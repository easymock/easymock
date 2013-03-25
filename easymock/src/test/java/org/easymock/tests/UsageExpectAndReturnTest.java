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
public class UsageExpectAndReturnTest {

    private IMethods mock;

    @Before
    public void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    public void booleanType() {
        expect(mock.booleanReturningMethod(4)).andReturn(true);
        replay(mock);
        assertEquals(true, mock.booleanReturningMethod(4));
        verify(mock);
    }

    @Test
    public void longType() {
        expect(mock.longReturningMethod(4)).andReturn(12l);
        replay(mock);
        assertEquals(12, mock.longReturningMethod(4));
        verify(mock);
    }

    @Test
    public void floatType() {
        expect(mock.floatReturningMethod(4)).andReturn(12f);
        replay(mock);
        assertEquals(12f, mock.floatReturningMethod(4), 0f);
        verify(mock);
    }

    @Test
    public void doubleType() {
        expect(mock.doubleReturningMethod(4)).andReturn(12.0);
        replay(mock);
        assertEquals(12.0, mock.doubleReturningMethod(4), 0.0);
        verify(mock);
    }

    @Test
    public void object() {
        expect(mock.objectReturningMethod(4)).andReturn("12");
        replay(mock);
        assertEquals("12", mock.objectReturningMethod(4));
        verify(mock);
    }

    @Test
    public void booleanAndRange() {
        expect(mock.booleanReturningMethod(4)).andReturn(true).once();
        replay(mock);
        assertEquals(true, mock.booleanReturningMethod(4));
        verify(mock);
    }

    @Test
    public void longAndRange() {
        expect(mock.longReturningMethod(4)).andReturn(12l).once();
        replay(mock);
        assertEquals(12, mock.longReturningMethod(4));
        verify(mock);
    }

    @Test
    public void floatAndRange() {
        expect(mock.floatReturningMethod(4)).andReturn(12f).once();
        replay(mock);
        assertEquals(12f, mock.floatReturningMethod(4), 0f);
        verify(mock);
    }

    @Test
    public void doubleAndRange() {
        expect(mock.doubleReturningMethod(4)).andReturn(12.0).once();
        replay(mock);
        assertEquals(12.0, mock.doubleReturningMethod(4), 0.0);
        verify(mock);
    }

    @Test
    public void objectAndRange() {
        expect(mock.objectReturningMethod(4)).andReturn("12").once();
        replay(mock);
        assertEquals("12", mock.objectReturningMethod(4));
        verify(mock);
    }

    @Test
    public void booleanAndCount() {
        expect(mock.booleanReturningMethod(4)).andReturn(true).times(2);
        replay(mock);
        assertEquals(true, mock.booleanReturningMethod(4));
        assertEquals(true, mock.booleanReturningMethod(4));
        verify(mock);
    }

    @Test
    public void longAndCount() {
        expect(mock.longReturningMethod(4)).andReturn(12l).times(2);
        replay(mock);
        assertEquals(12, mock.longReturningMethod(4));
        assertEquals(12, mock.longReturningMethod(4));
        verify(mock);
    }

    @Test
    public void floatAndCount() {
        expect(mock.floatReturningMethod(4)).andReturn(12f).times(2);
        replay(mock);
        assertEquals(12f, mock.floatReturningMethod(4), 0f);
        assertEquals(12f, mock.floatReturningMethod(4), 0f);
        verify(mock);
    }

    @Test
    public void doubleAndCount() {
        expect(mock.doubleReturningMethod(4)).andReturn(12.0).times(2);
        replay(mock);
        assertEquals(12.0, mock.doubleReturningMethod(4), 0.0);
        assertEquals(12.0, mock.doubleReturningMethod(4), 0.0);
        verify(mock);
    }

    @Test
    public void objectAndCount() {
        expect(mock.objectReturningMethod(4)).andReturn("12").times(2);
        replay(mock);
        assertEquals("12", mock.objectReturningMethod(4));
        assertEquals("12", mock.objectReturningMethod(4));
        verify(mock);
    }

    @Test
    public void booleanAndMinMax() {
        expect(mock.booleanReturningMethod(4)).andReturn(true).times(2, 3);
        replay(mock);
        assertEquals(true, mock.booleanReturningMethod(4));
        assertEquals(true, mock.booleanReturningMethod(4));
        verify(mock);
    }

    @Test
    public void longAndMinMax() {
        expect(mock.longReturningMethod(4)).andReturn(12l).times(2, 3);
        replay(mock);
        assertEquals(12, mock.longReturningMethod(4));
        assertEquals(12, mock.longReturningMethod(4));
        verify(mock);
    }

    @Test
    public void floatAndMinMax() {
        expect(mock.floatReturningMethod(4)).andReturn(12f).times(2, 3);
        replay(mock);
        assertEquals(12f, mock.floatReturningMethod(4), 0f);
        assertEquals(12f, mock.floatReturningMethod(4), 0f);
        verify(mock);
    }

    @Test
    public void doubleAndMinMax() {
        expect(mock.doubleReturningMethod(4)).andReturn(12.0).times(2, 3);
        replay(mock);
        assertEquals(12.0, mock.doubleReturningMethod(4), 0.0);
        assertEquals(12.0, mock.doubleReturningMethod(4), 0.0);
        verify(mock);
    }

    @Test
    public void objectAndMinMax() {
        expect(mock.objectReturningMethod(4)).andReturn("12").times(2, 3);
        replay(mock);
        assertEquals("12", mock.objectReturningMethod(4));
        assertEquals("12", mock.objectReturningMethod(4));
        verify(mock);
    }
}
