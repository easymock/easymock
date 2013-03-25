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

import org.easymock.ConstructorArgs;

/**
 * @author OFFIS, Tammo Freese
 */
public interface IProxyFactory {

    /**
     * @param toMock the class to mock by the factory
     * @param handler the handler that will be linked to the created proxy
     * @param mockedMethods the subset of {@code toMock}'s methods to mock, or
     *     null to mock all methods.
     * @param constructorArgs the constructor arguments to use, or null to use
     *     heuristics to choose a constructor.
     * @return the newly created proxy
     */
    <T> T createProxy(Class<T> toMock, InvocationHandler handler, Method[] mockedMethods,
            ConstructorArgs constructorArgs);

    /**
     * Returns the invocation handler for {@code mock};
     *
     * @param mock a mock instance previously returned by {@code createProxy}.
     * @return the handler handling method calls for the {@code mock}
     */
    InvocationHandler getInvocationHandler(Object mock);
}
