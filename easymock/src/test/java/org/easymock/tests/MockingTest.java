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
package org.easymock.tests;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.List;

import org.easymock.MockType;
import org.easymock.internal.MocksControl;
import org.junit.Test;

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

    /**
     * Make sure one mock is not interacting with another
     */
    @Test
    public void testTwoMocks() {
        final ClassToMock transition1 = createMock(ClassToMock.class);
        final ClassToMock transition2 = createMock(ClassToMock.class);

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
    public void testStrictClassMocking() {
        checkClassMocking(createStrictMock(ClassToMock.class), MockType.STRICT);
    }

    @Test
    public void testNiceClassMocking() {
        checkClassMocking(createNiceMock(ClassToMock.class), MockType.NICE);
    }

    private void checkInterfaceMock(final Object mock, final MockType behavior) {
        checkBehavior(mock, behavior);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testPartialClassMocking() {
        final ClassToMock mock = createMock(ClassToMock.class, getMethod());
        checkPartialClassMocking(mock, MockType.DEFAULT);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testStrictPartialClassMocking() {
        final ClassToMock mock = createStrictMock(ClassToMock.class, getMethod());
        checkPartialClassMocking(mock, MockType.STRICT);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testNicePartialClassMocking() {
        final ClassToMock mock = createNiceMock(ClassToMock.class, getMethod());
        checkPartialClassMocking(mock, MockType.NICE);
    }

    private void checkPartialClassMocking(final ClassToMock mock, final MockType behavior) {
        checkClassMocking(mock, behavior);
        assertEquals(10, mock.foo());
        expect(mock.method()).andReturn(30);
        replay(mock);
        assertEquals(10, mock.foo());
        assertEquals(30, mock.method());
        verify(mock);
    }

    private void checkClassMocking(final Object mock, final MockType behavior) {
        checkBehavior(mock, behavior);
    }

    private void checkBehavior(final Object mock, final MockType behavior) {
        assertEquals(behavior, extractBehavior(mock));
    }

    private MockType extractBehavior(final Object mock) {
        final MocksControl ctrl = MocksControl.getControl(mock);
        return ctrl.getType();
    }

    private Method[] getMethod() {
        try {
            return new Method[] { ClassToMock.class.getDeclaredMethod("method", (Class[]) null) };
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
