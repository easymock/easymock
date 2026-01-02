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

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.JTable;

/**
 * @author Henri Tremblay
 */
class PartialMockingTest {

    public static abstract class A {

        public String s;

        public int i;

        protected A(String s) {
            this.s = s;
        }

        private A(int i) {
            this.i = i;
        }

        protected abstract int foo();
    }

    public static class B {

        boolean called = false;

        public B() {
            called();
        }

        void called() {
            called = true;
        }
    }

    public static class C {

        public C() {
            called();
        }

        void called() {
            throw new RuntimeException("failed");
        }

    }

    @Test
    void testPartialMock_PublicConstructor() {
        ArrayList<String> list = createMockBuilder(ArrayList.class).withConstructor(3).createMock();
        list.add("test"); // shouldn't crash since constructor was called
    }

    @Test
    void testPartialMock_ProtectedConstructor() {
        A a = createMockBuilder(A.class).withConstructor("test").createMock();
        Assertions.assertEquals("test", a.s); // make sure constructor was called

        // Check that abstract method is mocked by default
        expect(a.foo()).andReturn(3);
        replay(a);
        Assertions.assertEquals(3, a.foo());
        verify(a);
    }

    @Test
    void testPartialMock_ConstructorNotFound() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> createMockBuilder(ArrayList.class).withConstructor(Float.TYPE).withArgs(2.0).createMock());
        Assertions.assertEquals("No constructor matching arguments can be found", ex.getMessage());
    }

    @Test
    void testPartialMock_InvalidParams() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> createMockBuilder(ArrayList.class).withConstructor(Integer.TYPE).withArgs("test"));
        Assertions.assertEquals("test isn't of type int", ex.getMessage());
    }

    @Test
    void testPartialMock_ExceptionInConstructor() {
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> createMockBuilder(ArrayList.class).withConstructor(-5).createMock());
        Assertions.assertEquals("Failed to mock class java.util.ArrayList with provider JdkClassInfoProvider", ex.getMessage());
        Assertions.assertEquals("Failed to instantiate mock calling constructor: Exception in constructor", ex.getCause().getMessage());
    }

    @Test
    void partiallyMockedSwingComponent_which_are_in_the_javax_package() {
        JTable table = EasyMock.partialMockBuilder(JTable.class).createMock();
        Assertions.assertNotNull(table);
    }

    @Test
    void partiallyMockedSwingComponentWithConstructor_calls_real_methods_from_constructor() {
        B b = EasyMock.partialMockBuilder(B.class)
            .withConstructor()
            .createMock();
        Assertions.assertNotNull(b);
    }

    @Test
    void partiallyMockedSwingComponentWithConstructor_calls_mocked_methods_from_constructor() {
        C c = EasyMock.partialMockBuilder(C.class)
            .withConstructor()
            .addMockedMethod("called")
            .createMock();
        Assertions.assertNotNull(c);
    }
}
