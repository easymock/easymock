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
package org.easymock.classextension.internal;

import static org.easymock.internal.ClassExtensionHelper.*;

import java.lang.reflect.Method;

import org.easymock.ConstructorArgs;
import org.easymock.classextension.IMocksControl;
import org.easymock.internal.ClassProxyFactory;
import org.easymock.internal.IProxyFactory;
import org.easymock.internal.MocksControl;

public class MocksClassControl extends MocksControl implements IMocksControl {

    private static final long serialVersionUID = -6032144451192179422L;

    public MocksClassControl(MockType type) {
        super(type);
    }

    public <T> T createMock(String name, Class<T> toMock,
            Method... mockedMethods) {
        
        if(toMock.isInterface()) {
            throw new IllegalArgumentException("Partial mocking doesn't make sense for interface");
        }
        
        T mock = createMock(name, toMock);

        // Set the mocked methods on the interceptor
        getInterceptor(mock).setMockedMethods(mockedMethods);

        return mock;
    }

    public <T> T createMock(Class<T> toMock, Method... mockedMethods) {
        
        if(toMock.isInterface()) {
            throw new IllegalArgumentException("Partial mocking doesn't make sense for interface");
        }
        
        T mock = createMock(toMock);

        // Set the mocked methods on the interceptor
        getInterceptor(mock).setMockedMethods(mockedMethods);

        return mock;
    }

    public <T> T createMock(Class<T> toMock, ConstructorArgs constructorArgs,
            Method... mockedMethods) {
        // Trick to allow the ClassProxyFactory to access constructor args
        setCurrentConstructorArgs(constructorArgs);
        try {
            return createMock(toMock, mockedMethods);
        } finally {
            setCurrentConstructorArgs(null);
        }
    }

    public <T> T createMock(String name, Class<T> toMock,
            ConstructorArgs constructorArgs, Method... mockedMethods) {
        // Trick to allow the ClassProxyFactory to access constructor args
        setCurrentConstructorArgs(constructorArgs);
        try {
            return createMock(name, toMock, mockedMethods);
        } finally {
            setCurrentConstructorArgs(null);
        }
    }

    @Override
    protected <T> IProxyFactory<T> createProxyFactory(Class<T> toMock) {
        if (toMock.isInterface()) {
            return super.createProxyFactory(toMock);
        }
        return new ClassProxyFactory<T>();
    }
}
