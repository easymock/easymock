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

import java.util.Iterator;

import org.easymock.MockControl;
import org.easymock.internal.Range;
import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
@SuppressWarnings("deprecation")
public class UsageRangeTest {

    private Iterator<String> mock;

    private MockControl<Iterator<String>> control;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        control = MockControl.createStrictControl((Class)Iterator.class);
        mock = control.getMock();
    }

    @Test
    public void zeroOrMoreNoCalls() {
        mock.hasNext();
        control.setReturnValue(false, MockControl.ZERO_OR_MORE);
        control.replay();
        control.verify();
    }

    @Test
    public void zeroOrMoreOneCall() {
        mock.hasNext();
        control.setReturnValue(false, MockControl.ZERO_OR_MORE);
        control.replay();
        assertFalse(mock.hasNext());
        control.verify();
    }

    @Test
    public void zeroOrMoreThreeCalls() {
        mock.hasNext();
        control.setReturnValue(false, MockControl.ZERO_OR_MORE);
        control.replay();
        assertFalse(mock.hasNext());
        assertFalse(mock.hasNext());
        assertFalse(mock.hasNext());
        control.verify();
    }

    @Test
    public void combination() {
        mock.hasNext();
        control.setReturnValue(true, MockControl.ONE_OR_MORE);
        mock.next();
        control.setReturnValue("1");

        mock.hasNext();
        control.setReturnValue(true, MockControl.ONE_OR_MORE);
        mock.next();
        control.setReturnValue("2");

        mock.hasNext();
        control.setReturnValue(false, MockControl.ONE_OR_MORE);

        control.replay();

        assertTrue(mock.hasNext());
        assertTrue(mock.hasNext());
        assertTrue(mock.hasNext());

        assertEquals("1", mock.next());

        try {
            mock.next();
            fail();
        } catch (AssertionError expected) {
        }

        assertTrue(mock.hasNext());

        assertEquals("2", mock.next());

        assertFalse(mock.hasNext());

        control.verify();

    }

    @Test
    public void withIllegalOwnRange() {
        mock.hasNext();
        try {
            control.setReturnValue(true, new Range(2, 7));
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected Range", e.getMessage());
        }
    }
}
