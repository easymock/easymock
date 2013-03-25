/**
 * Copyright 2001-2013 the original author or authors.
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
package org.easymock.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.easymock.ConstructorArgs;

/**
 * @author OFFIS, Tammo Freese
 */
public class JavaProxyFactory implements IProxyFactory {
    @SuppressWarnings("unchecked")
    public <T> T createProxy(Class<T> toMock, InvocationHandler handler,
            Method[] mockedMethods, ConstructorArgs constructorArgs) {
        return (T) Proxy.newProxyInstance(toMock.getClassLoader(), new Class[] { toMock }, handler);
    }

    public InvocationHandler getInvocationHandler(Object mock) {
        return Proxy.getInvocationHandler(mock);
    }
}
