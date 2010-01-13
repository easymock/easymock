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

import static org.junit.Assert.*;

import org.easymock.ArgumentsMatcher;
import org.easymock.MockControl;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
@SuppressWarnings("deprecation")
public class EqualsMatcherTest {
    final ArgumentsMatcher MATCHER = MockControl.EQUALS_MATCHER;

    @Test
    public void equalsMatcher() {
        assertTrue(MATCHER.matches(null, null));
        assertFalse(MATCHER.matches(null, new Object[0]));
        assertFalse(MATCHER.matches(new Object[0], null));
        assertFalse(MATCHER.matches(new Object[] { "" }, new Object[] { null }));
        assertFalse(MATCHER.matches(new Object[] { null }, new Object[] { "" }));
        assertTrue(MATCHER
                .matches(new Object[] { null }, new Object[] { null }));
        assertTrue(MATCHER.matches(new Object[] { "x" }, new Object[] { "x" }));
    }

    @Test
    public void differentNumberOfArguments() {
        assertFalse(MATCHER.matches(new Object[2], new Object[3]));
    }
}