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
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatcher;
import net.sf.cglib.core.CodeGenerationException;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.DefaultNamingPolicy;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import net.sf.cglib.core.VisibilityPredicate;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.easymock.ConstructorArgs;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.anyOf;

/**
 * Factory generating a mock for a class.
 *
 * @author Henri Tremblay
 */
public class ClassProxyFactory implements IProxyFactory {

    public static class MockMethodInterceptor implements MethodInterceptor, Serializable {

        private static final long serialVersionUID = -9054190871232972342L;

        private final InvocationHandler handler;

        private transient Set<Method> mockedMethods;

        public MockMethodInterceptor(InvocationHandler handler) {
            this.handler = handler;
        }

        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
                throws Throwable {

            // We conveniently mock abstract methods be default
            if (Modifier.isAbstract(method.getModifiers())) {
                return handler.invoke(obj, method, args);
            }

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
            // map. So the normal case is that is will call invokeSuper which will call the interceptor for
            // the bridged method. The problem is that it doesn't happen. It looks like a cglib bug. For
            // package scoped bridges (see GenericTest), the interceptor is not called for the bridged
            // method. Not normal from my point of view.
            if (method.isBridge()) {
                method = BridgeMethodResolver.findBridgedMethod(method);
            }

            if (mockedMethods != null && !mockedMethods.contains(method)) {
                return proxy.invokeSuper(obj, args);
            }

            return handler.invoke(obj, method, args);
        }

        public void setMockedMethods(Method... mockedMethods) {
            this.mockedMethods = new HashSet<>(Arrays.asList(mockedMethods));
        }

        @SuppressWarnings("unchecked")
        private void readObject(java.io.ObjectInputStream stream) throws IOException,
                ClassNotFoundException {
            stream.defaultReadObject();
            Set<MethodSerializationWrapper> methods = (Set<MethodSerializationWrapper>) stream
                    .readObject();
            if (methods == null) {
                return;
            }

            mockedMethods = new HashSet<>(methods.size());
            for (MethodSerializationWrapper m : methods) {
                try {
                    mockedMethods.add(m.getMethod());
                } catch (NoSuchMethodException e) {
                    // ///CLOVER:OFF
                    throw new IOException(e.toString());
                    // ///CLOVER:ON
                }
            }
        }

        private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();

            if (mockedMethods == null) {
                stream.writeObject(null);
                return;
            }

            Set<MethodSerializationWrapper> methods = new HashSet<>(
                mockedMethods.size());
            for (Method m : mockedMethods) {
                methods.add(new MethodSerializationWrapper(m));
            }

            stream.writeObject(methods);
        }
    }

    // ///CLOVER:OFF (I don't know how to test it automatically yet)
    private static final NamingPolicy ALLOWS_MOCKING_CLASSES_IN_SIGNED_PACKAGES = new DefaultNamingPolicy() {
        @Override
        public String getClassName(String prefix, String source, Object key,
                Predicate names) {
            return "codegen." + super.getClassName(prefix, source, key, names);
        }
    };

    // ///CLOVER:ON

    public static boolean isCallerMockInvocationHandlerInvoke(Throwable e) {
        StackTraceElement[] elements = e.getStackTrace();
        return elements.length > 2
                && elements[2].getClassName().equals(MockInvocationHandler.class.getName())
                && elements[2].getMethodName().equals("invoke");
    }

    @SuppressWarnings("unchecked")
    public <T> T createProxy(final Class<T> toMock, InvocationHandler handler,
            Method[] mockedMethods, ConstructorArgs args) {

        ElementMatcher.Junction<MethodDescription> junction = mockedMethods == null ? any() : anyOf(mockedMethods);

        Class<?> mockClass = new ByteBuddy()
            .subclass(toMock)
            .defineField("$callback", InvocationHandler.class, SyntheticState.SYNTHETIC, Visibility.PRIVATE, FieldManifestation.FINAL)
            .implement(Factory.class)
            .method(junction)
            .intercept(InvocationHandlerAdapter.of(handler))
            .make()
            .load(toMock.getClassLoader())
            .getLoaded();

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

        return (T) mock;
    }

    public InvocationHandler getInvocationHandler(Object mock) {
        Field field = null;
        try {
            field = mock.getClass().getDeclaredField("$callback");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(true);
        try {
            return (InvocationHandler) field.get(mock);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
