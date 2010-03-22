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

import java.lang.reflect.Proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;

import org.easymock.classextension.ConstructorArgs;
import org.easymock.classextension.internal.ClassProxyFactory.MockMethodInterceptor;
import org.easymock.internal.MocksControl;
import org.easymock.internal.ObjectMethodsFilter;

public final class ClassExtensionHelper {

    private static final ThreadLocal<ConstructorArgs> currentConstructorArgs = new ThreadLocal<ConstructorArgs>();

    // ///CLOVER:OFF
    private ClassExtensionHelper() {
    }

    // ///CLOVER:ON

    public static void setCurrentConstructorArgs(ConstructorArgs args) {
        currentConstructorArgs.set(args);
    }

    public static ConstructorArgs getCurrentConstructorArgs() {
        return currentConstructorArgs.get();
    }

    public static MockMethodInterceptor getInterceptor(Object mock) {
        Factory factory = (Factory) mock;
        return (MockMethodInterceptor) factory.getCallback(0);
    }

    public static MocksControl getControl(Object mock) {
        ObjectMethodsFilter handler;

        try {
            if (Enhancer.isEnhanced(mock.getClass())) {
                handler = (ObjectMethodsFilter) getInterceptor(mock)
                        .getHandler();
            } else if (Proxy.isProxyClass(mock.getClass())) {
                handler = (ObjectMethodsFilter) Proxy
                        .getInvocationHandler(mock);
            } else {
                throw new IllegalArgumentException(
                        "Not a mock: " + mock.getClass().getName());
            }
            return handler.getDelegate().getControl();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(
                    "Not a mock: " + mock.getClass().getName());
        }
    }
}
