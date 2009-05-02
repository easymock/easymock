/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.InvocationHandler;

public interface IProxyFactory<T> {
    T createProxy(Class<T> toMock, InvocationHandler handler);
}
