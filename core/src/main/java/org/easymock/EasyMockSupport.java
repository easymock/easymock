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
package org.easymock;

import org.easymock.internal.Injector;
import org.easymock.internal.MockBuilder;
import org.easymock.internal.MocksControl;
import org.easymock.internal.ObjectMethodsFilter;
import org.easymock.internal.ReflectionUtils;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to keep track of mocks easily. See EasyMock
 * documentation and SupportTest sample.
 * <p>
 * Example of usage:
 *
 * <pre>
 * public class SupportTest extends EasyMockSupport {
 *     &#064;Test
 *     public void test() {
 *         firstMock = createMock(A.class);
 *         secondMock = createMock(B.class);
 *
 *         replayAll(); // put both mocks in replay mode
 *
 *         // ... use mocks ..
 *
 *         verifyAll(); // verify both mocks
 *     }
 * }
 * </pre>
 *
 * @author Henri Tremblay
 */
public class EasyMockSupport {

    /** List of all controls created */
    protected final List<IMocksControl> controls = new ArrayList<>(5);

    /**
     * Creates a mock object that implements the given interface, order checking
     * is disabled by default.
     *
     * @param <T>
     *            the interface that the mock object should implement.
     * @param <R>
     *            the returned type. In general T == R but when mocking a generic type, it won't so to be nice with the
     *            caller, we return a different type
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @return the mock object.
     *
     * @since ${project.version}
     */
    public <T, R> R mock(Class<T> toMock) {
        return createControl().createMock(toMock);
    }

    /**
     * Creates a mock object that implements the given interface, order checking
     * is disabled by default.
     *
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     *
     * @param <T>
     *            the interface that the mock object should implement.
     * @param <R>
     *            the returned type. In general T == R but when mocking a generic type, it won't so to be nice with the
     *            caller, we return a different type
     * @return the mock object.
     * @throws IllegalArgumentException
     *             if the name is not a valid Java identifier.
     *
     * @since ${project.version}
     */
    public <T, R> R mock(String name, Class<T> toMock) {
        return createControl().createMock(name, toMock);
    }

    /**
     * Creates a mock object of the requested type that implements the given interface or extends
     * the given class
     *
     * @param type
     *            the type of the mock to be created.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @param <T>
     *            the interface that the mock object should implement.
     * @param <R>
     *            the returned type. In general T == R but when mocking a generic type, it won't so to be nice with the
     *            caller, we return a different type
     * @return the mock object.
     * @throws IllegalArgumentException
     *             if the name is not a valid Java identifier.
     *
     * @since ${project.version}
     */
    public <T, R> R mock(MockType type, Class<T> toMock) {
        return createControl(type).createMock(toMock);
    }

    /**
     * Creates a mock object of the requested type that implements the given interface or extends
     * the given class
     *
     * @param name
     *            the name of the mock object.
     * @param type
     *            the type of the mock to be created.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @param <T>
     *            the interface that the mock object should implement.
     * @param <R>
     *            the returned type. In general T == R but when mocking a generic type, it won't so to be nice with the
     *            caller, we return a different type
     * @return the mock object.
     * @throws IllegalArgumentException
     *             if the name is not a valid Java identifier.
     *
     * @since ${project.version}
     */
    public <T, R> R mock(String name, MockType type, Class<T> toMock) {
        return createControl(type).createMock(name, toMock);
    }

    /**
     * Creates a mock object that implements the given interface, order checking
     * is enabled by default.
     *
     * @param <T>
     *            the interface that the mock object should implement.
     * @param <R>
     *            the returned type. In general T == R but when mocking a generic type, it won't so to be nice with the
     *            caller, we return a different type
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @return the mock object.
     *
     * @since ${project.version}
     */
    public <T, R> R strictMock(Class<T> toMock) {
        return createStrictControl().createMock(toMock);
    }

    /**
     * Creates a mock object that implements the given interface, order checking
     * is enabled by default.
     *
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @param <T>
     *            the interface that the mock object should implement.
     * @param <R>
     *            the returned type. In general T == R but when mocking a generic type, it won't so to be nice with the
     *            caller, we return a different type
     * @return the mock object.
     * @throws IllegalArgumentException
     *             if the name is not a valid Java identifier.
     *
     * @since ${project.version}
     */
    public <T, R> R strictMock(String name, Class<T> toMock) {
        return createStrictControl().createMock(name, toMock);
    }

