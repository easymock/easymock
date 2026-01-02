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
package org.easymock.tests;

import org.easymock.MockType;
import org.easymock.internal.MocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.easymock.EasyMock.*;

/**
 * Test all kind of mocking making sure the partial mocking and interface works
 * and that to correct behavior is given.
 *
 * @author Henri Tremblay
 */
class MockingTest {

    public static class ClassToMock {
        public int foo() {
            return 10;
        }

        public int method() {
            return 20;
        }
    }

    static class PackageScopeClassToMock {
        public int foo() {
            return 10;
        }

        public int method() {
            return 20;
        }
    }

    /**
     * Make sure one mock is not interacting with another
     */
    @Test
    void testTwoMocks() {
        ClassToMock transition1 = createMock(ClassToMock.class);
        ClassToMock transition2 = createMock(ClassToMock.class);

        // Should have two different callbacks
        Assertions.assertNotSame(MocksControl.getInvocationHandler(transition2), MocksControl.getInvocationHandler(transition1));

        transition2.foo();
        transition1.foo();
    }

    @Test
    void testInterfaceMocking() {
        checkInterfaceMock(createMock(List.class), MockType.DEFAULT);
    }

    @Test
    void testNiceInterfaceMocking() {
        checkInterfaceMock(createNiceMock(List.class), MockType.NICE);
    }

    @Test
    void testStrictInterfaceMocking() {
        checkInterfaceMock(createStrictMock(List.class), MockType.STRICT);
    }

    @Test
    void testClassMocking() {
        checkClassMocking(createMock(ClassToMock.class), MockType.DEFAULT);
    }

    @Test
    void testPackageScopeClassMocking() {
        checkClassMocking(createMock(PackageScopeClassToMock.class), MockType.DEFAULT);
    }

    @Test
    void testMockObject() {
        checkClassMocking(createMock(Object.class), MockType.DEFAULT);
    }

    @Test
    void testStrictClassMocking() {
        checkClassMocking(createStrictMock(ClassToMock.class), MockType.STRICT);
    }

    @Test
    void testNiceClassMocking() {
        checkClassMocking(createNiceMock(ClassToMock.class), MockType.NICE);
    }

    @Test
    void testMockingNull() {
        try {
            createMock(null);
            Assertions.fail("Should throw a NPE");
        } catch(NullPointerException e) {
            Assertions.assertEquals("Can't mock 'null'", e.getMessage());
        }
    }

    private void checkInterfaceMock(Object mock, MockType behavior) {
        checkBehavior(mock, behavior);
    }

    private void checkClassMocking(Object mock, MockType behavior) {
        checkBehavior(mock, behavior);
    }

    private void checkBehavior(Object mock, MockType behavior) {
        Assertions.assertEquals(behavior, extractBehavior(mock));
    }

    private MockType extractBehavior(Object mock) {
        MocksControl ctrl = MocksControl.getControl(mock);
        return ctrl.getType();
    }
}
