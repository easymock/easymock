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

import java.lang.reflect.Method;

import org.easymock.classextension.internal.MockBuilder;

/**
 * Helper class to be used to keep tracks of mocks easily. Same as its base
 * class but supports class mocking
 */
public class EasyMockSupport extends org.easymock.EasyMockSupport {

    /**
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createStrictMock(Class<T> toMock, Method... mockedMethods) {
        return createStrictControl().createMock(toMock, mockedMethods);
    }

    /**
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createStrictMock(String name, Class<T> toMock,
            Method... mockedMethods) {
        return createStrictControl().createMock(name, toMock, mockedMethods);
    }

    /**
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createStrictMock(Class<T> toMock,
            ConstructorArgs constructorArgs, Method... mockedMethods) {
        return createStrictControl().createMock(toMock, constructorArgs,
                mockedMethods);
    }

    /**
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createStrictMock(String name, Class<T> toMock,
            ConstructorArgs constructorArgs, Method... mockedMethods) {
        return createStrictControl().createMock(name, toMock, constructorArgs,
                mockedMethods);
    }

    /**
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createMock(Class<T> toMock, Method... mockedMethods) {
        return createControl().createMock(toMock, mockedMethods);
    }

    /**
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createMock(String name, Class<T> toMock,
            Method... mockedMethods) {
        return createControl().createMock(name, toMock, mockedMethods);
    }

    /**
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createMock(Class<T> toMock, ConstructorArgs constructorArgs,
            Method... mockedMethods) {
        return createControl().createMock(toMock, constructorArgs,
                mockedMethods);
    }

    /**
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createMock(String name, Class<T> toMock,
            ConstructorArgs constructorArgs, Method... mockedMethods) {
        return createControl().createMock(name, toMock, constructorArgs,
                mockedMethods);
    }

    /**
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createNiceMock(Class<T> toMock, Method... mockedMethods) {
        return createNiceControl().createMock(toMock, mockedMethods);
    }

    /**
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createNiceMock(String name, Class<T> toMock,
            Method... mockedMethods) {
        return createNiceControl().createMock(name, toMock, mockedMethods);
    }

    /**
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public <T> T createNiceMock(Class<T> toMock,
            ConstructorArgs constructorArgs, Method... mockedMethods) {
        return createNiceControl().createMock(toMock, constructorArgs,
                mockedMethods);
    }

    /**
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

    public <T> IMockBuilder<T> createMockBuilder(Class<T> toMock) {
        return new MockBuilder<T>(toMock, controls);
    }

}
