/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.classextension;

import java.lang.reflect.Method;

public interface IMocksControl extends org.easymock.IMocksControl {
    <T> T createMock(Class<T> toMock, Method... mockedMethods);

    <T> T createMock(Class<T> toMock, ConstructorArgs constructorArgs,
            Method... mockedMethods);

    <T> T createMock(String name, Class<T> toMock, Method... mockedMethods);

    <T> T createMock(String name, Class<T> toMock,
            ConstructorArgs constructorArgs, Method... mockedMethods);
}
