/*
 * Copyright 2001-2023 the original author or authors.
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.easymock.EasyMock.*;

/**
 * @author OFFIS, Tammo Freese
 */
public class UsageRangeTest {

    private Iterator<String> mock;

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setup() {
        mock = createStrictMock(Iterator.class);
    }

    @Test
    public void zeroOrMoreNoCalls() {
        expect(mock.hasNext()).andReturn(false).anyTimes();
        replay(mock);
        verify(mock);
    }

    @Test
    public void zeroOrMoreOneCall() {
        expect(mock.hasNext()).andReturn(false).anyTimes();
        replay(mock);
        Assertions.assertFalse(mock.hasNext());
        verify(mock);
    }

    @Test
    public void zeroOrMoreThreeCalls() {
        expect(mock.hasNext()).andReturn(false).anyTimes();
        replay(mock);
        Assertions.assertFalse(mock.hasNext());
        Assertions.assertFalse(mock.hasNext());
        Assertions.assertFalse(mock.hasNext());
        verify(mock);
    }

    @Test
    public void combination() {
        expect(mock.hasNext()).andReturn(true).atLeastOnce();
        expect(mock.next()).andReturn("1");

        expect(mock.hasNext()).andReturn(true).atLeastOnce();
        expect(mock.next()).andReturn("2");

        expect(mock.hasNext()).andReturn(false).atLeastOnce();

        replay(mock);

        Assertions.assertTrue(mock.hasNext());
        Assertions.assertTrue(mock.hasNext());
        Assertions.assertTrue(mock.hasNext());

        Assertions.assertEquals("1", mock.next());

        try {
            mock.next();
            Assertions.fail();
        } catch (AssertionError expected) {
        }

        Assertions.assertTrue(mock.hasNext());

        Assertions.assertEquals("2", mock.next());

        Assertions.assertFalse(mock.hasNext());

        verifyRecording(mock);

    }
}
