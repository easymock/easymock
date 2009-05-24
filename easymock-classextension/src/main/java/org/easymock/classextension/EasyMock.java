/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.classextension;

import java.lang.reflect.Method;

import org.easymock.classextension.internal.ClassExtensionHelper;
import org.easymock.classextension.internal.MocksClassControl;
import org.easymock.internal.MocksControl;

public class EasyMock extends org.easymock.EasyMock {

    public static <T> T createStrictMock(Class<T> toMock) {
        return createStrictControl().createMock(toMock);
    }

    public static <T> T createStrictMock(String name, Class<T> toMock) {
        return createStrictControl().createMock(name, toMock);
    }

    public static <T> T createStrictMock(Class<T> toMock,
            Method... mockedMethods) {
        return createStrictControl().createMock(toMock, mockedMethods);
    }

    public static <T> T createStrictMock(String name, Class<T> toMock,
            Method... mockedMethods) {
        return createStrictControl().createMock(name, toMock, mockedMethods);
    }

    public static <T> T createStrictMock(Class<T> toMock,
            ConstructorArgs constructorArgs, Method... mockedMethods) {
        return createStrictControl().createMock(toMock, constructorArgs,
                mockedMethods);
    }

    public static <T> T createStrictMock(String name, Class<T> toMock,
            ConstructorArgs constructorArgs, Method... mockedMethods) {
        return createStrictControl().createMock(name, toMock, constructorArgs,
                mockedMethods);
    }

    public static <T> T createMock(Class<T> toMock) {
        return createControl().createMock(toMock);
    }

    public static <T> T createMock(String name, Class<T> toMock) {
        return createControl().createMock(name, toMock);
    }

    public static <T> T createMock(Class<T> toMock, Method... mockedMethods) {
        return createControl().createMock(toMock, mockedMethods);
    }

    public static <T> T createMock(String name, Class<T> toMock,
            Method... mockedMethods) {
        return createControl().createMock(name, toMock, mockedMethods);
    }

    public static <T> T createMock(Class<T> toMock,
            ConstructorArgs constructorArgs, Method... mockedMethods) {
        return createControl().createMock(toMock, constructorArgs,
                mockedMethods);
    }

    public static <T> T createMock(String name, Class<T> toMock,
            ConstructorArgs constructorArgs, Method... mockedMethods) {
        return createControl().createMock(name, toMock, constructorArgs,
                mockedMethods);
    }

    public static <T> T createNiceMock(Class<T> toMock) {
        return createNiceControl().createMock(toMock);
    }

    public static <T> T createNiceMock(String name, Class<T> toMock) {
        return createNiceControl().createMock(name, toMock);
    }

    public static <T> T createNiceMock(Class<T> toMock, Method... mockedMethods) {
        return createNiceControl().createMock(toMock, mockedMethods);
    }

    public static <T> T createNiceMock(String name, Class<T> toMock,
            Method... mockedMethods) {
        return createNiceControl().createMock(name, toMock, mockedMethods);
    }

    public static <T> T createNiceMock(Class<T> toMock,
            ConstructorArgs constructorArgs, Method... mockedMethods) {
        return createNiceControl().createMock(toMock, constructorArgs,
                mockedMethods);
    }

    public static <T> T createNiceMock(String name, Class<T> toMock,
            ConstructorArgs constructorArgs, Method... mockedMethods) {
        return createNiceControl().createMock(name, toMock, constructorArgs,
                mockedMethods);
    }

    public static IMocksControl createStrictControl() {
        return new MocksClassControl(MocksControl.MockType.STRICT);
    }

    public static IMocksControl createControl() {
        return new MocksClassControl(MocksControl.MockType.DEFAULT);
    }

    public static IMocksControl createNiceControl() {
        return new MocksClassControl(MocksControl.MockType.NICE);
    }

    public static void replay(Object... mocks) {
        for (Object mock : mocks) {
            ClassExtensionHelper.getControl(mock).replay();
        }
    }

    public static void reset(Object... mocks) {
        for (Object mock : mocks) {
            ClassExtensionHelper.getControl(mock).reset();
        }
    }
    
    public static void resetToNice(Object... mocks) {
        for (Object mock : mocks) {
            ClassExtensionHelper.getControl(mock).resetToNice();
        }
    }

    public static void resetToDefault(Object... mocks) {
        for (Object mock : mocks) {
            ClassExtensionHelper.getControl(mock).resetToDefault();
        }
    }    

    public static void resetToStrict(Object... mocks) {
        for (Object mock : mocks) {
            ClassExtensionHelper.getControl(mock).resetToStrict();
        }
    }
    
    public static void verify(Object... mocks) {
        for (Object mock : mocks) {
            ClassExtensionHelper.getControl(mock).verify();
        }
    }

    public static void checkOrder(Object mock, boolean value) {
        ClassExtensionHelper.getControl(mock).checkOrder(value);
    }
    
    public static void makeThreadSafe(Object mock, boolean threadSafe) {
        ClassExtensionHelper.getControl(mock).makeThreadSafe(threadSafe);
    }

    // ///CLOVER:OFF
    /** Prevent instantiation but allow inheritance */
    protected EasyMock() {
    }
    // ///CLOVER:ON
}
