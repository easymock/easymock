/*
 * Copyright 2001-2020 the original author or authors.
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

import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Henri Tremblay
 */
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
    public void finalEqualsShouldNotStackOverflow() {
        MyInt myInt = createMock(MyInt.class);
        expect(myInt.get()).andReturn(42);
        replay(myInt);
        assertEquals(42, myInt.get());
    }
}
