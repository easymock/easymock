/*
 * Copyright 2001-2021 the original author or authors.
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
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

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

    @Test
    public void testMocksControl_PartialMock_EmptyInterface() {
        expectPartialMocking("an empty interface", false, EmptyInterface.class);
    }

    @Test
    public void testMocksControl_PartialMock_InterfaceWithoutDefaultMethods() {
        expectPartialMocking("an interface without default methods", false,
            InterfaceWithoutDefaultMethods.class);
    }

    @Test
    public void testMocksControl_PartialMock_InterfaceWithDefaultMethod() {
        expectPartialMocking("an interface with a default method", true,
            InterfaceWithDefaultMethod.class);
    }

    @Test
    public void testMocksControl_PartialMock_InterfaceWithInheritedDefaultMethod() {
        expectPartialMocking("an interface with an inherited default method", true,
            InterfaceWithInheritedDefaultMethod.class);
    }

    @Test
    public void testMocksControl_PartialMock_InterfaceWithMockedDefaultMethod() throws Exception {
        expectPartialMocking("an interface with a mocked default method", false,
            InterfaceWithDefaultMethod.class,
            InterfaceWithDefaultMethod.class.getMethod("method"));
    }

    private void testList(IMocksControl ctrl, List<?> list) {
        expect(list.size()).andReturn(3);
        ctrl.replay();
        assertEquals(3, list.size());
        ctrl.verify();
    }

    private void expectPartialMocking(String caseName, boolean expected,
        Class<?> toMock, Method... mockedMethods) {
        String allowanceText = "should" + (expected ? "" : "n't") + " be allowed";
        String message = "partial mocking on " + caseName + " " + allowanceText;
        assertEquals(message, expected, tryMock(toMock, mockedMethods));
    }

    private boolean tryMock(Class<?> toMock, Method... mockedMethods) {
        try {
            createControl().createMock(null, toMock, null, mockedMethods);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private interface EmptyInterface {}

    private interface InterfaceWithoutDefaultMethods {
        void method();
    }

    private interface InterfaceWithDefaultMethod {
        default void method() {}
    }

    private interface InterfaceWithInheritedDefaultMethod extends InterfaceWithDefaultMethod {}
}
