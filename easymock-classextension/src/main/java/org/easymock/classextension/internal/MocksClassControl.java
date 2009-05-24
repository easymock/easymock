/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.classextension.internal;

import static org.easymock.classextension.internal.ClassExtensionHelper.*;

import java.lang.reflect.Method;

import org.easymock.classextension.ConstructorArgs;
import org.easymock.classextension.IMocksControl;
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
