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
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.sf.cglib.core.DefaultNamingPolicy;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import org.easymock.ConstructorArgs;

import java.lang.reflect.*;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.anyOf;

/**
 * Factory generating a mock for a class.
 *
 * @author Henri Tremblay
 */
public class ClassProxyFactory implements IProxyFactory {


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

        ElementMatcher.Junction<MethodDescription> junction;
        if (mockedMethods == null) {
            junction = any();
        } else {
            junction = anyOf(mockedMethods)
                .or(ElementMatchers.isAbstract()); // We conveniently mock abstract methods be default
        }

        Class<?> mockClass = new ByteBuddy()
            .subclass(toMock)
            .defineField("$callback", InvocationHandler.class, SyntheticState.SYNTHETIC, Visibility.PRIVATE, FieldManifestation.FINAL)
            .method(junction)
            .intercept(InvocationHandlerAdapter.of(handler))
            .make()
            .load(toMock.getClassLoader(), new ClassLoadingStrategy.ForUnsafeInjection())
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

        Field callbackField = getCallbackField(mock);
        try {
            callbackField.set(mock, handler);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return (T) mock;
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
