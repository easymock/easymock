/**
 * Copyright 2003-2013 the original author or authors.
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
package org.easymock.classextension.tests2;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.easymock.classextension.ConstructorArgs;
import org.easymock.classextension.IMockBuilder;
import org.easymock.classextension.tests2.MocksControlTest.A;
import org.junit.Test;

/**
 * @author Henri Tremblay
 */
@SuppressWarnings("deprecation")
public class EasyMockClassExtensionTest {

    private static class ParamEntry {
        Class<?>[] types;

        Object[] values;

        ParamEntry(Class<?>[] types, Object[] values) {
            this.types = types;
            this.values = values;
        }

        boolean isNamed() {
            return types[0] == String.class;
        }

        boolean isConstructorCalled() {
            return Arrays.asList(types).contains(
                    org.easymock.ConstructorArgs.class);
        }

        A getMock(String methodName) throws Exception {
            Method m = org.easymock.EasyMock.class.getMethod(methodName, types);
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
            new Class[] { Class.class },
            new Class[] { String.class, Class.class },
            new Class[] { Class.class, Method[].class },
            new Class[] { String.class, Class.class, Method[].class },
            new Class[] { Class.class, org.easymock.ConstructorArgs.class, Method[].class },
            new Class[] { String.class, Class.class, org.easymock.ConstructorArgs.class, Method[].class }
    };

    /** Values to pass to each method call */
    private static final Object[][] PARAMETER_VALUES;

    /** All 6 flavors of method calls */
    private static final ParamEntry[] PARAMETERS = new ParamEntry[6];

    static {

        Method[] methods;
        try {
            methods = new Method[] { A.class.getMethod("add", Integer.TYPE),
                    A.class.getMethod("toString") };
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        ConstructorArgs args;
        try {
            args = new ConstructorArgs(A.class.getConstructor(Integer.TYPE), 3);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        PARAMETER_VALUES = new Object[][] {
                new Object[] { A.class },
                new Object[] { "myMock", A.class },
                new Object[] { A.class, methods },
                new Object[] { "myMock", A.class, methods },
                new Object[] { A.class, args, methods },
                new Object[] { "myMock", A.class, args, methods }
        };

        for (int i = 0; i < PARAMETERS.length; i++) {
            PARAMETERS[i] = new ParamEntry(PARAMETER_TYPES[i],
                    PARAMETER_VALUES[i]);
        }
    }

    @Test
    public void testClassMocking() {
        ArrayList<?> list = createMock(ArrayList.class);
        testList(list);
    }

    @Test
    public void testInterfaceMocking() {
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
    public void testResetReplay() {
        ArrayList<?> list = createStrictMock(ArrayList.class);
        expect(list.size()).andReturn(3);
        reset(list);
        expect(list.size()).andReturn(1);
        replay(list);
        assertEquals(1, list.size());
        verify(list);
    }

    @Test
    public void testResetTo() {
        ArrayList<?> list = createMock(ArrayList.class);
        // Just to make sure the all can be called on a mock
        resetToNice(list);
        resetToStrict(list);
        resetToDefault(list);
    }

    @Test
    public void testMakeThreadSafe() {
        ArrayList<?> list = createMock(ArrayList.class);
        // Just to make sure the all can be called on a mock
        makeThreadSafe(list, true);
    }

    @Test
    public void testVarargs() {
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

    @SuppressWarnings("unchecked")
    @Test
    public void testCheckOrder() {
        ArrayList<Integer> list = createStrictMock(ArrayList.class);
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
        ArrayList<Integer> list = createStrictMock(ArrayList.class,
                new Method[] { ArrayList.class.getMethod("add",
                        new Class[] { Object.class }) });

        expect(list.add(1)).andReturn(true);
        expect(list.add(2)).andReturn(true);

        replay(list);

        assertTrue(list.isEmpty());

        try {
            list.add(2);
            fail();
        } catch (AssertionError e) {
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMock_Partial() throws Exception {
        ArrayList<Integer> list = createMock(ArrayList.class,
                new Method[] { ArrayList.class.getMethod("add",
                        new Class[] { Object.class }) });

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
        ArrayList<?> list = createNiceMock(ArrayList.class,
                new Method[] { ArrayList.class.getMethod("get",
                        new Class[] { Integer.TYPE }) });

        replay(list);

        assertNull(list.get(0));
        assertTrue(list.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCompare() {
        BigDecimal expected = new BigDecimal("15.6");
        BigDecimal actual = new BigDecimal("15.60");

        ArrayList<BigDecimal> list = createMock(ArrayList.class);
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
        Method m = ArrayList.class.getMethod("toString", (Class<?>[]) null);

        list = createMock("mockName", ArrayList.class, m);
        assertEquals("mockName", list.toString());
        list = createStrictMock("mockName", ArrayList.class, m);
        assertEquals("mockName", list.toString());
        list = createNiceMock("mockName", ArrayList.class, m);
        assertEquals("mockName", list.toString());
    }

    @Test
    public void testStrictMock() throws Exception {
        for (ParamEntry p : PARAMETERS) {
            A mock = p.getMock("createStrictMock");
            p.test(mock);
            testStrict(mock);
        }
    }

    @Test
    public void testNormalMock() throws Exception {
        for (ParamEntry p : PARAMETERS) {
            A mock = p.getMock("createMock");
            p.test(mock);
            testNormal(mock);
        }
    }

    @Test
    public void testNiceMock() throws Exception {
        for (ParamEntry p : PARAMETERS) {
            A mock = p.getMock("createNiceMock");
            p.test(mock);
            testNice(mock);
        }
    }

    @Test
    public void testCreateMockBuilder() {
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
        try {
            mock.add(2);
            fail("Should be ordered");
        } catch (AssertionError e) {
        }
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
        try {
            mock.add(3);
            fail("Should be ordered");
        } catch (AssertionError e) {
        }
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
