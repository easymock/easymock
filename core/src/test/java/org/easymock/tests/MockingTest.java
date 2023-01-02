/*
 * Copyright 2001-2023 the original author or authors.
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
import org.junit.Test;

import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * Test all kind of mocking making sure the partial mocking and interface works
 * and that to correct behavior is given.
 *
 * @author Henri Tremblay
 */
public class MockingTest {

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
    public void testTwoMocks() {
        ClassToMock transition1 = createMock(ClassToMock.class);
        ClassToMock transition2 = createMock(ClassToMock.class);

        // Should have two different callbacks
        assertNotSame(MocksControl.getInvocationHandler(transition2),
                MocksControl.getInvocationHandler(transition1));

        transition2.foo();
        transition1.foo();
    }

    @Test
    public void testInterfaceMocking() {
        checkInterfaceMock(createMock(List.class), MockType.DEFAULT);
    }

    @Test
    public void testNiceInterfaceMocking() {
        checkInterfaceMock(createNiceMock(List.class), MockType.NICE);
    }

    @Test
    public void testStrictInterfaceMocking() {
        checkInterfaceMock(createStrictMock(List.class), MockType.STRICT);
    }

    @Test
    public void testClassMocking() {
        checkClassMocking(createMock(ClassToMock.class), MockType.DEFAULT);
    }

    @Test
    public void testPackageScopeClassMocking() {
        checkClassMocking(createMock(PackageScopeClassToMock.class), MockType.DEFAULT);
    }

    @Test
    public void testMockObject() {
        checkClassMocking(createMock(Object.class), MockType.DEFAULT);
    }

    @Test
    public void testStrictClassMocking() {
        checkClassMocking(createStrictMock(ClassToMock.class), MockType.STRICT);
    }

    @Test
    public void testNiceClassMocking() {
        checkClassMocking(createNiceMock(ClassToMock.class), MockType.NICE);
    }

    @Test
    public void testMockingNull() {
        try {
            createMock(null);
            fail("Should throw a NPE");
        } catch(NullPointerException e) {
            assertEquals("Can't mock 'null'", e.getMessage());
        }
    }

    private void checkInterfaceMock(Object mock, MockType behavior) {
        checkBehavior(mock, behavior);
    }

    private void checkClassMocking(Object mock, MockType behavior) {
        checkBehavior(mock, behavior);
    }

    private void checkBehavior(Object mock, MockType behavior) {
        assertEquals(behavior, extractBehavior(mock));
    }

    private MockType extractBehavior(Object mock) {
        MocksControl ctrl = MocksControl.getControl(mock);
        return ctrl.getType();
    }
}
