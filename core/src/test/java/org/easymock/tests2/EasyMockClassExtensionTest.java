/*
 * Copyright 2001-2026 the original author or authors.
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

import org.easymock.ConstructorArgs;
import org.easymock.EasyMock;
import org.easymock.IMockBuilder;
import org.easymock.internal.EasyMockProperties;
import org.easymock.tests2.MocksControlTest.A;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.DISABLE_CLASS_MOCKING;
import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.cmpEq;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.makeThreadSafe;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.resetToDefault;
import static org.easymock.EasyMock.resetToNice;
import static org.easymock.EasyMock.resetToStrict;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Henri Tremblay
 */
class EasyMockClassExtensionTest {

    private static class ParamEntry {
        final  Class<?>[] types;

        final Object[] values;

        ParamEntry(Class<?>[] types, Object[] values) {
            this.types = types;
            this.values = values;
        }

        boolean isNamed() {
            return types[0] == String.class;
        }

        boolean isConstructorCalled() {
            return Arrays.asList(types).contains(ConstructorArgs.class);
        }

        A getMock(String methodName) throws Exception {
            Method m = EasyMock.class.getMethod(methodName, types);
            return (A) m.invoke(null, values);
        }

        public void test(A mock) {
            if (isNamed()) {
                testNamed(mock);
            }
            if (isConstructorCalled()) {
                testPartial_ConstructorCalled(mock);
            } else {
                testPartial_NoConstructorCalled(mock);
            }
        }
    }

    /** Types of all method flavors */
    private static final Class<?>[][] PARAMETER_TYPES = new Class<?>[][] {
            new Class[] { Class.class }, //
            new Class[] { String.class, Class.class } //
    };

    /** Values to pass to each method call */
    private static final Object[][] PARAMETER_VALUES;

    /** All 6 flavors of method calls */
    private static final ParamEntry[] PARAMETERS = new ParamEntry[PARAMETER_TYPES.length];

    static {
        PARAMETER_VALUES = new Object[][] {
                new Object[] { A.class }, //
                new Object[] { "myMock", A.class } //
        };

        for (int i = 0; i < PARAMETERS.length; i++) {
            PARAMETERS[i] = new ParamEntry(PARAMETER_TYPES[i], PARAMETER_VALUES[i]);
        }
    }

