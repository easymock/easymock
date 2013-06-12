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

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.easymock.ConstructorArgs;

import com.google.dexmaker.stock.ProxyBuilder;

// ///CLOVER:OFF (sadly not possible to test android with clover)
/**
 * Mocks concrete classes for Android's runtime by generating dex files.
 */
public final class AndroidClassProxyFactory implements IProxyFactory {
    public <T> T createProxy(Class<T> toMock, InvocationHandler handler,
            Method[] mockedMethods, ConstructorArgs constructorArgs) {
        final MockHandler interceptor = new MockHandler(handler, mockedMethods);
        try {
            ProxyBuilder<T> builder = ProxyBuilder.forClass(toMock)
                    .handler(interceptor);
            if (constructorArgs != null) {
                builder.constructorArgTypes(constructorArgs.getConstructor().getParameterTypes())
                        .constructorArgValues(constructorArgs.getInitArgs());
            } else {
                try {
                    DefaultClassInstantiator instantiator = new DefaultClassInstantiator();
                    Constructor<?> constructor = instantiator.getConstructorToUse(toMock);
                    Object[] params = instantiator.getArgsForTypes(constructor.getParameterTypes());
                    builder.constructorArgTypes(constructor.getParameterTypes())
                            .constructorArgValues(params);
                } catch (InstantiationException e) {
                    throw new RuntimeException("Fail to instantiate mock for " + toMock);
                }
            }
            return builder.build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to mock " + toMock, e);
        }
    }

    public InvocationHandler getInvocationHandler(Object mock) {
        MockHandler mockHandler = (MockHandler) ProxyBuilder.getInvocationHandler(mock);
        return mockHandler.delegate;
    }

    private static class MockHandler implements InvocationHandler {
        private final InvocationHandler delegate;
        private final Set<Method> mockedMethods;

        public MockHandler(InvocationHandler delegate, Method... mockedMethods) {
            this.delegate = delegate;
            this.mockedMethods = (mockedMethods != null)
                    ? new HashSet<Method>(Arrays.asList(mockedMethods))
                    : null;
        }

        public Object invoke(Object obj, Method method, Object[] args) throws Throwable {
            if (method.isBridge()) {
                method = BridgeMethodResolver.findBridgedMethod(method);
            }

            // Never intercept EasyMock's own calls to fillInStackTrace
            boolean internalFillInStackTraceCall = obj instanceof Throwable
                    && method.getName().equals("fillInStackTrace")
                    && ClassProxyFactory.isCallerMockInvocationHandlerInvoke(new Throwable());

            if (internalFillInStackTraceCall
                    || isMocked(method) && !Modifier.isAbstract(method.getModifiers())) {
                return ProxyBuilder.callSuper(obj, method, args);
            }

            return delegate.invoke(obj, method, args);
        }

        private boolean isMocked(Method method) {
            return mockedMethods != null && !mockedMethods.contains(method);
        }
    }
}
// ///CLOVER:ON