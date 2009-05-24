/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
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