    @Test
    void testDisablingClassMocking() {
        EasyMockProperties.getInstance().setProperty(DISABLE_CLASS_MOCKING, Boolean.TRUE.toString());
        try {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> createMock(ArrayList.class), "Class mocking should be disabled");
            assertEquals("Class mocking is currently disabled. Change " + DISABLE_CLASS_MOCKING
                    + " to true do modify this behavior", e.getMessage());
        } finally {
            EasyMockProperties.getInstance().setProperty(DISABLE_CLASS_MOCKING, null);
        }
    }

    @Test
    void testClassMocking() {
        ArrayList<?> list = createMock(ArrayList.class);
        testList(list);
    }

    @Test
    void testInterfaceMocking() {
        List<?> list = createMock(List.class);
        testList(list);
    }

    private void testList(List<?> list) {
        expect(list.size()).andReturn(3);
        replay(list);
        assertEquals(3, list.size());
        verify(list);
    }

    @Test
    void testResetReplay() {
        ArrayList<?> list = createStrictMock(ArrayList.class);
        expect(list.size()).andReturn(3);
        reset(list);
        expect(list.size()).andReturn(1);
        replay(list);
        assertEquals(1, list.size());
        verify(list);
    }

    @Test
    void testResetTo() {
        ArrayList<?> list = createMock(ArrayList.class);
        // Just to make sure the all can be called on a mock
        resetToNice(list);
        resetToStrict(list);
        resetToDefault(list);
    }

    @Test
    void testMakeThreadSafe() {
        ArrayList<?> list = createMock(ArrayList.class);
        // Just to make sure the all can be called on a mock
        makeThreadSafe(list, true);
    }

    @Test
    void testVarargs() {
        ArrayList<?> list2 = createStrictMock(ArrayList.class);
        ArrayList<?> list1 = createStrictMock(ArrayList.class);

        expect(list1.size()).andReturn(1);
        expect(list2.size()).andReturn(2);
        reset(list1, list2);

        expect(list1.size()).andReturn(3);
        expect(list2.size()).andReturn(4);
        replay(list1, list2);

        assertEquals(3, list1.size());
        assertEquals(4, list2.size());

        verify(list1, list2);
    }

    @Test
    void testCheckOrder() {
        ArrayList<Integer> list = createStrictMock(ArrayList.class);
        checkOrder(list, false);
        expect(list.add(1)).andReturn(true);
        expect(list.add(3)).andReturn(true);
        replay(list);
        list.add(3);
        list.add(1);
        verify(list);
    }

    @Test
    void testStrictMock_Partial() {
        List<Integer> list = createMockBuilder(ArrayList.class).addMockedMethod("add",
                Object.class).createStrictMock();

        expect(list.add(1)).andReturn(true);
        expect(list.add(2)).andReturn(true);

        replay(list);

        assertTrue(list.isEmpty());
        assertThrows(AssertionError.class, () -> list.add(2));
    }

    @Test
    void testMock_Partial() {
        ArrayList<Integer> list = createMockBuilder(ArrayList.class).addMockedMethod("add",
                Object.class).createMock();

        expect(list.add(1)).andReturn(true);
        expect(list.add(2)).andReturn(true);

        replay(list);

        assertTrue(list.isEmpty());

        list.add(2);
        list.add(1);

        verify(list);
    }

    @Test
    void testNiceMock_Partial() {
        ArrayList<?> list = createMockBuilder(ArrayList.class).addMockedMethod("get").createNiceMock();

        replay(list);

        assertNull(list.get(0));
        assertTrue(list.isEmpty());
    }

    @Test
    void testCompare() {
        BigDecimal expected = new BigDecimal("15.6");
        BigDecimal actual = new BigDecimal("15.60");

        ArrayList<BigDecimal> list = createMock(ArrayList.class);
        expect(list.add(cmpEq(expected))).andReturn(true);

        replay(list);

        list.add(actual);

        verify(list);
    }

    @Test
    void testNamedMock() throws Exception {
        ArrayList<BigDecimal> list = createMock("mockName", ArrayList.class);
        assertEquals("mockName", list.toString());
        list = createStrictMock("mockName", ArrayList.class);
        assertEquals("mockName", list.toString());
        list = createNiceMock("mockName", ArrayList.class);
        assertEquals("mockName", list.toString());

        // Note that toString needs to be mocked if you want EasyMock default
        // toString() behavior
        Method m = ArrayList.class.getMethod("toString", (Class<?>[]) null);

        list = createMockBuilder(ArrayList.class).addMockedMethod(m).createMock("mockName");
        assertEquals("mockName", list.toString());
        list = createMockBuilder(ArrayList.class).addMockedMethod(m).createStrictMock("mockName");
        assertEquals("mockName", list.toString());
        list = createMockBuilder(ArrayList.class).addMockedMethod(m).createNiceMock("mockName");
        assertEquals("mockName", list.toString());
    }

    @Test
    void testStrictMock() throws Exception {
        for (ParamEntry p : PARAMETERS) {
            A mock = p.getMock("createStrictMock");
            p.test(mock);
            testStrict(mock);
        }
    }

    @Test
    void testNormalMock() throws Exception {
        for (ParamEntry p : PARAMETERS) {
            A mock = p.getMock("createMock");
            p.test(mock);
            testNormal(mock);
        }
    }

    @Test
    void testNiceMock() throws Exception {
        for (ParamEntry p : PARAMETERS) {
            A mock = p.getMock("createNiceMock");
            p.test(mock);
            testNice(mock);
        }
    }

    @Test
    void testCreateMockBuilder() {
        IMockBuilder<A> builder = createMockBuilder(A.class);
        A a = builder.withConstructor(int.class).withArgs(2).createMock();
        assertEquals(2, a.i);
    }

    // 3 mock types

    private static void testStrict(A mock) {
        reset(mock); // just in case we are not in a stable state
        expect(mock.add(1)).andReturn(true);
        expect(mock.add(2)).andReturn(true);
        replay(mock);

        assertThrows(AssertionError.class, () -> mock.add(2));
    }

    private static void testNormal(A mock) {
        reset(mock); // just in case we are not in a stable state
        expect(mock.add(1)).andReturn(true);
        expect(mock.add(2)).andReturn(true);
        replay(mock);
        // unordered
        mock.add(2);
        mock.add(1);
        // but not nice
        assertThrows(AssertionError.class, () -> mock.add(3));
    }

    private static void testNice(A mock) {
        reset(mock); // just in case we are not in a stable state
        replay(mock);
        assertFalse(mock.add(2));
        verify(mock);
    }

    // call flavors

    private static void testNamed(A mock) {
        assertEquals("myMock", mock.toString());
    }

    private static void testPartial_NoConstructorCalled(A mock) {
        // not really nice since I'm looking at the inner implementation
        assertEquals(0, mock.i);
    }

    private static void testPartial_ConstructorCalled(A mock) {
        assertEquals(3, mock.i);
    }
}
