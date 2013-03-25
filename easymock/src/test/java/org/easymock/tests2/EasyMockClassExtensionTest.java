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

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.easymock.ConstructorArgs;
import org.easymock.EasyMock;
import org.easymock.IMockBuilder;
import org.easymock.internal.EasyMockProperties;
import org.easymock.tests2.MocksControlTest.A;
import org.junit.Test;

/**
 * @author Henri Tremblay
 */
public class EasyMockClassExtensionTest {

    private static class ParamEntry {
        Class<?>[] types;

        Object[] values;

        ParamEntry(final Class<?>[] types, final Object[] values) {
            this.types = types;
            this.values = values;
        }

        boolean isNamed() {
            return types[0] == String.class;
        }

        boolean isConstructorCalled() {
            return Arrays.asList(types).contains(ConstructorArgs.class);
        }

        A getMock(final String methodName) throws Exception {
            final Method m = EasyMock.class.getMethod(methodName, types);
            return (A) m.invoke(null, values);
        }

        public void test(final A mock) {
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
    /** Types of all method flavors */
    private static final Class<?>[][] PARAMETER_TYPES = new Class<?>[][] { new Class[] { Class.class }, //
            new Class[] { String.class, Class.class }, //
            new Class[] { Class.class, Method[].class }, //
            new Class[] { String.class, Class.class, Method[].class }, //
            new Class[] { Class.class, ConstructorArgs.class, Method[].class }, //
            new Class[] { String.class, Class.class, ConstructorArgs.class, Method[].class } //
    };

    /** Values to pass to each method call */
    private static final Object[][] PARAMETER_VALUES;

    /** All 6 flavors of method calls */
    private static final ParamEntry[] PARAMETERS = new ParamEntry[PARAMETER_TYPES.length];

    static {

        Method[] methods;
        try {
            methods = new Method[] { A.class.getMethod("add", Integer.TYPE), A.class.getMethod("toString") };
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        ConstructorArgs args;
        try {
            args = new ConstructorArgs(A.class.getConstructor(Integer.TYPE), 3);
        } catch (final SecurityException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        PARAMETER_VALUES = new Object[][] { new Object[] { A.class }, //
                new Object[] { "myMock", A.class }, //
                new Object[] { A.class, methods }, //
                new Object[] { "myMock", A.class, methods }, //
                new Object[] { A.class, args, methods }, //
                new Object[] { "myMock", A.class, args, methods } //
        };

        for (int i = 0; i < PARAMETERS.length; i++) {
            PARAMETERS[i] = new ParamEntry(PARAMETER_TYPES[i], PARAMETER_VALUES[i]);
        }
    }

    @Test
    public void testDisablingClassMocking() {
        EasyMockProperties.getInstance().setProperty(DISABLE_CLASS_MOCKING, Boolean.TRUE.toString());
        try {
            final ArrayList<?> list = createMock(ArrayList.class);
            fail("Class mocking should be disabled");
        } catch (final IllegalArgumentException e) {
            assertEquals("Class mocking is currently disabled. Change " + EasyMock.DISABLE_CLASS_MOCKING
                    + " to true do modify this behavior", e.getMessage());
        } finally {
            EasyMockProperties.getInstance().setProperty(DISABLE_CLASS_MOCKING, null);
        }
    }

    @Test
    public void testClassMocking() {
        final ArrayList<?> list = createMock(ArrayList.class);
        testList(list);
    }

    @Test
    public void testInterfaceMocking() {
        final List<?> list = createMock(List.class);
        testList(list);
    }

    private void testList(final List<?> list) {
        expect(list.size()).andReturn(3);
        replay(list);
        assertEquals(3, list.size());
        verify(list);
    }

    @Test
    public void testResetReplay() {
        final ArrayList<?> list = createStrictMock(ArrayList.class);
        expect(list.size()).andReturn(3);
        reset(list);
        expect(list.size()).andReturn(1);
        replay(list);
        assertEquals(1, list.size());
        verify(list);
    }

    @Test
    public void testResetTo() {
        final ArrayList<?> list = createMock(ArrayList.class);
        // Just to make sure the all can be called on a mock
        resetToNice(list);
        resetToStrict(list);
        resetToDefault(list);
    }

    @Test
    public void testMakeThreadSafe() {
        final ArrayList<?> list = createMock(ArrayList.class);
        // Just to make sure the all can be called on a mock
        makeThreadSafe(list, true);
    }

    @Test
    public void testVarargs() {
        final ArrayList<?> list2 = createStrictMock(ArrayList.class);
        final ArrayList<?> list1 = createStrictMock(ArrayList.class);

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

    @SuppressWarnings("unchecked")
    @Test
    public void testCheckOrder() {
        final ArrayList<Integer> list = createStrictMock(ArrayList.class);
        checkOrder(list, false);
        expect(list.add(1)).andReturn(true);
        expect(list.add(3)).andReturn(true);
        replay(list);
        list.add(3);
        list.add(1);
        verify(list);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testStrictMock_Partial() throws Exception {
        final ArrayList<Integer> list = createMockBuilder(ArrayList.class).addMockedMethod("add",
                Object.class).createStrictMock();

        expect(list.add(1)).andReturn(true);
        expect(list.add(2)).andReturn(true);

        replay(list);

        assertTrue(list.isEmpty());

        try {
            list.add(2);
            fail();
        } catch (final AssertionError e) {
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMock_Partial() throws Exception {
        final ArrayList<Integer> list = createMockBuilder(ArrayList.class).addMockedMethod("add",
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
    public void testNiceMock_Partial() throws Exception {
        final ArrayList<?> list = createMockBuilder(ArrayList.class).addMockedMethod("get").createNiceMock();

        replay(list);

        assertNull(list.get(0));
        assertTrue(list.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCompare() {
        final BigDecimal expected = new BigDecimal("15.6");
        final BigDecimal actual = new BigDecimal("15.60");

        final ArrayList<BigDecimal> list = createMock(ArrayList.class);
        expect(list.add(cmpEq(expected))).andReturn(true);

        replay(list);

        list.add(actual);

        verify(list);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNamedMock() throws Exception {
        ArrayList<BigDecimal> list = createMock("mockName", ArrayList.class);
        assertEquals("mockName", list.toString());
        list = createStrictMock("mockName", ArrayList.class);
        assertEquals("mockName", list.toString());
        list = createNiceMock("mockName", ArrayList.class);
        assertEquals("mockName", list.toString());

        // Note that toString needs to be mocked if you want EasyMock default
        // toString() behavior
        final Method m = ArrayList.class.getMethod("toString", (Class<?>[]) null);

        list = createMockBuilder(ArrayList.class).addMockedMethod(m).createMock("mockName");
        assertEquals("mockName", list.toString());
        list = createMockBuilder(ArrayList.class).addMockedMethod(m).createStrictMock("mockName");
        assertEquals("mockName", list.toString());
        list = createMockBuilder(ArrayList.class).addMockedMethod(m).createNiceMock("mockName");
        assertEquals("mockName", list.toString());
    }

    @Test
    public void testStrictMock() throws Exception {
        for (final ParamEntry p : PARAMETERS) {
            final A mock = p.getMock("createStrictMock");
            p.test(mock);
            testStrict(mock);
        }
    }

    @Test
    public void testNormalMock() throws Exception {
        for (final ParamEntry p : PARAMETERS) {
            final A mock = p.getMock("createMock");
            p.test(mock);
            testNormal(mock);
        }
    }

    @Test
    public void testNiceMock() throws Exception {
        for (final ParamEntry p : PARAMETERS) {
            final A mock = p.getMock("createNiceMock");
            p.test(mock);
            testNice(mock);
        }
    }

    @Test
    public void testCreateMockBuilder() {
        final IMockBuilder<A> builder = createMockBuilder(A.class);
        final A a = builder.withConstructor(int.class).withArgs(2).createMock();
        assertEquals(2, a.i);
    }

    // 3 mock types

    private static void testStrict(final A mock) {
        reset(mock); // just in case we are not in a stable state
        expect(mock.add(1)).andReturn(true);
        expect(mock.add(2)).andReturn(true);
        replay(mock);
        try {
            mock.add(2);
            fail("Should be ordered");
        } catch (final AssertionError e) {
        }
    }

    private static void testNormal(final A mock) {
        reset(mock); // just in case we are not in a stable state
        expect(mock.add(1)).andReturn(true);
        expect(mock.add(2)).andReturn(true);
        replay(mock);
        // unordered
        mock.add(2);
        mock.add(1);
        // but not nice
        try {
            mock.add(3);
            fail("Should be ordered");
        } catch (final AssertionError e) {
        }
    }

    private static void testNice(final A mock) {
        reset(mock); // just in case we are not in a stable state
        replay(mock);
        assertFalse(mock.add(2));
        verify(mock);
    }

    // call flavors

    private static void testNamed(final A mock) {
        assertEquals("myMock", mock.toString());
    }

    private static void testPartial_NoConstructorCalled(final A mock) {
        // not really nice since I'm looking at the inner implementation
        assertEquals(0, mock.i);
    }

    private static void testPartial_ConstructorCalled(final A mock) {
        assertEquals(3, mock.i);
    }
}