    /**
     * Creates a mock object that implements the given interface, order checking
     * is disabled by default, and the mock object will return {@code 0},
     * {@code null} or {@code false} for unexpected invocations.
     *
     * @param <T>
     *            the interface that the mock object should implement.
     * @param <R>
     *            the returned type. In general T == R but when mocking a generic type, it won't so to be nice with the
     *            caller, we return a different type
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @return the mock object.
     *
     * @since ${project.version}
     */
    public <T, R> R niceMock(Class<T> toMock) {
        return createNiceControl().createMock(toMock);
    }

    /**
     * Creates a mock object that implements the given interface, order checking
     * is disabled by default, and the mock object will return {@code 0},
     * {@code null} or {@code false} for unexpected invocations.
     *
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     *
     * @param <T>
     *            the interface that the mock object should implement.
     * @param <R>
     *            the returned type. In general T == R but when mocking a generic type, it won't so to be nice with the
     *            caller, we return a different type
     * @return the mock object.
     * @throws IllegalArgumentException
     *             if the name is not a valid Java identifier.
     *
     * @since ${project.version}
     */
    public <T, R> R niceMock(String name, Class<T> toMock) {
        return createNiceControl().createMock(name, toMock);
    }

    /**
     * Create a mock builder allowing to create a partial mock for the given
     * class or interface.
     *
     * @param <T>
     *            the interface that the mock object should implement.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @return a mock builder to create a partial mock
     *
     * @since ${project.version}
     */
    public <T> IMockBuilder<T> partialMockBuilder(Class<T> toMock) {
        return new MockBuilder<>(toMock, this);
    }

    /**
     * Creates a mock object of the requested type that implements the given interface or extends
     * the given class
     *
     * @param type
     *            the type of the mock to be created.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @param <T>
     *            the interface that the mock object should implement.
     * @param <R>
     *            the returned type. In general T == R but when mocking a generic type, it won't so to be nice with the
     *            caller, we return a different type
     * @return the mock object.
     * @throws IllegalArgumentException
     *             if the name is not a valid Java identifier.
     */
    public <T, R> R createMock(MockType type, Class<T> toMock) {
        return mock(type, toMock);
    }

    /**
     * Creates a mock object of the requested type that implements the given interface or extends
     * the given class
     *
     * @param name
     *            the name of the mock object.
     * @param type
     *            the type of the mock to be created.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @param <T>
     *            the interface that the mock object should implement.
     * @param <R>
     *            the returned type. In general T == R but when mocking a generic type, it won't so to be nice with the
     *            caller, we return a different type
     * @return the mock object.
     * @throws IllegalArgumentException
     *             if the name is not a valid Java identifier.
     */
    public <T, R> R createMock(String name, MockType type, Class<T> toMock) {
        return mock(name, type, toMock);
    }

    /**
     * Creates a mock object that implements the given interface, order checking
     * is enabled by default.
     *
     * @param <T>
     *            the interface that the mock object should implement.
     * @param <R>
     *            the returned type. In general T == R but when mocking a generic type, it won't so to be nice with the
     *            caller, we return a different type
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @return the mock object.
     */
    public <T, R> R createStrictMock(Class<T> toMock) {
        return strictMock(toMock);
    }

    /**
     * Creates a mock object that implements the given interface, order checking
     * is enabled by default.
     *
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @param <T>
     *            the interface that the mock object should implement.
     * @param <R>
     *            the returned type. In general T == R but when mocking a generic type, it won't so to be nice with the
     *            caller, we return a different type
     * @return the mock object.
     * @throws IllegalArgumentException
     *             if the name is not a valid Java identifier.
     */
    public <T, R> R createStrictMock(String name, Class<T> toMock) {
        return strictMock(name, toMock);
    }

