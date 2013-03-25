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

import static org.junit.Assert.*;

import org.easymock.internal.ErrorMessage;
import org.junit.Test;

/**
 * @author Henri Tremblay
 */
public class ErrorMessageTest {

    @Test
    public void testGetters() {
        final ErrorMessage m = new ErrorMessage(true, "error", 3);
        assertTrue(m.isMatching());
        assertEquals("error", m.getMessage());
        assertEquals(3, m.getActualCount());
    }

    @Test
    public void testAppendTo_matchingOne() {
        final StringBuilder sb = new StringBuilder(20);
        final ErrorMessage m = new ErrorMessage(true, "error()", 2);
        m.appendTo(sb, 1);
        assertEquals("\n    error(), actual: 3", sb.toString());
    }

    @Test
    public void testAppendTo_matchingNone() {
        final StringBuilder sb = new StringBuilder(20);
        final ErrorMessage m = new ErrorMessage(false, "error()", 2);
        m.appendTo(sb, 0);
        assertEquals("\n    error(), actual: 2", sb.toString());
    }

    @Test
    public void testAppendTo_matchingMultiple() {
        final StringBuilder sb = new StringBuilder(20);
        final ErrorMessage m = new ErrorMessage(true, "error()", 2);
        m.appendTo(sb, 2);
        assertEquals("\n    error(), actual: 2 (+1)", sb.toString());
    }
}
