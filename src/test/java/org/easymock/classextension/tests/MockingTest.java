/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.classextension.tests;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import org.easymock.classextension.internal.ClassExtensionHelper;
import org.easymock.internal.MocksControl;
import org.easymock.internal.MocksControl.MockType;
import org.junit.Test;

/**
 * Test all kind of mocking making sure the partial mocking and interface works
 * and that to correct behavior is given.
 */
@SuppressWarnings("deprecation")
public class MockingTest {

    private MockControl<?> ctrl;

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
        MockControl<ClassToMock> transition1Control = MockClassControl
                .createControl(ClassToMock.class);
        ClassToMock transition1 = transition1Control.getMock();
        MockControl<ClassToMock> transition2Control = MockClassControl
                .createControl(ClassToMock.class);
        ClassToMock transition2 = transition2Control.getMock();

        // Should have two different callbacks
        assertNotSame(ClassExtensionHelper.getInterceptor(transition2),
                ClassExtensionHelper.getInterceptor(transition1));

        transition2.foo();
        transition1.foo();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testInterfaceMocking() {
        MockControl<List> ctrl = MockClassControl.createControl(List.class);
        checkInterfaceMock(ctrl, MockType.DEFAULT);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNiceInterfaceMocking() {
        MockControl<List> ctrl = MockClassControl.createNiceControl(List.class);
        checkInterfaceMock(ctrl, MockType.NICE);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testStrictInterfaceMocking() {
        MockControl<List> ctrl = MockClassControl
                .createStrictControl(List.class);
        checkInterfaceMock(ctrl, MockType.STRICT);
    }

    @Test
    public void testClassMocking() {
        MockControl<ClassToMock> ctrl = MockClassControl
                .createControl(ClassToMock.class);
        checkClassMocking(ctrl, MockType.DEFAULT);
    }

    @Test
    public void testStrictClassMocking() {
        MockControl<ClassToMock> ctrl = MockClassControl
                .createStrictControl(ClassToMock.class);
        checkClassMocking(ctrl, MockType.STRICT);
    }

    @Test
    public void testNiceClassMocking() {
        MockControl<ClassToMock> ctrl = MockClassControl
                .createNiceControl(ClassToMock.class);
        checkClassMocking(ctrl, MockType.NICE);
    }

    private void checkInterfaceMock(MockControl<?> ctrl, MockType behavior) {
        this.ctrl = ctrl;
        checkBehavior(ctrl, behavior);
    }

    @Test
    public void testPartialClassMocking() {
        MockControl<ClassToMock> ctrl = MockClassControl.createControl(
                ClassToMock.class, getMethod());
        checkPartialClassMocking(ctrl, MockType.DEFAULT);
    }

    @Test
    public void testStrictPartialClassMocking() {
        MockControl<ClassToMock> ctrl = MockClassControl.createStrictControl(
                ClassToMock.class, getMethod());
        checkPartialClassMocking(ctrl, MockType.STRICT);
    }

    @Test
    public void testNicePartialClassMocking() {
        MockControl<ClassToMock> ctrl = MockClassControl.createNiceControl(
                ClassToMock.class, getMethod());
        checkPartialClassMocking(ctrl, MockType.NICE);
    }

    @Test
    public void testDeprecatedClassMocking() {
        MockControl<ClassToMock> ctrl = MockClassControl.createControl(
                ClassToMock.class, null, null);
        checkClassMocking(ctrl, MockType.DEFAULT);
    }

    @Test
    public void testDeprecatedStrictClassMocking() {
        MockControl<ClassToMock> ctrl = MockClassControl.createStrictControl(
                ClassToMock.class, null, null);
        checkClassMocking(ctrl, MockType.STRICT);
    }

    @Test
    public void testDeprecatedNiceClassMocking() {
        MockControl<ClassToMock> ctrl = MockClassControl.createNiceControl(
                ClassToMock.class, null, null);
        checkClassMocking(ctrl, MockType.NICE);
    }

    @Test
    public void testDeprecatedPartialClassMocking() {
        MockControl<ClassToMock> ctrl = MockClassControl.createControl(
                ClassToMock.class, null, null, getMethod());
        checkPartialClassMocking(ctrl, MockType.DEFAULT);
    }

    @Test
    public void testDeprecatedStrictPartialClassMocking() {
        MockControl<ClassToMock> ctrl = MockClassControl.createStrictControl(
                ClassToMock.class, null, null, getMethod());
        checkPartialClassMocking(ctrl, MockType.STRICT);
    }

    @Test
    public void testDeprecatedNicePartialClassMocking() {
        MockControl<ClassToMock> ctrl = MockClassControl.createNiceControl(
                ClassToMock.class, null, null, getMethod());
        checkPartialClassMocking(ctrl, MockType.NICE);
    }

    private void checkPartialClassMocking(MockControl<ClassToMock> ctrl,
            MockType behavior) {
        checkClassMocking(ctrl, behavior);
        ClassToMock mock = ctrl.getMock();
        assertEquals(10, mock.foo());
        ctrl.expectAndReturn(mock.method(), 30);
        ctrl.replay();
        assertEquals(10, mock.foo());
        assertEquals(30, mock.method());
        ctrl.verify();
    }

    private void checkClassMocking(MockControl<ClassToMock> ctrl,
            MockType behavior) {
        assertTrue(ctrl instanceof MockClassControl);
        checkBehavior(ctrl, behavior);
    }

    private void checkBehavior(MockControl<?> ctrl, MockType behavior) {
        assertEquals(behavior, extractBehavior(ctrl));
    }

    private MockType extractBehavior(MockControl<?> ctrl) {
        try {
            // Get the MocksControl
            Field field = MockControl.class.getDeclaredField("ctrl");
            field.setAccessible(true);
            MocksControl mocksCtrl = (MocksControl) field.get(ctrl);
            field.setAccessible(false);

            // Get the MockType
            field = MocksControl.class.getDeclaredField("type");
            field.setAccessible(true);
            MockType type = (MockType) field.get(mocksCtrl);
            field.setAccessible(false);

            return type;
        } catch (Exception e) {
            throw new RuntimeException("Can't find behavior field");
        }
    }

    private Method[] getMethod() {
        try {
            return new Method[] { ClassToMock.class.getDeclaredMethod("method",
                    (Class[]) null) };
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
