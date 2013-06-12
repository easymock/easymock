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
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.ConstructorArgs;

import net.sf.cglib.core.CodeGenerationException;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.DefaultNamingPolicy;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import net.sf.cglib.core.VisibilityPredicate;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

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

        public MockMethodInterceptor(final InvocationHandler handler) {
            this.handler = handler;
        }

        public Object intercept(final Object obj, Method method, final Object[] args, final MethodProxy proxy)
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

        public void setMockedMethods(final Method... mockedMethods) {
            this.mockedMethods = new HashSet<Method>(Arrays.asList(mockedMethods));
        }

        @SuppressWarnings("unchecked")
        private void readObject(final java.io.ObjectInputStream stream) throws IOException,
                ClassNotFoundException {
            stream.defaultReadObject();
            final Set<MethodSerializationWrapper> methods = (Set<MethodSerializationWrapper>) stream
                    .readObject();
            if (methods == null) {
                return;
            }

            mockedMethods = new HashSet<Method>(methods.size());
            for (final MethodSerializationWrapper m : methods) {
                try {
                    mockedMethods.add(m.getMethod());
                } catch (final NoSuchMethodException e) {
                    // ///CLOVER:OFF
                    throw new IOException(e.toString());
                    // ///CLOVER:ON
                }
            }
        }

        private void writeObject(final java.io.ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();

            if (mockedMethods == null) {
                stream.writeObject(null);
                return;
            }

            final Set<MethodSerializationWrapper> methods = new HashSet<MethodSerializationWrapper>(
                    mockedMethods.size());
            for (final Method m : mockedMethods) {
                methods.add(new MethodSerializationWrapper(m));
            }

            stream.writeObject(methods);
        }
    }

    // ///CLOVER:OFF (I don't know how to test it automatically yet)
    private static final NamingPolicy ALLOWS_MOCKING_CLASSES_IN_SIGNED_PACKAGES = new DefaultNamingPolicy() {
        @Override
        public String getClassName(final String prefix, final String source, final Object key,
                final Predicate names) {
            return "codegen." + super.getClassName(prefix, source, key, names);
        }
    };

    // ///CLOVER:ON

    public static boolean isCallerMockInvocationHandlerInvoke(final Throwable e) throws Throwable {
        final StackTraceElement[] elements = e.getStackTrace();
        return elements.length > 2
                && elements[2].getClassName().equals(MockInvocationHandler.class.getName())
                && elements[2].getMethodName().equals("invoke");
    }

    @SuppressWarnings("unchecked")
    public <T> T createProxy(final Class<T> toMock, final InvocationHandler handler,
            final Method[] mockedMethods, final ConstructorArgs args) {
        final Enhancer enhancer = createEnhancer(toMock);

        final MockMethodInterceptor interceptor = new MockMethodInterceptor(handler);
        if (mockedMethods != null) {
            interceptor.setMockedMethods(mockedMethods);
        }
        enhancer.setCallbackType(interceptor.getClass());

        Class<?> mockClass;
        try {
            mockClass = enhancer.createClass();
        } catch (final CodeGenerationException e) {
            // ///CLOVER:OFF (don't know how to test it automatically)
            // Probably caused by a NoClassDefFoundError, let's try EasyMock class loader
            // instead of the default one (which is the class to mock one
            // This is required by Eclipse Plug-ins, the mock class loader doesn't see
            // cglib most of the time. Using EasyMock class loader solves this
            // See issue ID: 2994002
            enhancer.setClassLoader(getClass().getClassLoader());
            mockClass = enhancer.createClass();
            // ///CLOVER:ON
        }

        try {
            Enhancer.registerCallbacks(mockClass, new Callback[] { interceptor });

            if (args != null) {
                // Really instantiate the class
                Constructor<?> cstr;
                try {
                    // Get the constructor with the same params
                    cstr = mockClass.getDeclaredConstructor(args.getConstructor().getParameterTypes());
                } catch (final NoSuchMethodException e) {
                    // Shouldn't happen, constructor is checked when ConstructorArgs is instantiated
                    // ///CLOVER:OFF
                    throw new RuntimeException("Fail to find constructor for param types", e);
                    // ///CLOVER:ON
                }
                T mock;
                try {
                    cstr.setAccessible(true); // So we can call a protected
                    // constructor
                    mock = (T) cstr.newInstance(args.getInitArgs());
                } catch (final InstantiationException e) {
                    // ///CLOVER:OFF
                    throw new RuntimeException("Failed to instantiate mock calling constructor", e);
                    // ///CLOVER:ON
                } catch (final IllegalAccessException e) {
                    // ///CLOVER:OFF
                    throw new RuntimeException("Failed to instantiate mock calling constructor", e);
                    // ///CLOVER:ON
                } catch (final InvocationTargetException e) {
                    throw new RuntimeException(
                            "Failed to instantiate mock calling constructor: Exception in constructor",
                            e.getTargetException());
                }
                return mock;
            } else {
                // Do not call any constructor

                Factory mock;
                try {
                    mock = (Factory) ClassInstantiatorFactory.getInstantiator().newInstance(mockClass);
                } catch (final InstantiationException e) {
                    // ///CLOVER:OFF
                    throw new RuntimeException("Fail to instantiate mock for " + toMock + " on "
                            + ClassInstantiatorFactory.getJVM() + " JVM");
                    // ///CLOVER:ON
                }

                // This call is required. CGlib has some "magic code" making sure a
                // callback is used by only one instance of a given class. So only
                // the
                // instance created right after registering the callback will get
                // it.
                // However, this is done in the constructor which I'm bypassing to
                // allow class instantiation without calling a constructor.
                // Fortunately, the "magic code" is also called in getCallback which
                // is
                // why I'm calling it here mock.getCallback(0);
                mock.getCallback(0);

                return (T) mock;
            }
        } finally {
            // To avoid CGLib out of memory issues
            Enhancer.registerCallbacks(mockClass, null);
        }
    }

    private Enhancer createEnhancer(final Class<?> toMock) {
        // Create the mock
        final Enhancer enhancer = new Enhancer() {

            /**
             * Filter all private constructors but do not check that there are
             * some left
             */
            @SuppressWarnings("rawtypes")
            @Override
            protected void filterConstructors(final Class sc, final List constructors) {
                CollectionUtils.filter(constructors, new VisibilityPredicate(sc, true));
            }
        };
        enhancer.setSuperclass(toMock);

        // ///CLOVER:OFF (I don't know how to test it automatically yet)
        // See issue ID: 2994002
        if (toMock.getSigners() != null) {
            enhancer.setNamingPolicy(ALLOWS_MOCKING_CLASSES_IN_SIGNED_PACKAGES);
        }
        // ///CLOVER:ON

        return enhancer;
    }

    public InvocationHandler getInvocationHandler(final Object mock) {
        final Factory factory = (Factory) mock;
        return ((MockMethodInterceptor) factory.getCallback(0)).handler;
    }
}
