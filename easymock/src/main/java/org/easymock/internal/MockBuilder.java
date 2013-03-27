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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.easymock.*;

/**
 * Default implementation of IMockBuilder.
 * <p>
 * <i>The original idea and part of the code where contributed by Rodrigo
 * Damazio and Bruno Fonseca at <a href="http://www.google.com">Google</a></i>
 * 
 * @param <T>
 *            type of the mock created
 * 
 * @author Henri Tremblay
 */
public class MockBuilder<T> implements IMockBuilder<T> {

    private final Class<T> toMock;

    private Set<Method> mockedMethods;

    private Constructor<T> constructor;

    private ConstructorArgs constructorArgs;

    private final EasyMockSupport support;

    public MockBuilder(final Class<T> toMock) {
        this(toMock, null);
    }

    /**
     * Used by EasyMockSupport to allow the mock registration in the list of
     * controls
     * 
     * @param toMock
     *            The class of the mock to create
     * @param support
     *            The EasyMockSupport used to create mocks. Null if none
     */
    public MockBuilder(final Class<T> toMock, final EasyMockSupport support) {
        this.toMock = toMock;
        this.support = support;
    }

    public IMockBuilder<T> addMockedMethod(final Method method) {
        if (Modifier.isFinal(method.getModifiers())) {
            throw new IllegalArgumentException("Final methods can't be mocked");
        }
        if (mockedMethods == null) {
            mockedMethods = new HashSet<Method>();
        }
        mockedMethods.add(method);
        return this;
    }

    public IMockBuilder<T> addMockedMethod(final String methodName) {
        final Method m = ReflectionUtils.findMethod(toMock, methodName);
        if (m == null) {
            throw new IllegalArgumentException("Method not found (or private): " + methodName);
        }
        addMockedMethod(m);
        return this;
    }

    public IMockBuilder<T> addMockedMethod(final String methodName, final Class<?>... parameterTypes) {
        final Method m = ReflectionUtils.findMethod(toMock, methodName, parameterTypes);
        if (m == null) {
            throw new IllegalArgumentException("Method not found (or private): " + methodName);
        }
        addMockedMethod(m);
        return this;
    }

    public IMockBuilder<T> addMockedMethods(final String... methodNames) {
        for (final String methodName : methodNames) {
            addMockedMethod(methodName);
        }
        return this;
    }

    public IMockBuilder<T> addMockedMethods(final Method... methods) {
        for (final Method method : methods) {
            addMockedMethod(method);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public IMockBuilder<T> withConstructor(final Constructor<?> constructor) {
        checkConstructorNotInitialized();
        this.constructor = (Constructor<T>) constructor;
        return this;
    }

    @SuppressWarnings("unchecked")
    public IMockBuilder<T> withConstructor(final ConstructorArgs constructorArgs) {
        checkConstructorNotInitialized();
        this.constructor = (Constructor<T>) constructorArgs.getConstructor();
        this.constructorArgs = constructorArgs;
        return this;
    }

    public IMockBuilder<T> withConstructor() {
        checkConstructorNotInitialized();
        try {
            constructor = ReflectionUtils.getConstructor(toMock);
        } catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException("No empty constructor can be found", e);
        }
        constructorArgs = new ConstructorArgs(constructor);
        return this;
    }

    public IMockBuilder<T> withConstructor(final Object... initArgs) {
        checkConstructorNotInitialized();
        try {
            constructor = ReflectionUtils.getConstructor(toMock, initArgs);
        } catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException("No constructor matching arguments can be found", e);
        }
        constructorArgs = new ConstructorArgs(constructor, initArgs);
        return this;
    }

    public IMockBuilder<T> withConstructor(final Class<?>... argTypes) {
        checkConstructorNotInitialized();

        try {
            constructor = toMock.getDeclaredConstructor(argTypes);
        } catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException("No constructor matching arguments can be found", e);
        }
        return this;
    }

    public IMockBuilder<T> withArgs(final Object... initArgs) {
        if (constructor == null) {
            throw new IllegalStateException(
                    "Trying to define constructor arguments without first setting their type.");
        }
        if (constructorArgs != null) {
            throw new IllegalStateException("Trying to define the constructor arguments more than once.");
        }

        constructorArgs = new ConstructorArgs(constructor, initArgs);
        return this;
    }

    public T createMock(final MockType type) {
        final IMocksControl control = (support == null ? EasyMock.createControl(type) : support
                .createControl(type));
        return createMock(null, control);
    }

    public T createMock(final String name, final MockType type) {
        final IMocksControl control = (support == null ? EasyMock.createControl(type) : support
                .createControl(type));
        return createMock(name, control);
    }

    public T createMock(final IMocksControl control) {
        return createMock(null, control);
    }

    public T createMock() {
        return createMock((String) null);
    }

    public T createNiceMock() {
        return createNiceMock(null);
    }

    public T createStrictMock() {
        return createStrictMock(null);
    }

    @SuppressWarnings("deprecation")
    public T createMock(final String name, final IMocksControl control) {
        final Method[] mockedMethodArray = (mockedMethods == null ? new Method[0] : mockedMethods
                .toArray(new Method[mockedMethods.size()]));

        // Create a mock with no default {@code withConstructor} was not called.
        if (constructor == null) {
            return control.createMock(name, toMock, mockedMethodArray);
        }

        // If the constructor is defined, so must be its arguments
        if (constructorArgs == null) {
            throw new IllegalStateException("Picked a constructor but didn't pass arguments to it");
        }

        return control.createMock(name, toMock, constructorArgs, mockedMethodArray);
    }

    public T createMock(final String name) {
        final IMocksControl control = (support == null ? EasyMock.createControl() : support.createControl());
        return createMock(name, control);
    }

    public T createNiceMock(final String name) {
        final IMocksControl control = (support == null ? EasyMock.createNiceControl() : support
                .createNiceControl());
        return createMock(name, control);
    }

    public T createStrictMock(final String name) {
        final IMocksControl control = (support == null ? EasyMock.createStrictControl() : support
                .createStrictControl());
        return createMock(name, control);
    }

    private void checkConstructorNotInitialized() {
        if (constructor != null) {
            throw new IllegalStateException("Trying to define the constructor call more than once.");
        }
    }
}
