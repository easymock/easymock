/**
 * Copyright 2001-2018 the original author or authors.
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

import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Henri Tremblay
 */
public class DelegateToTest {

    public interface IMyInterface {
        int getInt(int k);
    }

    public interface IMyVarArgsInterface {
        int getInts(int... vals);
        int getMoreInts(int i, int... vals);
        int getObjects(Object o, String... vals);
    }

    @Test
    public void testDelegate() {
        IMyInterface mock = createMock(IMyInterface.class);
        IMyInterface delegateTo = new IMyInterface() {
            private int i = 0;

            public int getInt(int k) {
                return i += k;
            }
        };

        expect(mock.getInt(10)).andDelegateTo(delegateTo);
        expect(mock.getInt(5)).andDelegateTo(delegateTo).andDelegateTo(delegateTo).times(2);

        replay(mock);

        assertEquals(10, mock.getInt(10));
        assertEquals(15, mock.getInt(5));
        assertEquals(20, mock.getInt(5));
        assertEquals(25, mock.getInt(5));

        verify(mock);
    }

    @Test
    public void testStubDelegate() {
        IMyInterface mock = createMock(IMyInterface.class);
        IMyInterface delegateTo = new IMyInterface() {
            private int i = 0;

            public int getInt(int k) {
                return ++i;
            }
        };
        expect(mock.getInt(5)).andReturn(3).andStubDelegateTo(delegateTo);
        expect(mock.getInt(20)).andStubDelegateTo(delegateTo);

        replay(mock);

        assertEquals(3, mock.getInt(5));
        assertEquals(1, mock.getInt(5));
        assertEquals(2, mock.getInt(5));
        assertEquals(3, mock.getInt(20));
        assertEquals(4, mock.getInt(20));

        verify(mock);
    }

    @Test
    public void testReturnException() {
        IMyInterface m = createMock(IMyInterface.class);
        IMyInterface delegateTo = new IMyInterface() {
            public int getInt(int k) {
                throw new ArithmeticException("Not good!");
            }
        };
        expect(m.getInt(5)).andDelegateTo(delegateTo);

        replay(m);

        try {
            m.getInt(5);
            fail();
        } catch (ArithmeticException e) {
            assertEquals("Not good!", e.getMessage());
        }

        verify(m);
    }

    @Test
    public void testWrongClass() {
        IMyInterface m = createMock(IMyInterface.class);
        expect(m.getInt(0)).andDelegateTo("allo");
        replay(m);
        try {
            m.getInt(0);
            fail("Should throw an exception");
        } catch (IllegalArgumentException e) {
            assertEquals(
                    "Delegation to object [allo] is not implementing the mocked method [public abstract int org.easymock.tests2.DelegateToTest$IMyInterface.getInt(int)]",
                    e.getMessage());
        }
    }

    @Test
    public void nullDelegationNotAllowed() {
        IMyInterface mock = createMock(IMyInterface.class);
        try {
            expect(mock.getInt(1)).andDelegateTo(null);
            fail();
        } catch (NullPointerException expected) {
            assertEquals("delegated to object must not be null", expected.getMessage());
        }
    }

    @Test
    public void nullStubDelegationNotAllowed() {
        IMyInterface mock = createMock(IMyInterface.class);
        try {
            expect(mock.getInt(1)).andStubDelegateTo(null);
            fail();
        } catch (NullPointerException expected) {
            assertEquals("delegated to object must not be null", expected.getMessage());
        }
    }

    @Test
    public void varargs() {
        IMyVarArgsInterface mock = createMock(IMyVarArgsInterface.class);
        IMyVarArgsInterface delegateTo = new IMyVarArgsInterface() {
            @Override
            public int getInts(int... vals) {
                return 0;
            }

            @Override
            public int getMoreInts(int i, int... vals) {
                return 0;
            }

            @Override
            public int getObjects(Object o, String... vals) {
                return 0;
            }
        };

        expect(mock.getInts(1, 2, 3, 4, 5)).andDelegateTo(delegateTo);
        expect(mock.getMoreInts(1, 2, 3)).andDelegateTo(delegateTo);
        expect(mock.getObjects("a", "b", "c")).andDelegateTo(delegateTo);
        expect(mock.getInts()).andDelegateTo(delegateTo);

        replay(mock);

        assertEquals(0, mock.getInts(1, 2, 3, 4, 5));
        assertEquals(0, mock.getMoreInts(1, 2, 3));
        assertEquals(0, mock.getObjects("a", "b", "c"));
        assertEquals(0, mock.getInts());
    }

}
