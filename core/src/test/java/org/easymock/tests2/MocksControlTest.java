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

import org.easymock.ConstructorArgs;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Henri Tremblay
 */
class MocksControlTest {

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
    void testMocksControl_Interface() {
        IMocksControl ctrl = createControl();
        List<?> list = ctrl.createMock(List.class);
        testList(ctrl, list);
    }

    @Test
    void testMocksControl_Class() {
        IMocksControl ctrl = createControl();
        ArrayList<?> list = ctrl.createMock(ArrayList.class);
        testList(ctrl, list);
    }

    @Test
    void testMocksControl_Class_WithName() {
        IMocksControl ctrl = createControl();
        ArrayList<?> list = ctrl.createMock("myMock", ArrayList.class);
        testList(ctrl, list);
    }

    @Test
    void testMocksControl_PartialMock_NoConstructorCalled() throws Exception {
        IMocksControl ctrl = createControl();
        A a = ctrl.createMock(null, A.class, null, A.class.getMethod("bar"), A.class.getMethod(
                "toString"));

        Assertions.assertEquals(0, a.i, "No constructor called so should not be initialized");
        expect(a.bar()).andReturn(5);
        replay(a);
        Assertions.assertEquals(5, a.foo(), "foo isn't mocked so it will call bar which return 5");
        verify(a);

        Assertions.assertEquals("EasyMock for class org.easymock.tests2.MocksControlTest$A", a.toString());
    }

    @Test
    void testMocksControl_NamedPartialMock_NoConstructorCalled() throws Exception {
        IMocksControl ctrl = createControl();
        A a = ctrl.createMock("myMock", A.class, null, A.class.getMethod("bar"), A.class
                .getMethod("toString"));

        Assertions.assertEquals(0, a.i, "No constructor called so should not be initialized");
        expect(a.bar()).andReturn(5);
        replay(a);
        Assertions.assertEquals(5, a.foo(), "foo isn't mocked so it will call bar which return 5");
        verify(a);

        Assertions.assertEquals("myMock", a.toString());
    }

    @Test
    void testMocksControl_PartialMock_ConstructorCalled() throws Exception {
        IMocksControl ctrl = createControl();

        ConstructorArgs args = new ConstructorArgs(A.class.getConstructor(Integer.TYPE), 6);

        A a = ctrl.createMock(null, A.class, args, A.class.getMethod("bar"), A.class.getMethod(
                "toString"));

        Assertions.assertEquals(6, a.i, "Constructor called so should be initialized");
        expect(a.bar()).andReturn(5);
        replay(a);
        Assertions.assertEquals(5, a.foo(), "foo isn't mocked so it will call bar which return 5");
        verify(a);

        Assertions.assertEquals("EasyMock for class org.easymock.tests2.MocksControlTest$A", a.toString());
    }

    @Test
    void testMocksControl_NamedPartialMock_ConstructorCalled() throws Exception {
        IMocksControl ctrl = createControl();

        ConstructorArgs args = new ConstructorArgs(A.class.getConstructor(Integer.TYPE), 6);

        A a = ctrl.createMock("myMock", A.class, args, A.class.getMethod("bar"), A.class
                .getMethod("toString"));

        Assertions.assertEquals(6, a.i, "Constructor called so should be initialized");
        expect(a.bar()).andReturn(5);
        replay(a);
        Assertions.assertEquals(5, a.foo(), "foo isn't mocked so it will call bar which return 5");
        verify(a);

        Assertions.assertEquals("myMock", a.toString());
    }

    private void testList(IMocksControl ctrl, List<?> list) {
        expect(list.size()).andReturn(3);
        ctrl.replay();
        Assertions.assertEquals(3, list.size());
        ctrl.verify();
    }

}