    /**
     * Creates a mock object that implements the given interface, order checking
     * is disabled by default.
     *
     * @param <T>
     *            the interface that the mock object should implement.
     * @param <R>
     *            the returned type. In general T == R but when mocking a generic type, it won't so to be nice with the
     *            caller, we return a different type
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @return the mock object.
     */
    public <T, R> R createMock(Class<T> toMock) {
        return mock(toMock);
    }

    /**
     * Creates a mock object that implements the given interface, order checking
     * is disabled by default.
     *
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @param <R>
     *            the returned type. In general T == R but when mocking a generic type, it won't so to be nice with the
     *            caller, we return a different type
     * @param <T>
     *            the interface that the mock object should implement.
     * @return the mock object.
     * @throws IllegalArgumentException
     *             if the name is not a valid Java identifier.
     */
    public <T, R> R createMock(String name, Class<T> toMock) {
        return mock(name, toMock);
    }

    /**
     * Creates a mock object that implements the given interface, order checking
     * is disabled by default, and the mock object will return {@code 0},
     * {@code null} or {@code false} for unexpected invocations.
     *
     * @param <T>
     *            the interface that the mock object should implement.
     * @param <R>
     *            the returned type. In general T == R but when mocking a generic type, it won't so to be nice with the
     *            caller, we return a different type
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @return the mock object.
     */
    public <T, R> R createNiceMock(Class<T> toMock) {
        return niceMock(toMock);
    }

    /**
     * Creates a mock object that implements the given interface, order checking
     * is disabled by default, and the mock object will return {@code 0},
     * {@code null} or {@code false} for unexpected invocations.
     *
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     *
     * @param <T>
     *            the interface that the mock object should implement.
     * @param <R>
     *            the returned type. In general T == R but when mocking a generic type, it won't so to be nice with the
     *            caller, we return a different type
     * @return the mock object.
     * @throws IllegalArgumentException
     *             if the name is not a valid Java identifier.
     */
    public <T, R> R createNiceMock(String name, Class<T> toMock) {
        return niceMock(name, toMock);
    }

    /**
     * Creates a control, order checking is enabled by default.
     *
     * @return the control.
     */
    public IMocksControl createStrictControl() {
        IMocksControl ctrl = EasyMock.createStrictControl();
        controls.add(ctrl);
        return ctrl;
    }

    /**
     * Create a mock builder allowing to create a partial mock for the given
     * class or interface.
     *
     * @param <T>
     *            the interface that the mock object should implement.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @return a mock builder to create a partial mock
     */
    public <T> IMockBuilder<T> createMockBuilder(Class<T> toMock) {
        return partialMockBuilder(toMock);
    }

    /**
     * Creates a control of the given type.
     *
     * @param type the mock type.
     * @return the control.
     * @since 3.2
     */
    public IMocksControl createControl(MockType type) {
        IMocksControl ctrl = EasyMock.createControl(type);
        controls.add(ctrl);
        return ctrl;
    }

    /**
     * Creates a control, order checking is disabled by default.
     *
     * @return the control.
     */
    public IMocksControl createControl() {
        return createControl(MockType.DEFAULT);
    }

    /**
     * Creates a control, order checking is disabled by default, and the mock
     * objects created by this control will return {@code 0},
     * {@code null} or {@code false} for unexpected invocations.
     *
     * @return the control.
     */
    public IMocksControl createNiceControl() {
        IMocksControl ctrl = EasyMock.createNiceControl();
        controls.add(ctrl);
        return ctrl;
    }

    /**
     * Switches all registered mock objects (more exactly: the controls of the
     * mock objects) to replay mode. For details, see the EasyMock
     * documentation.
     */
    public void replayAll() {
        for (IMocksControl c : controls) {
            c.replay();
        }
    }

    /**
     * Resets all registered mock objects (more exactly: the controls of the
     * mock objects). For details, see the EasyMock documentation.
     */
    public void resetAll() {
        for (IMocksControl c : controls) {
            c.reset();
        }
    }

    /**
     * Verifies all registered mock objects have their expectations met and that no
     * unexpected call was performed.
     * <p>
     * This method as same effect as calling {@link #verifyAllRecordings()}
     * followed by {@link #verifyAllUnexpectedCalls()}.
     */
    public void verifyAll() {
        for (IMocksControl c : controls) {
            c.verify();
        }
    }

