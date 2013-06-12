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

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.easymock.*;

/**
 * @author OFFIS, Tammo Freese
 */
public class MocksControl implements IMocksControl, IExpectationSetters<Object>, Serializable {

    private static final long serialVersionUID = 443604921336702014L;

    /** lazily created; the proxy factory for classes */
    private static IProxyFactory classProxyFactory;

    private static final IProxyFactory interfaceProxyFactory = new JavaProxyFactory();

    private IMocksControlState state;

    private IMocksBehavior behavior;

    /**
     * This class was kept here for compabitility reason with frameworks using EasyMock
     * @deprecated Use org.easymock.MockType
     */
    @Deprecated
    public enum MockType {
        NICE(org.easymock.MockType.NICE),
        DEFAULT(org.easymock.MockType.DEFAULT),
        STRICT(org.easymock.MockType.STRICT);

        public org.easymock.MockType realType;

        MockType(final org.easymock.MockType realType) {
            this.realType = realType;
        }
    }

    private org.easymock.MockType type;

    public MocksControl(final org.easymock.MockType type) {
        this.type = type;
        reset();
    }

    public MocksControl(final MockType type) {
        this.type = type.realType;
        reset();
    }

    public org.easymock.MockType getType() {
        return type;
    }

    public IMocksControlState getState() {
        return state;
    }

    public <T> T createMock(final Class<T> toMock) {
        return createMock(null, toMock, (Method[]) null);
    }

    public <T> T createMock(final String name, final Class<T> toMock) {
        return createMock(name, toMock, (Method[]) null);
    }

    @Deprecated
    public <T> T createMock(final String name, final Class<T> toMock, final Method... mockedMethods) {
        return createMock(name, toMock, null, mockedMethods);
    }

    @Deprecated
    public <T> T createMock(final Class<T> toMock, final Method... mockedMethods) {
        return createMock(null, toMock, null, mockedMethods);
    }

    @Deprecated
    public <T> T createMock(final Class<T> toMock, final ConstructorArgs constructorArgs,
            final Method... mockedMethods) {
        return createMock(null, toMock, constructorArgs, mockedMethods);
    }

    @Deprecated
    public <T> T createMock(final String name, final Class<T> toMock, final ConstructorArgs constructorArgs,
            final Method... mockedMethods) {
        if (toMock.isInterface() && mockedMethods != null) {
            throw new IllegalArgumentException("Partial mocking doesn't make sense for interface");
        }

        try {
            state.assertRecordState();
            final IProxyFactory proxyFactory = toMock.isInterface()
                    ? interfaceProxyFactory
                    : getClassProxyFactory();
            return proxyFactory.createProxy(toMock, new ObjectMethodsFilter(toMock,
                    new MockInvocationHandler(this), name), mockedMethods, constructorArgs);
        } catch (final RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public static IProxyFactory getProxyFactory(final Object o) {
        return Proxy.isProxyClass(o.getClass())
                ? new JavaProxyFactory()
                : getClassProxyFactory();
    }

    private static IProxyFactory getClassProxyFactory() {
        final String classMockingDisabled = EasyMockProperties.getInstance().getProperty(
                EasyMock.DISABLE_CLASS_MOCKING);
        if (Boolean.valueOf(classMockingDisabled)) {
            throw new IllegalArgumentException("Class mocking is currently disabled. Change "
                    + EasyMock.DISABLE_CLASS_MOCKING + " to true do modify this behavior");
        }

        final IProxyFactory cached = classProxyFactory;
        if (cached != null) {
            return cached;
        }

        // ///CLOVER:OFF
        if (AndroidSupport.isAndroid()) {
            return classProxyFactory = new AndroidClassProxyFactory();
        }
        // ///CLOVER:ON

        try {
            return classProxyFactory = new ClassProxyFactory();
        } catch (final NoClassDefFoundError e) {
            throw new RuntimeException(
                    "Class mocking requires to have cglib and objenesis librairies in the classpath", e);
        }
    }

    public static MocksControl getControl(final Object mock) {
        try {
            final IProxyFactory factory = getProxyFactory(mock);
            final ObjectMethodsFilter handler = (ObjectMethodsFilter) factory.getInvocationHandler(mock);
            return handler.getDelegate().getControl();
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException("Not a mock: " + mock.getClass().getName());
        }
    }

    public static InvocationHandler getInvocationHandler(final Object mock) {
        return getClassProxyFactory().getInvocationHandler(mock);
    }

    /**
     * Return the class of interface (depending on the mock type) that was
     * mocked
     *
     * @param <T>
     *            Mocked class
     * @param <V>
     *            Mock class
     * @param proxy
     *            Mock object
     * @return the mocked class or interface
     */
    @SuppressWarnings("unchecked")
    public static <T, V extends T> Class<T> getMockedType(final V proxy) {
        if (Proxy.isProxyClass(proxy.getClass())) {
            return (Class<T>) proxy.getClass().getInterfaces()[0];
        }
        return (Class<T>) proxy.getClass().getSuperclass();
    }

    public final void reset() {
        behavior = new MocksBehavior(type == org.easymock.MockType.NICE);
        behavior.checkOrder(type == org.easymock.MockType.STRICT);
        state = new RecordState(behavior);
        LastControl.reportLastControl(null);
    }

    public void resetToNice() {
        type = org.easymock.MockType.NICE;
        reset();
    }

    public void resetToDefault() {
        type = org.easymock.MockType.DEFAULT;
        reset();
    }

    public void resetToStrict() {
        type = org.easymock.MockType.STRICT;
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
