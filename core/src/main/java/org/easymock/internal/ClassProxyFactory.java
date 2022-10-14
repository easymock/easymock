/*
 * Copyright 2001-2022 the original author or authors.
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

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.modifier.FieldManifestation;
import net.bytebuddy.description.modifier.SyntheticState;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.easymock.ConstructorArgs;

import java.io.IOException;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Factory generating a mock for a class.
 *
 * @author Henri Tremblay
 */
public class ClassProxyFactory implements IProxyFactory {

    public static class MockMethodInterceptor implements InvocationHandler, Serializable {

        private static final long serialVersionUID = -9054190871232972342L;

        private final InvocationHandler handler;

        public MockMethodInterceptor(InvocationHandler handler) {
            this.handler = handler;
        }

        @Override
        public Object invoke(Object obj, Method method, Object[] args) throws Throwable {

            // Here I need to check if the fillInStackTrace was called by EasyMock inner code
            // If it's the case, just ignore the call. We ignore it for two reasons
            // 1- In Java 7, the fillInStackTrace won't work because, since no constructor was called, the stackTrace attribute is null
            // 2- There might be some unexpected side effect in the original fillInStackTrace. So it seems more logical to ignore the call
            if (obj instanceof Throwable && method.getName().equals("fillInStackTrace")) {
                if(isCallerMockInvocationHandlerInvoke(new Throwable())) {
                    return obj;
                }
            }

            // Bridges should delegate to their bridged method. It should be done before
            // checking for mocked methods because only unbridged method are mocked
            // It also make sure the method passed to the handler is not the bridge. Normally it
            // shouldn't be necessary because bridges are never mocked so are never in the mockedMethods
            // map. So the normal case is that it will call invokeSuper which will call the interceptor for
            // the bridged method. The problem is that it doesn't happen. It looks like a cglib bug. For
            // package scoped bridges (see GenericTest), the interceptor is not called for the bridged
            // method. Not normal from my point of view.
            if (method.isBridge()) {
                method = BridgeMethodResolver.findBridgedMethod(method);
            }

            return handler.invoke(obj, method, args);
        }

    }

    private static final AtomicInteger id = new AtomicInteger(0);

    public static boolean isCallerMockInvocationHandlerInvoke(Throwable e) {
        StackTraceElement[] elements = e.getStackTrace();
        return elements.length > 2
                && elements[2].getClassName().equals(MockInvocationHandler.class.getName())
                && elements[2].getMethodName().equals("invoke");
    }

    @SuppressWarnings("unchecked")
    public <T> T createProxy(final Class<T> toMock, InvocationHandler handler,
            Method[] mockedMethods, ConstructorArgs args) {

        ElementMatcher.Junction<MethodDescription> junction;
        if (mockedMethods == null) {
            junction = ElementMatchers.any();
        } else {
            junction = ElementMatchers.anyOf(mockedMethods)
                .or(ElementMatchers.isAbstract()); // We conveniently mock abstract methods be default
        }

        Class<?> mockClass;
        try(DynamicType.Unloaded<T> unloaded = new ByteBuddy()
            .subclass(toMock)
            .name(classPackage(toMock) + toMock.getSimpleName() + "$$$EasyMock$" + id.incrementAndGet())
            .defineField("$callback", InvocationHandler.class, SyntheticState.SYNTHETIC, Visibility.PRIVATE, FieldManifestation.FINAL)
            .method(junction)
            .intercept(InvocationHandlerAdapter.of(new MockMethodInterceptor(handler)))
            .make()) {
            ClassLoader classLoader = classLoader(toMock);

            mockClass = unloaded
                .load(classLoader, classLoadingStrategy())
                .getLoaded();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        T mock;

        if (args != null) {
            // Really instantiate the class
            Constructor<?> cstr;
            try {
                // Get the constructor with the same params
                cstr = mockClass.getDeclaredConstructor(args.getConstructor().getParameterTypes());
            } catch (NoSuchMethodException e) {
                // Shouldn't happen, constructor is checked when ConstructorArgs is instantiated
                // ///CLOVER:OFF
                throw new RuntimeException("Fail to find constructor for param types", e);
                // ///CLOVER:ON
            }
            try {
                cstr.setAccessible(true); // So we can call a protected
                // constructor
                mock = (T) cstr.newInstance(args.getInitArgs());
            } catch (InstantiationException | IllegalAccessException e) {
                // ///CLOVER:OFF
                throw new RuntimeException("Failed to instantiate mock calling constructor", e);
                // ///CLOVER:ON
            } catch (InvocationTargetException e) {
                throw new RuntimeException(
                        "Failed to instantiate mock calling constructor: Exception in constructor",
                        e.getTargetException());
            }
        } else {
            // Do not call any constructor
            try {
                mock = (T) ClassInstantiatorFactory.getInstantiator().newInstance(mockClass);
            } catch (InstantiationException e) {
                // ///CLOVER:OFF
                throw new RuntimeException("Fail to instantiate mock for " + toMock + " on "
                        + ClassInstantiatorFactory.getJVM() + " JVM");
                // ///CLOVER:ON
            }
        }

        Field callbackField = getCallbackField(mock);
        try {
            callbackField.set(mock, handler);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return mock;
    }

    private String classPackage(Class<?> toMock) {
        // We want to create the mock in the same class as the original class when the class is in default scope
        return (toMock.getModifiers() & (Modifier.PRIVATE | Modifier.PUBLIC | Modifier.PROTECTED)) == 0 ? toMock.getPackage().getName() + "." : "";
    }

    private <T> ClassLoader classLoader(Class<T> toMock) {
        return toMock.getClassLoader();
    }

    private ClassLoadingStrategy<ClassLoader> classLoadingStrategy() {
        if (ClassInjector.UsingUnsafe.isAvailable()) {
            return new ClassLoadingStrategy.ForUnsafeInjection();
        }
        // I don't think this helps much. It was an attempt to help OSGi, but it doesn't work.
        // Right now, everything is using Unsafe to we never get there
        return ClassLoadingStrategy.UsingLookup.of(MethodHandles.lookup());
    }

    public InvocationHandler getInvocationHandler(Object mock) {
        Field field = getCallbackField(mock);
        try {
            return (InvocationHandler) field.get(mock);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Field getCallbackField(Object mock) {

        Field field;
        try {
            field = mock.getClass().getDeclaredField("$callback");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(true);
        return field;
    }
}
