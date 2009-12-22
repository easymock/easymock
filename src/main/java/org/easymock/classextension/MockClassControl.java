/**
 * Copyright 2003-2009 OFFIS, Henri Tremblay
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
package org.easymock.classextension;

import static org.easymock.internal.ClassExtensionHelper.*;

import java.lang.reflect.Method;

import org.easymock.MockControl;
import org.easymock.classextension.internal.MocksClassControl;

/**
 * Instances of <code>MockClassControl</code> control the behavior of their
 * associated mock objects. For more information, see the EasyMock
 * documentation.
 * 
 * @see <a href="http://www.easymock.org/">EasyMock</a>
 * @deprecated Use org.easymock.classextension.EasyMock instead
 */
@Deprecated
public class MockClassControl<T> extends MockControl<T> {

    private static final long serialVersionUID = -4329096516045908847L;

    /**
     * Creates a mock control object for the specified class or interface. The
     * {@link MockClassControl}and its associated mock object will not check
     * the order of expected method calls. An unexpected method call on the mock
     * object will lead to an <code>AssertionFailedError</code>.
     * 
     * @param classToMock
     *            the class to mock.
     * @return the mock control (which is a {@link MockClassControl}instance)
     */
    public static <T> MockControl<T> createControl(Class<T> classToMock) {
        return new MockClassControl<T>((MocksClassControl) EasyMock
                .createControl(), classToMock);
    }

    /**
     * Same as {@link #createControl(Class)}but allows to pass a list of
     * methods to mock. All the other methods won't be. It means that if these
     * methods are called, their real code will be executed.
     * 
     * @param classToMock
     *            the class to mock
     * @param mockedMethods
     *            Methods to be mocked. If null, all methods will be mocked.
     * @return the mock control
     */
    public static <T> MockClassControl<T> createControl(Class<T> classToMock,
            Method... mockedMethods) {
        return new MockClassControl<T>((MocksClassControl) EasyMock
                .createControl(), classToMock, mockedMethods);
    }

    /**
     * Creates a mock control object for the specified class or interface. The
     * {@link MockClassControl}and its associated mock object will check the
     * order of expected method calls. An unexpected method call on the mock
     * object will lead to an <code>AssertionFailedError</code>.
     * 
     * @param classToMock
     *            the class to mock.
     * @return the mock control (which is a {@link MockClassControl}instance)
     */
    public static <T> MockControl<T> createStrictControl(Class<T> classToMock) {
        return new MockClassControl<T>((MocksClassControl) EasyMock
                .createStrictControl(), classToMock);
    }

    /**
     * Same as {@link #createStrictControl(Class)}but allows to pass a list of
     * methods to mock. All the other methods won't be. It means that if these
     * methods are called, their real code will be executed.
     * 
     * @param classToMock
     *            the class to mock
     * @param mockedMethods
     *            Methods to be mocked. If null, all methods will be mocked.
     * @return the mock control
     */
    public static <T> MockClassControl<T> createStrictControl(
            Class<T> classToMock, Method... mockedMethods) {
        return new MockClassControl<T>((MocksClassControl) EasyMock
                .createStrictControl(), classToMock, mockedMethods);
    }

    /**
     * Creates a mock control object for the specified class or interface. The
     * {@link MockClassControl}and its associated mock object will check not
     * the order of expected method calls. An unexpected method call on the mock
     * object will return an empty value (0, null, false).
     * 
     * @param classToMock
     *            the class to mock.
     * @return the mock control (which is a {@link MockClassControl}instance)
     */
    public static <T> MockControl<T> createNiceControl(Class<T> classToMock) {
        return new MockClassControl<T>((MocksClassControl) EasyMock
                .createNiceControl(), classToMock);
    }

    /**
     * Same as {@link #createNiceControl(Class, Method[])}but allows to pass a
     * list of methods to mock. All the other methods won't be. It means that if
     * these methods are called, their real code will be executed.
     * 
     * @param classToMock
     *            the class to mock
     * @param mockedMethods
     *            Methods to be mocked. If null, all methods will be mocked.
     * @return the mock control
     */
    public static <T> MockClassControl<T> createNiceControl(
            Class<T> classToMock, Method... mockedMethods) {
        return new MockClassControl<T>((MocksClassControl) EasyMock
                .createNiceControl(), classToMock, mockedMethods);
    }

    /**
     * @deprecated No need to pick a constructor anymore. Constructor arguments
     *             are now ignored. Just use {@link #createControl(Class)}
     */
    @Deprecated
    public static <T> MockClassControl<T> createControl(Class<T> classToMock,
            Class<?>[] constructorTypes, Object[] constructorArgs) {
        return (MockClassControl<T>) createControl(classToMock);
    }

    /**
     * @deprecated No need to pick a constructor anymore. Constructor arguments
     *             are now ignored. Just use
     *             {@link #createControl(Class, Method[])}
     */
    @Deprecated
    public static <T> MockClassControl<T> createControl(Class<T> classToMock,
            Class<?>[] constructorTypes, Object[] constructorArgs,
            Method[] mockedMethods) {
        return createControl(classToMock, mockedMethods);
    }

    /**
     * @deprecated No need to pick a constructor anymore. Constructor arguments
     *             are now ignored. Just use {@link #createStrictControl(Class)}
     */
    @Deprecated
    public static <T> MockClassControl<T> createStrictControl(
            Class<T> classToMock, Class<?>[] constructorTypes,
            Object[] constructorArgs) {
        return (MockClassControl<T>) createStrictControl(classToMock);
    }

    /**
     * @deprecated No need to pick a constructor anymore. Constructor arguments
     *             are now ignored. Just use
     *             {@link #createStrictControl(Class, Method[])}
     */
    @Deprecated
    public static <T> MockClassControl<T> createStrictControl(
            Class<T> classToMock, Class<?>[] constructorTypes,
            Object[] constructorArgs, Method... mockedMethods) {
        return createStrictControl(classToMock, mockedMethods);
    }

    /**
     * @deprecated No need to pick a constructor anymore. Constructor arguments
     *             are now ignored. Just use {@link #createNiceControl(Class)}
     */
    @Deprecated
    public static <T> MockClassControl<T> createNiceControl(
            Class<T> classToMock, Class<?>[] constructorTypes,
            Object[] constructorArgs) {
        return (MockClassControl<T>) createNiceControl(classToMock);
    }

    /**
     * @deprecated No need to pick a constructor anymore. Constructor arguments
     *             are now ignored. Just use
     *             {@link #createNiceControl(Class, Method[])}
     */
    @Deprecated
    public static <T> MockClassControl<T> createNiceControl(
            Class<T> classToMock, Class<?>[] constructorTypes,
            Object[] constructorArgs, Method... mockedMethods) {
        return createNiceControl(classToMock, mockedMethods);
    }

    private MockClassControl(MocksClassControl ctrl, Class<T> classToMock,
            Method... mockedMethods) {
        super(ctrl, classToMock);
        // Set the mocked methods on the interceptor
        getInterceptor(getMock()).setMockedMethods(mockedMethods);
    }

    private MockClassControl(MocksClassControl ctrl, Class<T> classToMock) {
        super(ctrl, classToMock);
    }
}
