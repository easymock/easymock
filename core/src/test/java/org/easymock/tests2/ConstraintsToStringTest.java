/*
 * Copyright 2001-2025 the original author or authors.
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

import org.easymock.IArgumentMatcher;
import org.easymock.internal.matchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author OFFIS, Tammo Freese
 */
class ConstraintsToStringTest {
    private StringBuffer buffer;

    @BeforeEach
    void setup() {
        buffer = new StringBuffer();
    }

    @Test
    void sameToStringWithString() {
        new Same("X").appendTo(buffer);
        assertEquals("same(\"X\")", buffer.toString());

    }

    @Test
    void nullToString() {
        Null.NULL.appendTo(buffer);
        assertEquals("isNull()", buffer.toString());
    }

    @Test
    void notNullToString() {
        NotNull.NOT_NULL.appendTo(buffer);
        assertEquals("notNull()", buffer.toString());
    }

    @Test
    void anyToString() {
        Any.ANY.appendTo(buffer);
        assertEquals("<any>", buffer.toString());
    }

    @Test
    void sameToStringWithChar() {
        new Same('x').appendTo(buffer);
        assertEquals("same('x')", buffer.toString());
    }

    @Test
    void sameToStringWithObject() {
        Object o = new Object() {
            @Override
            public String toString() {
                return "X";
            }
        };
        new Same(o).appendTo(buffer);
        assertEquals("same(X)", buffer.toString());
    }

    @Test
    void equalsToStringWithString() {
        new Equals("X").appendTo(buffer);
        assertEquals("\"X\"", buffer.toString());

    }

    @Test
    void equalsToStringWithChar() {
        new Equals('x').appendTo(buffer);
        assertEquals("'x'", buffer.toString());
    }

    @Test
    void equalsToStringWithObject() {
        Object o = new Object() {
            @Override
            public String toString() {
                return "X";
            }
        };
        new Equals(o).appendTo(buffer);
        assertEquals("X", buffer.toString());
    }

    @Test
    void equalsToStringWithArray() {
        String[] s = new String[] { "a", "b", null, "c" };
        new Equals(s).appendTo(buffer);
        assertEquals("[\"a\", \"b\", null, \"c\"]", buffer.toString());
    }

    @Test
    void orToString() {
        List<IArgumentMatcher> matchers = new ArrayList<>();
        matchers.add(new Equals(1));
        matchers.add(new Equals(2));
        new Or(matchers).appendTo(buffer);
        assertEquals("or(1 (int), 2 (int))", buffer.toString());
    }

    @Test
    void notToString() {
        new Not(new Equals(1)).appendTo(buffer);
        assertEquals("not(1 (int))", buffer.toString());
    }

    @Test
    void andToString() {
        List<IArgumentMatcher> matchers = new ArrayList<>();
        matchers.add(new Equals(1));
        matchers.add(new Equals(2));
        new And(matchers).appendTo(buffer);
        assertEquals("and(1 (int), 2 (int))", buffer.toString());
    }

    @Test
    void startsWithToString() {
        new StartsWith("AB").appendTo(buffer);
        assertEquals("startsWith(\"AB\")", buffer.toString());
    }

    @Test
    void endsWithToString() {
        new EndsWith("AB").appendTo(buffer);
        assertEquals("endsWith(\"AB\")", buffer.toString());
    }

    @Test
    void containsToString() {
        new Contains("AB").appendTo(buffer);
        assertEquals("contains(\"AB\")", buffer.toString());
    }

    @Test
    void findToString() {
        new Find("\\s+").appendTo(buffer);
        assertEquals("find(\"\\\\s+\")", buffer.toString());
    }

    @Test
    void matchesToString() {
        new Matches("\\s+").appendTo(buffer);
        assertEquals("matches(\"\\\\s+\")", buffer.toString());
    }

    @Test
    void equalsWithDeltaToString() {
        new EqualsWithDelta(2.1d, 0.2d).appendTo(buffer);
        assertEquals("eq(2.1, 0.2)", buffer.toString());
    }

    @Test
    void arrayEqualsToString() {
        new ArrayEquals(new Object[] { 1, "a", null }).appendTo(buffer);
        assertEquals("[1 (int), \"a\", null]", buffer.toString());
    }
}
