/*
 * Copyright 2001-2018 the original author or authors.
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

import org.easymock.*;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author OFFIS, Tammo Freese
 */
public class MocksControl implements IMocksControl, IExpectationSetters<Object>, Serializable {

    private static final long serialVersionUID = 443604921336702014L;

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

    /** lazily created; the proxy factory for classes */
    private static volatile IProxyFactory classProxyFactory;

    private static final IProxyFactory interfaceProxyFactory = new JavaProxyFactory();

    private IMocksControlState state;

    private IMocksBehavior behavior;

    private MockType type;

    public MocksControl(MockType type) {
        this.type = type;
        reset();
    }

    public MockType getType() {
        return type;
    }

    public IMocksControlState getState() {
        return state;
    }

    @Override
    public <T, R> R createMock(Class<T> toMock) {
        return createMock(null, toMock, null, (Method[]) null);
    }

    @Override
    public <T, R> R createMock(String name, Class<T> toMock) {
        return createMock(name, toMock, null, (Method[]) null);
    }

    @Override
    public <T, R> R createMock(String name, Class<T> toMock, ConstructorArgs constructorArgs,
            Method... mockedMethods) {
        if (toMock == null) {
            throw new NullPointerException("Can't mock 'null'");
        }
        if (toMock.isInterface() && mockedMethods != null) {
            throw new IllegalArgumentException("Partial mocking doesn't make sense for interface");
        }

        try {
            state.assertRecordState();
            IProxyFactory proxyFactory = toMock.isInterface()
                    ? interfaceProxyFactory
                    : getClassProxyFactory();
            try {
                @SuppressWarnings("unchecked")
                R mock = (R) proxyFactory.createProxy(toMock, new ObjectMethodsFilter(toMock,
                    new MockInvocationHandler(this), name), mockedMethods, constructorArgs);
                return mock;
            } catch (NoClassDefFoundError e) {
                if(e.getMessage().startsWith("org/objenesis")) {
                    throw new RuntimeExceptionWrapper(new RuntimeException(
                        "Class mocking requires to have Objenesis library in the classpath", e));
                }
                throw e;
            }

        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public static IProxyFactory getProxyFactory(Object o) {
        return Proxy.isProxyClass(o.getClass())
                ? interfaceProxyFactory
                : getClassProxyFactory();
    }

    private static IProxyFactory getClassProxyFactory() {
        String classMockingDisabled = EasyMockProperties.getInstance().getProperty(
                EasyMock.DISABLE_CLASS_MOCKING);
        if (Boolean.valueOf(classMockingDisabled)) {
            throw new IllegalArgumentException("Class mocking is currently disabled. Change "
                    + EasyMock.DISABLE_CLASS_MOCKING + " to true do modify this behavior");
        }

        IProxyFactory cached = classProxyFactory;
        if (cached != null) {
            return cached;
        }

        // ///CLOVER:OFF
        if (AndroidSupport.isAndroid()) {
            return classProxyFactory = new AndroidClassProxyFactory();
        }
        // ///CLOVER:ON

        return classProxyFactory = new ClassProxyFactory();
    }

    public static MocksControl getControl(Object mock) {
        try {
            IProxyFactory factory = getProxyFactory(mock);
            ObjectMethodsFilter handler = (ObjectMethodsFilter) factory.getInvocationHandler(mock);
            return handler.getDelegate().getControl();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Not a mock: " + mock.getClass().getName());
        }
    }

    public static InvocationHandler getInvocationHandler(Object mock) {
        return getProxyFactory(mock).getInvocationHandler(mock);
    }

    /**
     * Return the class of interface (depending on the mock type) that was
     * mocked
     *
     * @param <T>
     *            Mocked class
     * @param <R>
     *            Mock class
     * @param proxy
     *            Mock object
     * @return the mocked class or interface
     */
    @SuppressWarnings("unchecked")
    public static <T,  R extends T> Class<R> getMockedClass(T proxy) {
        if (Proxy.isProxyClass(proxy.getClass())) {
            return (Class<R>) proxy.getClass().getInterfaces()[0];
        }
        return (Class<R>) proxy.getClass().getSuperclass();
    }

    /**
     * Return the class of interface (depending on the mock type) that was
     * mocked
     *
     * @param <T>
     *            Mocked class
     * @param <R>
     *            Mock class
     * @param proxy
     *            Mock object
     * @return the mocked class or interface
     * @deprecated use {@link #getMockedClass(Object)} instead
     */
    @Deprecated
    public static <T,  R extends T> Class<R> getMockedType(T proxy) {
        return getMockedClass(proxy);
    }

    @Override
    public void reset() {
        behavior = new MocksBehavior(type == org.easymock.MockType.NICE);
        behavior.checkOrder(type == org.easymock.MockType.STRICT);
        state = new RecordState(behavior);
        LastControl.reportLastControl(null);
    }

    @Override
    public void resetToNice() {
        type = MockType.NICE;
        reset();
    }

    @Override
    public void resetToDefault() {
        type = MockType.DEFAULT;
        reset();
    }

    @Override
    public void resetToStrict() {
        type = MockType.STRICT;
        reset();
    }

    @Override
    public void replay() {
        try {
            state.replay();
            state = new ReplayState(behavior);
            LastControl.reportLastControl(null);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    @Override
    public void verifyRecording() {
        try {
            state.verifyRecording();
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        } catch (AssertionErrorWrapper e) {
            throw (AssertionError) e.getAssertionError().fillInStackTrace();
        }
    }

    @Override
    public void verifyUnexpectedCalls() {
        try {
            state.verifyUnexpectedCalls();
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        } catch (AssertionErrorWrapper e) {
            throw (AssertionError) e.getAssertionError().fillInStackTrace();
        }
    }

    @Override
    public void verify() {
        try {
            state.verify();
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        } catch (AssertionErrorWrapper e) {
            throw (AssertionError) e.getAssertionError().fillInStackTrace();
        }
    }

    @Override
    public void checkOrder(boolean value) {
        try {
            state.checkOrder(value);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    @Override
    public void makeThreadSafe(boolean threadSafe) {
        try {
            state.makeThreadSafe(threadSafe);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    @Override
    public void checkIsUsedInOneThread(boolean shouldBeUsedInOneThread) {
        try {
            state.checkIsUsedInOneThread(shouldBeUsedInOneThread);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    // methods from IBehaviorSetters

    @Override
    public IExpectationSetters<Object> andReturn(Object value) {
        try {
            state.andReturn(value);
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    @Override
    public IExpectationSetters<Object> andThrow(Throwable throwable) {
        try {
            state.andThrow(throwable);
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    @Override
    public IExpectationSetters<Object> andAnswer(IAnswer<?> answer) {
        try {
            state.andAnswer(answer);
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    @Override
    public IExpectationSetters<Object> andDelegateTo(Object answer) {
        try {
            state.andDelegateTo(answer);
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    @Override
    public IExpectationSetters<Object> andVoid() {
        try {
            state.andVoid();
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    @Override
    public void andStubReturn(Object value) {
        try {
            state.andStubReturn(value);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    @Override
    public void andStubThrow(Throwable throwable) {
        try {
            state.andStubThrow(throwable);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    @Override
    public void andStubAnswer(IAnswer<?> answer) {
        try {
            state.andStubAnswer(answer);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    @Override
    public void andStubDelegateTo(Object delegateTo) {
        try {
            state.andStubDelegateTo(delegateTo);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    @Override
    public void asStub() {
        try {
            state.asStub();
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    @Override
    public IExpectationSetters<Object> times(int times) {
        try {
            state.times(new Range(times));
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    @Override
    public IExpectationSetters<Object> times(int min, int max) {
        try {
            state.times(new Range(min, max));
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    @Override
    public IExpectationSetters<Object> once() {
        try {
            state.times(ONCE);
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    @Override
    public IExpectationSetters<Object> atLeastOnce() {
        try {
            state.times(AT_LEAST_ONCE);
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    @Override
    public IExpectationSetters<Object> anyTimes() {
        try {
            state.times(ZERO_OR_MORE);
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

}
