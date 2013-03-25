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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.easymock.IArgumentMatcher;
import org.easymock.internal.matchers.*;
import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class ConstraintsToStringTest {
    private StringBuffer buffer;

    @Before
    public void setup() {
        buffer = new StringBuffer();
    }

    @Test
    public void sameToStringWithString() {
        new Same("X").appendTo(buffer);
        assertEquals("same(\"X\")", buffer.toString());

    }

    @Test
    public void nullToString() {
        Null.NULL.appendTo(buffer);
        assertEquals("isNull()", buffer.toString());
    }

    @Test
    public void notNullToString() {
        NotNull.NOT_NULL.appendTo(buffer);
        assertEquals("notNull()", buffer.toString());
    }

    @Test
    public void anyToString() {
        Any.ANY.appendTo(buffer);
        assertEquals("<any>", buffer.toString());
    }

    @Test
    public void sameToStringWithChar() {
        new Same('x').appendTo(buffer);
        assertEquals("same('x')", buffer.toString());
    }

    @Test
    public void sameToStringWithObject() {
        final Object o = new Object() {
            @Override
            public String toString() {
                return "X";
            }
        };
        new Same(o).appendTo(buffer);
        assertEquals("same(X)", buffer.toString());
    }

    @Test
    public void equalsToStringWithString() {
        new Equals("X").appendTo(buffer);
        assertEquals("\"X\"", buffer.toString());

    }

    @Test
    public void equalsToStringWithChar() {
        new Equals('x').appendTo(buffer);
        assertEquals("'x'", buffer.toString());
    }

    @Test
    public void equalsToStringWithObject() {
        final Object o = new Object() {
            @Override
            public String toString() {
                return "X";
            }
        };
        new Equals(o).appendTo(buffer);
        assertEquals("X", buffer.toString());
    }

    @Test
    public void equalsToStringWithArray() {
        final String[] s = new String[] { "a", "b", null, "c" };
        new Equals(s).appendTo(buffer);
        assertEquals("[\"a\", \"b\", null, \"c\"]", buffer.toString());
    }

    @Test
    public void orToString() {
        final List<IArgumentMatcher> matchers = new ArrayList<IArgumentMatcher>();
        matchers.add(new Equals(1));
        matchers.add(new Equals(2));
        new Or(matchers).appendTo(buffer);
        assertEquals("or(1, 2)", buffer.toString());
    }

    @Test
    public void notToString() {
        new Not(new Equals(1)).appendTo(buffer);
        assertEquals("not(1)", buffer.toString());
    }

    @Test
    public void andToString() {
        final List<IArgumentMatcher> matchers = new ArrayList<IArgumentMatcher>();
        matchers.add(new Equals(1));
        matchers.add(new Equals(2));
        new And(matchers).appendTo(buffer);
        assertEquals("and(1, 2)", buffer.toString());
    }

    @Test
    public void startsWithToString() {
        new StartsWith("AB").appendTo(buffer);
        assertEquals("startsWith(\"AB\")", buffer.toString());
    }

    @Test
    public void endsWithToString() {
        new EndsWith("AB").appendTo(buffer);
        assertEquals("endsWith(\"AB\")", buffer.toString());
    }

    @Test
    public void containsToString() {
        new Contains("AB").appendTo(buffer);
        assertEquals("contains(\"AB\")", buffer.toString());
    }

    @Test
    public void findToString() {
        new Find("\\s+").appendTo(buffer);
        assertEquals("find(\"\\\\s+\")", buffer.toString());
    }

    @Test
    public void matchesToString() {
        new Matches("\\s+").appendTo(buffer);
        assertEquals("matches(\"\\\\s+\")", buffer.toString());
    }

    @Test
    public void equalsWithDeltaToString() {
        new EqualsWithDelta(2.1d, 0.2d).appendTo(buffer);
        assertEquals("eq(2.1, 0.2)", buffer.toString());
    }

    @Test
    public void arrayEqualsToString() {
        new ArrayEquals(new Object[] { 1, "a", null }).appendTo(buffer);
        assertEquals("[1, \"a\", null]", buffer.toString());
    }
}
