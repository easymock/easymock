/**
 * Copyright 2001-2011 the original author or authors.
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
import java.lang.reflect.*;
import java.lang.reflect.InvocationHandler;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.cglib.core.*;
import net.sf.cglib.proxy.*;

import org.easymock.ConstructorArgs;

/**
 * Factory generating a mock for a class.
 * <p>
 * Note that this class is stateful
 * 
 * @param <T>
 *            type of the proxy created
 * 
 * @author Henri Tremblay
 */
public class ClassProxyFactory<T> implements IProxyFactory<T> {

    public static class MockMethodInterceptor implements MethodInterceptor, Serializable {

        private static final long serialVersionUID = -9054190871232972342L;

        private final InvocationHandler handler;

        private transient Set<Method> mockedMethods;

        public MockMethodInterceptor(final InvocationHandler handler) {
            this.handler = handler;
        }

        public Object intercept(final Object obj, final Method method, final Object[] args,
                final MethodProxy proxy) throws Throwable {

            // Bridges should be called so they can forward to the real
            // method
            if (method.isBridge()) {
                final Method m = BridgeMethodResolver.findBridgedMethod(method);
                return handler.invoke(obj, m, args);
            }

            // We conveniently mock abstract methods be default
            if (Modifier.isAbstract(method.getModifiers())) {
                return handler.invoke(obj, method, args);
            }

            // Here I need to check if the fillInStackTrace was called by EasyMock inner code
            // If yes, invoke super. Otherwise, just behave normally
            if (obj instanceof Throwable && method.getName().equals("fillInStackTrace")) {
                final Exception e = new Exception();
                final StackTraceElement[] elements = e.getStackTrace();
                // ///CLOVER:OFF
                if (elements.length > 2) {
                    // ///CLOVER:ON    
                    final StackTraceElement element = elements[2];
                    if (element.getClassName().equals("org.easymock.internal.MockInvocationHandler")
                            && element.getMethodName().equals("invoke")) {
                        return proxy.invokeSuper(obj, args);
                    }
                }
            }

            if (mockedMethods != null && !mockedMethods.contains(method)) {
                return proxy.invokeSuper(obj, args);
            }

            return handler.invoke(obj, method, args);
        }

        public InvocationHandler getHandler() {
            return handler;
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

    @SuppressWarnings("unchecked")
    public T createProxy(final Class<T> toMock, final InvocationHandler handler) {

        // Dirty trick to fix ObjectMethodsFilter
        // It will replace the equals, hashCode, toString methods it kept that
        // are the ones
        // from Object.class by the correct ones since they might have been
        // overloaded
        // in the mocked class.
        try {
            updateMethod(handler, toMock.getMethod("equals", new Class[] { Object.class }));
            updateMethod(handler, toMock.getMethod("hashCode", new Class[0]));
            updateMethod(handler, toMock.getMethod("toString", new Class[0]));
        } catch (final NoSuchMethodException e) {
            // ///CLOVER:OFF
            throw new InternalError(
                    "We strangly failed to retrieve methods that always exist on an object...");
            // ///CLOVER:ON
        }

        final Enhancer enhancer = createEnhancer(toMock);

        final MethodInterceptor interceptor = new MockMethodInterceptor(handler);
        enhancer.setCallbackType(interceptor.getClass());

        Class mockClass;
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

        Enhancer.registerCallbacks(mockClass, new Callback[] { interceptor });

        if (ClassExtensionHelper.getCurrentConstructorArgs() != null) {
            // Really instantiate the class
            final ConstructorArgs args = ClassExtensionHelper.getCurrentConstructorArgs();
            Constructor cstr;
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
                        "Failed to instantiate mock calling constructor: Exception in constructor", e
                                .getTargetException());
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
    }

    private Enhancer createEnhancer(final Class<T> toMock) {
        // Create the mock
        final Enhancer enhancer = new Enhancer() {
            /**
             * Filter all private constructors but do not check that there are
             * some left
             */
            @SuppressWarnings("unchecked")
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

    private void updateMethod(final InvocationHandler objectMethodsFilter, final Method correctMethod) {
        final Field methodField = retrieveField(ObjectMethodsFilter.class, correctMethod.getName() + "Method");
        updateField(objectMethodsFilter, correctMethod, methodField);
    }

    private Field retrieveField(final Class<?> clazz, final String field) {
        try {
            return clazz.getDeclaredField(field);
        } catch (final NoSuchFieldException e) {
            // ///CLOVER:OFF
            throw new InternalError("There must be some refactoring because the " + field
                    + " field was there...");
            // ///CLOVER:ON
        }
    }

    private void updateField(final Object instance, final Object value, final Field field) {
        final boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(instance, value);
        } catch (final IllegalAccessException e) {
            // ///CLOVER:OFF
            throw new InternalError("Should be accessible since we set it ourselves");
            // ///CLOVER:ON
        }
        field.setAccessible(accessible);
    }
}
