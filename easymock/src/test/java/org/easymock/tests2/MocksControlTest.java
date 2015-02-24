/**
 * Copyright 2001-2015 the original author or authors.
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
import org.easymock.IMocksControl;
import org.easymock.MockType;
import org.easymock.internal.MocksControl;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Henri Tremblay
 */
public class MocksControlTest {

    public static class A {
        int i = 1;

        public A(int i) {
            this.i = i;
        }

        public int foo() {
            return bar();
        }

        public int bar() {
            return i;
        }

        public boolean add(int i) {
            this.i += i;
            return true;
        }
    }

    @Test
    public void testMocksControl_Interface() {
        IMocksControl ctrl = createControl();
        List<?> list = ctrl.createMock(List.class);
        testList(ctrl, list);
    }

    @Test
    public void testMocksControl_Class() {
        IMocksControl ctrl = createControl();
        ArrayList<?> list = ctrl.createMock(ArrayList.class);
        testList(ctrl, list);
    }

    @Test
    public void testMocksControl_Class_WithName() {
        IMocksControl ctrl = createControl();
        ArrayList<?> list = ctrl.createMock("myMock", ArrayList.class);
        testList(ctrl, list);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testMocksControl_PartialMock_NoConstructorCalled() throws Exception {
        IMocksControl ctrl = createControl();
        A a = ctrl.createMock(null, A.class, null, A.class.getMethod("bar"), A.class.getMethod(
                "toString"));

        assertEquals("No constructor called so should not be initialized", 0, a.i);
        expect(a.bar()).andReturn(5);
        replay(a);
        assertEquals("foo isn't mocked so it will call bar which return 5", 5, a.foo());
        verify(a);

        assertEquals("EasyMock for class org.easymock.tests2.MocksControlTest$A", a.toString());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testMocksControl_NamedPartialMock_NoConstructorCalled() throws Exception {
        IMocksControl ctrl = createControl();
        A a = ctrl.createMock("myMock", A.class, null, A.class.getMethod("bar"), A.class
                .getMethod("toString"));

        assertEquals("No constructor called so should not be initialized", 0, a.i);
        expect(a.bar()).andReturn(5);
        replay(a);
        assertEquals("foo isn't mocked so it will call bar which return 5", 5, a.foo());
        verify(a);

        assertEquals("myMock", a.toString());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testMocksControl_PartialMock_ConstructorCalled() throws Exception {
        IMocksControl ctrl = createControl();

        ConstructorArgs args = new ConstructorArgs(A.class.getConstructor(Integer.TYPE), 6);

        A a = ctrl.createMock(null, A.class, args, A.class.getMethod("bar"), A.class.getMethod(
                "toString"));

        assertEquals("Constructor called so should be initialized", 6, a.i);
        expect(a.bar()).andReturn(5);
        replay(a);
        assertEquals("foo isn't mocked so it will call bar which return 5", 5, a.foo());
        verify(a);

        assertEquals("EasyMock for class org.easymock.tests2.MocksControlTest$A", a.toString());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testMocksControl_NamedPartialMock_ConstructorCalled() throws Exception {
        IMocksControl ctrl = createControl();

        ConstructorArgs args = new ConstructorArgs(A.class.getConstructor(Integer.TYPE), 6);

        A a = ctrl.createMock("myMock", A.class, args, A.class.getMethod("bar"), A.class
                .getMethod("toString"));

        assertEquals("Constructor called so should be initialized", 6, a.i);
        expect(a.bar()).andReturn(5);
        replay(a);
        assertEquals("foo isn't mocked so it will call bar which return 5", 5, a.foo());
        verify(a);

        assertEquals("myMock", a.toString());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testInterfaceForbidden_PartialMock() throws Exception {
        ConstructorArgs args = new ConstructorArgs(ArrayList.class.getConstructor(Integer.TYPE), 6);
        Method[] methods = new Method[] { List.class.getMethod("size") };

        IMocksControl ctrl = createControl();

        try {
            ctrl.createMock(null, List.class, null, methods);
            fail("partial mocking on interface shouldn't be allowed");
        } catch (IllegalArgumentException e) {
        }

        try {
            ctrl.createMock(null, List.class, args, methods);
            fail("partial mocking on interface shouldn't be allowed");
        } catch (IllegalArgumentException e) {
        }

        try {
            ctrl.createMock("myMock", List.class, null, methods);
            fail("partial mocking on interface shouldn't be allowed");
        } catch (IllegalArgumentException e) {
        }

        try {
            ctrl.createMock("myMock", List.class, args, methods);
            fail("partial mocking on interface shouldn't be allowed");
        } catch (IllegalArgumentException e) {
        }
    }

    private void testList(IMocksControl ctrl, List<?> list) {
        expect(list.size()).andReturn(3);
        ctrl.replay();
        assertEquals(3, list.size());
        ctrl.verify();
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testCreateOldMockTypeFromNewMockType() {
        assertSame(MockType.NICE, MocksControl.MockType.NICE.realType);
    }

    @Test
    public void testCreateMocksControlFromOldMockType() {
        @SuppressWarnings("deprecation")
        MocksControl c = new MocksControl(MocksControl.MockType.NICE);
        assertSame(MockType.NICE, c.getType());
    }
}
