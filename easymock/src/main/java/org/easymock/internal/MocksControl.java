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

import static org.easymock.internal.ClassExtensionHelper.*;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.easymock.*;

/**
 * @author OFFIS, Tammo Freese
 */
public class MocksControl implements IMocksControl, IExpectationSetters<Object>, Serializable {

    private static final long serialVersionUID = 443604921336702014L;

    private IMocksControlState state;

    private IMocksBehavior behavior;

    public enum MockType {
        NICE, DEFAULT, STRICT
    }

    private MockType type;

    public MocksControl(final MockType type) {
        this.type = type;
        reset();
    }

    public MockType getType() {
        return type;
    }

    public IMocksControlState getState() {
        return state;
    }

    public <T> T createMock(final Class<T> toMock) {
        try {
            state.assertRecordState();
            final IProxyFactory<T> proxyFactory = createProxyFactory(toMock);
            return proxyFactory.createProxy(toMock, new ObjectMethodsFilter(toMock,
                    new MockInvocationHandler(this), null));
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public <T> T createMock(final String name, final Class<T> toMock) {
        try {
            state.assertRecordState();
            final IProxyFactory<T> proxyFactory = createProxyFactory(toMock);
            return proxyFactory.createProxy(toMock, new ObjectMethodsFilter(toMock,
                    new MockInvocationHandler(this), name));
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public <T> T createMock(final String name, final Class<T> toMock, final Method... mockedMethods) {

        if (toMock.isInterface()) {
            throw new IllegalArgumentException("Partial mocking doesn't make sense for interface");
        }

        final T mock = createMock(name, toMock);

        // Set the mocked methods on the interceptor
        getInterceptor(mock).setMockedMethods(mockedMethods);

        return mock;
    }

    public <T> T createMock(final Class<T> toMock, final Method... mockedMethods) {

        if (toMock.isInterface()) {
            throw new IllegalArgumentException("Partial mocking doesn't make sense for interface");
        }

        final T mock = createMock(toMock);

        // Set the mocked methods on the interceptor
        getInterceptor(mock).setMockedMethods(mockedMethods);

        return mock;
    }

    public <T> T createMock(final Class<T> toMock, final ConstructorArgs constructorArgs,
            final Method... mockedMethods) {
        // Trick to allow the ClassProxyFactory to access constructor args
        setCurrentConstructorArgs(constructorArgs);
        try {
            return createMock(toMock, mockedMethods);
        } finally {
            setCurrentConstructorArgs(null);
        }
    }

    public <T> T createMock(final String name, final Class<T> toMock, final ConstructorArgs constructorArgs,
            final Method... mockedMethods) {
        // Trick to allow the ClassProxyFactory to access constructor args
        setCurrentConstructorArgs(constructorArgs);
        try {
            return createMock(name, toMock, mockedMethods);
        } finally {
            setCurrentConstructorArgs(null);
        }
    }

    protected <T> IProxyFactory<T> createProxyFactory(final Class<T> toMock) {
        if (toMock.isInterface()) {
            return new JavaProxyFactory<T>();
        }
        final String classMockingDisabled = EasyMockProperties.getInstance().getProperty(
                EasyMock.DISABLE_CLASS_MOCKING);
        if (Boolean.valueOf(classMockingDisabled)) {
            throw new IllegalArgumentException("Class mocking is currently disabled. Change "
                    + EasyMock.DISABLE_CLASS_MOCKING + " to true do modify this behavior");
        }
        try {
            return new ClassProxyFactory<T>();
        } catch (final NoClassDefFoundError e) {
            throw new RuntimeException(
                    "Class mocking requires to have cglib and objenesis librairies in the classpath", e);
        }
    }

    public final void reset() {
        behavior = new MocksBehavior(type == MockType.NICE);
        behavior.checkOrder(type == MockType.STRICT);
        state = new RecordState(behavior);
        LastControl.reportLastControl(null);
    }

    public void resetToNice() {
        type = MockType.NICE;
        reset();
    }

    public void resetToDefault() {
        type = MockType.DEFAULT;
        reset();
    }

    public void resetToStrict() {
        type = MockType.STRICT;
        reset();
    }

    public void replay() {
        try {
            state.replay();
            state = new ReplayState(behavior);
            LastControl.reportLastControl(null);
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void verify() {
        try {
            state.verify();
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        } catch (final AssertionErrorWrapper e) {
            throw (AssertionError) e.getAssertionError().fillInStackTrace();
        }
    }

    public void checkOrder(final boolean value) {
        try {
            state.checkOrder(value);
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void makeThreadSafe(final boolean threadSafe) {
        try {
            state.makeThreadSafe(threadSafe);
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void checkIsUsedInOneThread(final boolean shouldBeUsedInOneThread) {
        try {
            state.checkIsUsedInOneThread(shouldBeUsedInOneThread);
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    // methods from IBehaviorSetters

    public IExpectationSetters<Object> andReturn(final Object value) {
        try {
            state.andReturn(value);
            return this;
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters<Object> andThrow(final Throwable throwable) {
        try {
            state.andThrow(throwable);
            return this;
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters<Object> andAnswer(final IAnswer<? extends Object> answer) {
        try {
            state.andAnswer(answer);
            return this;
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters<Object> andDelegateTo(final Object answer) {
        try {
            state.andDelegateTo(answer);
            return this;
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void andStubReturn(final Object value) {
        try {
            state.andStubReturn(value);
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void andStubThrow(final Throwable throwable) {
        try {
            state.andStubThrow(throwable);
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void andStubAnswer(final IAnswer<? extends Object> answer) {
        try {
            state.andStubAnswer(answer);
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void andStubDelegateTo(final Object delegateTo) {
        try {
            state.andStubDelegateTo(delegateTo);
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void asStub() {
        try {
            state.asStub();
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters<Object> times(final int times) {
        try {
            state.times(new Range(times));
            return this;
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters<Object> times(final int min, final int max) {
        try {
            state.times(new Range(min, max));
            return this;
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters<Object> once() {
        try {
            state.times(ONCE);
            return this;
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters<Object> atLeastOnce() {
        try {
            state.times(AT_LEAST_ONCE);
            return this;
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters<Object> anyTimes() {
        try {
            state.times(ZERO_OR_MORE);
            return this;
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Exactly one call.
     */
    public static final Range ONCE = new Range(1);

    /**
     * One or more calls.
     */
    public static final Range AT_LEAST_ONCE = new Range(1, Integer.MAX_VALUE);

    /**
     * Zero or more calls.
     */
    public static final Range ZERO_OR_MORE = new Range(0, Integer.MAX_VALUE);

}
