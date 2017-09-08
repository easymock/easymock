package org.easymock.tests;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class FinalEqualsTest {

    static class MyInt {
        private final int i;

        public MyInt(int i) {
            this.i = i;
        }

        @Override
        public final boolean equals(Object obj) {
            return obj instanceof MyInt && this.get() == ((MyInt) obj).get();
        }

        public int get() {
            return i;
        }
    }

    @Test
    public void testGet() {
        MyInt myInt = createMock(MyInt.class);
        expect(myInt.get()).andReturn(42);
        replay(myInt);
        assertEquals(42, myInt.get());
    }
}