    /**
     * Verifies all registered mock objects have their expectations met.
     *
     * @since 3.5
     */
    public void verifyAllRecordings() {
        for (IMocksControl c : controls) {
            c.verifyRecording();
        }
    }

    /**
     * Verifies that no registered mock objects had
     * unexpected calls.
     *
     * @since 3.5
     */
    public void verifyAllUnexpectedCalls() {
        for (IMocksControl c : controls) {
            c.verifyUnexpectedCalls();
        }
    }

    /**
     * Resets all registered mock objects (more exactly: the controls of the
     * mock objects) and turn them to a mock with nice behavior. For details,
     * see the EasyMock documentation.
     */
    public void resetAllToNice() {
        for (IMocksControl c : controls) {
            c.resetToNice();
        }
    }

    /**
     * Resets all registered mock objects (more exactly: the controls of the
     * mock objects) and turn them to a mock with default behavior. For details,
     * see the EasyMock documentation.
     */
    public void resetAllToDefault() {
        for (IMocksControl c : controls) {
            c.resetToDefault();
        }
    }

    /**
     * Resets all registered mock objects (more exactly: the controls of the
     * mock objects) and turn them to a mock with strict behavior. For details,
     * see the EasyMock documentation.
     */
    public void resetAllToStrict() {
        for (IMocksControl c : controls) {
            c.resetToStrict();
        }
    }

    /**
     * Inject a mock to every fields annotated with {@link Mock} on the class passed
     * in parameter. Then, inject these mocks to the fields of every class annotated with {@link TestSubject}.
     * <p>
     * The rules are
     * <ul>
     *     <li>Static and final fields are ignored</li>
     *     <li>If two mocks have the same field name, return an error</li>
     *     <li>If a mock has a field name and no matching field is found, return an error</li>
     * </ul>
     * Then, ignoring all fields and mocks matched by field name
     * <ul>
     *     <li>If a mock without field name can be assigned to a field, do it. The same mock can be assigned more than once</li>
     *     <li>If no mock can be assigned to a field, skip the field silently</li>
     *     <li>If the mock cannot be assigned to any field, skip the mock silently</li>
     *     <li>If two mocks can be assigned to the same field, return an error</li>
     * </ul>
     * Fields are searched recursively on the superclasses
     * <p>
     * <b>Note:</b> If the parameter extends {@code EasyMockSupport}, the mocks will be created using it to allow
     * {@code replayAll/verifyAll} to work afterwards
     * @param obj the object on which to inject mocks
     * @since 3.2
     */
    public static void injectMocks(Object obj) {
        Injector.injectMocks(obj);
    }

    /**
     * Will return the class that was mocked if it's a mock or {@code null} otherwise.
     *
     * @param possibleMock mock we want the type of
     * @param <T> type of the possible mock
     * @param <R> type of mocked class
     * @return the mocked type or null of not a mock
     * @since 3.5
     */
    public static <T,  R extends T> Class<R> getMockedClass(T possibleMock) {
        // Check that it is a real EasyMock mock
        if (!isAMock(possibleMock)) {
            return null;
        }
        return MocksControl.getMockedClass(possibleMock);
    }

    /**
     * Tells if this mock is an EasyMock mock.
     *
     * @param possibleMock the object that might be a mock
     * @return true if it's a mock
     */
    public static boolean isAMock(Object possibleMock) {
        if(possibleMock == null) {
            return false;
        }
        if (Proxy.isProxyClass(possibleMock.getClass())) {
            return (Proxy.getInvocationHandler(possibleMock) instanceof ObjectMethodsFilter);
        }
        else if(ReflectionUtils.isClassAvailable("net.bytebuddy.ByteBuddy")) {
            return ObjectMockingHelper.isAClassMock(possibleMock);
        }
        return false;
    }

    /**
     * Hides ByteBuddy classes from EasyMockSupport to prevent {@code NoClassDefFoundError} if
     * ByteBuddy isn't used.
     */
    private static class ObjectMockingHelper {
        public static boolean isAClassMock(Object possibleMock) {
            return possibleMock.getClass().getSimpleName().contains("$$$EasyMock$");
        }
    }
}
