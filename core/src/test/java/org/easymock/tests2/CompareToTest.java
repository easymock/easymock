/*
 * Copyright 2001-2024 the original author or authors.
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

import java.math.BigDecimal;

import org.easymock.internal.matchers.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Henri Tremblay
 */
public class CompareToTest {

    @Test
    public void testNotComparable() {
        CompareTo<Long> cmpTo = new CompareTo<Long>(5L) {

            private static final long serialVersionUID = 1L;

            @Override
            protected String getName() {
                return null;
            }

            @Override
            protected boolean matchResult(int result) {
                fail("Shouldn't be called since the passed argument is not Comparable");
                return true;
            }

        };

        assertFalse(cmpTo.matches(new Object()));
    }

    @Test
    public void testLessThan() {
        test(new LessThan<>("b"), true, false, false, "lt");
    }

    @Test
    public void testGreaterThan() {
        test(new GreaterThan<>("b"), false, true, false, "gt");
    }

    @Test
    public void testLessOrEqual() {
        test(new LessOrEqual<>("b"), true, false, true, "leq");
    }

    @Test
    public void testGreaterOrEqual() {
        test(new GreaterOrEqual<>("b"), false, true, true, "geq");
    }

    @Test
    public void testCompareEqual() {
        test(new CompareEqual<>("b"), false, false, true, "cmpEq");

        // Make sure it works when equals provide a different result than
        // compare
        CompareEqual<BigDecimal> cmpEq = new CompareEqual<>(new BigDecimal("5.00"));
        assertTrue(cmpEq.matches(new BigDecimal("5")));
    }

    private void test(CompareTo<String> cmpTo, boolean lower, boolean higher,
            boolean equals, String name) {

        assertEquals(lower, cmpTo.matches("a"));
        assertEquals(equals, cmpTo.matches("b"));
        assertEquals(higher, cmpTo.matches("c"));

        StringBuffer sb = new StringBuffer();
        cmpTo.appendTo(sb);
        assertEquals(name + "(b)", sb.toString());
    }
}
