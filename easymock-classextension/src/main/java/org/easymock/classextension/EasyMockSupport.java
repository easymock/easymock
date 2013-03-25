/**
 * Copyright 2003-2013 the original author or authors.
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

import java.lang.reflect.Method;

/**
 * This class is provided solely to allow an easier transition to EasyMock 3.0.
 * You should now use {@link org.easymock.EasyMockSupport} for classes and
 * interfaces mocking.
 * 
 * @author Henri Tremblay
 * 
 * @deprecated You can use {@link org.easymock.EasyMockSupport} even for class
 *             mocking
 */
@Deprecated
public class EasyMockSupport extends org.easymock.EasyMockSupport {

    /**
     * Creates a mock object that implements the given class, order checking is
     * enabled by default.
     * 
     * @param <T>
     *            the interface that the mock object should implement.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createStrictMock(Class<T> toMock, Method... mockedMethods) {
        return createStrictControl().createMock(toMock, mockedMethods);
    }

    /**
     * Creates a mock object that implements the given class, order checking is
     * enabled by default.
     * 
     * @param <T>
     *            the interface that the mock object should implement.
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createStrictMock(String name, Class<T> toMock,
            Method... mockedMethods) {
        return createStrictControl().createMock(name, toMock, mockedMethods);
    }

    /**
     * Creates a mock object that implements the given class, order checking is
     * enabled by default.
     * 
     * @param <T>
     *            the interface that the mock object should implement.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @param constructorArgs
     *            constructor and parameters used to instantiate the mock.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createStrictMock(Class<T> toMock,
            ConstructorArgs constructorArgs, Method... mockedMethods) {
        return createStrictControl().createMock(toMock, constructorArgs,
                mockedMethods);
    }

    /**
     * Creates a mock object that implements the given class, order checking is
     * enabled by default.
     * 
     * @param <T>
     *            the interface that the mock object should implement.
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @param constructorArgs
     *            constructor and parameters used to instantiate the mock.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createStrictMock(String name, Class<T> toMock,
            ConstructorArgs constructorArgs, Method... mockedMethods) {
        return createStrictControl().createMock(name, toMock, constructorArgs,
                mockedMethods);
    }

    /**
     * Creates a mock object that implements the given class, order checking is
     * disabled by default.
     * 
     * @param <T>
     *            the interface that the mock object should implement.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createMock(Class<T> toMock, Method... mockedMethods) {
        return createControl().createMock(toMock, mockedMethods);
    }

    /**
     * Creates a mock object that implements the given class, order checking is
     * disabled by default.
     * 
     * @param <T>
     *            the interface that the mock object should implement.
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createMock(String name, Class<T> toMock,
            Method... mockedMethods) {
        return createControl().createMock(name, toMock, mockedMethods);
    }

    /**
     * Creates a mock object that implements the given class, order checking is
     * disabled by default.
     * 
     * @param <T>
     *            the interface that the mock object should implement.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @param constructorArgs
     *            constructor and parameters used to instantiate the mock.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createMock(Class<T> toMock, ConstructorArgs constructorArgs,
            Method... mockedMethods) {
        return createControl().createMock(toMock, constructorArgs,
                mockedMethods);
    }

    /**
     * Creates a mock object that implements the given class, order checking is
     * disabled by default.
     * 
     * @param <T>
     *            the interface that the mock object should implement.
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @param constructorArgs
     *            constructor and parameters used to instantiate the mock.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createMock(String name, Class<T> toMock,
            ConstructorArgs constructorArgs, Method... mockedMethods) {
        return createControl().createMock(name, toMock, constructorArgs,
                mockedMethods);
    }

    /**
     * Creates a mock object that implements the given class, order checking is
     * disabled by default, and the mock object will return <code>0</code>,
     * <code>null</code> or <code>false</code> for unexpected invocations.
     * 
     * @param <T>
     *            the interface that the mock object should implement.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createNiceMock(Class<T> toMock, Method... mockedMethods) {
        return createNiceControl().createMock(toMock, mockedMethods);
    }

    /**
     * Creates a mock object that implements the given class, order checking is
     * disabled by default, and the mock object will return <code>0</code>,
     * <code>null</code> or <code>false</code> for unexpected invocations.
     * 
     * @param <T>
     *            the interface that the mock object should implement.
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createNiceMock(String name, Class<T> toMock,
            Method... mockedMethods) {
        return createNiceControl().createMock(name, toMock, mockedMethods);
    }

    /**
     * Creates a mock object that implements the given class, order checking is
     * disabled by default, and the mock object will return <code>0</code>,
     * <code>null</code> or <code>false</code> for unexpected invocations.
     * 
     * @param <T>
     *            the interface that the mock object should implement.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @param constructorArgs
     *            constructor and parameters used to instantiate the mock.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createNiceMock(Class<T> toMock,
            ConstructorArgs constructorArgs, Method... mockedMethods) {
        return createNiceControl().createMock(toMock, constructorArgs,
                mockedMethods);
    }

    /**
     * Creates a mock object that implements the given class, order checking is
     * disabled by default, and the mock object will return <code>0</code>,
     * <code>null</code> or <code>false</code> for unexpected invocations.
     * 
     * @param <T>
     *            the interface that the mock object should implement.
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class of the interface that the mock object should
     *            implement.
     * @param constructorArgs
     *            constructor and parameters used to instantiate the mock.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createNiceMock(String name, Class<T> toMock,
            ConstructorArgs constructorArgs, Method... mockedMethods) {
        return createNiceControl().createMock(name, toMock, constructorArgs,
                mockedMethods);
    }

    @Override
    public IMocksControl createStrictControl() {
        IMocksControl ctrl = EasyMock.createStrictControl();
        controls.add(ctrl);
        return ctrl;
    }

    @Override
    public IMocksControl createControl() {
        IMocksControl ctrl = EasyMock.createControl();
        controls.add(ctrl);
        return ctrl;
    }

    @Override
    public IMocksControl createNiceControl() {
        IMocksControl ctrl = EasyMock.createNiceControl();
        controls.add(ctrl);
        return ctrl;
    }

}
