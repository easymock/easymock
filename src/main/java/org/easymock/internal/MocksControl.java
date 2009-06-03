/*
 * Copyright 2001-2009 OFFIS, Tammo Freese
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

import org.easymock.IAnswer;
import org.easymock.IExpectationSetters;
import org.easymock.IMocksControl;

public class MocksControl implements IMocksControl, IExpectationSetters<Object>, Serializable {

    private static final long serialVersionUID = 443604921336702014L;

    private IMocksControlState state;

    private IMocksBehavior behavior;

    public enum MockType {
        NICE, DEFAULT, STRICT
    }

    private MockType type;

    public MocksControl(MockType type) {
        this.type = type;
        reset();
    }

    public IMocksControlState getState() {
        return state;
    }

    public <T> T createMock(Class<T> toMock) {
        try {
            state.assertRecordState();
            IProxyFactory<T> proxyFactory = createProxyFactory(toMock);
            return proxyFactory.createProxy(toMock, new ObjectMethodsFilter(
                    toMock, new MockInvocationHandler(this), null));
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public <T> T createMock(String name, Class<T> toMock) {
        try {
            state.assertRecordState();
            IProxyFactory<T> proxyFactory = createProxyFactory(toMock);
            return proxyFactory.createProxy(toMock, new ObjectMethodsFilter(
                    toMock, new MockInvocationHandler(this), name));
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    protected <T> IProxyFactory<T> createProxyFactory(Class<T> toMock) {
        return new JavaProxyFactory<T>();
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
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void verify() {
        try {
            state.verify();
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        } catch (AssertionErrorWrapper e) {
            throw (AssertionError) e.getAssertionError().fillInStackTrace();
        }
    }

    public void checkOrder(boolean value) {
        try {
            state.checkOrder(value);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }
    
    public void makeThreadSafe(boolean threadSafe) {
        try {
            state.makeThreadSafe(threadSafe);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }
    
    public void checkIsUsedInOneThread(boolean shouldBeUsedInOneThread) {
        try {
            state.checkIsUsedInOneThread(shouldBeUsedInOneThread);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    // methods from IBehaviorSetters

    public IExpectationSetters<Object> andReturn(Object value) {
        try {
            state.andReturn(value);
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters<Object> andThrow(Throwable throwable) {
        try {
            state.andThrow(throwable);
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters<Object> andAnswer(IAnswer<? extends Object> answer) {
        try {
            state.andAnswer(answer);
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters<Object> andDelegateTo(Object answer) {
        try {
            state.andDelegateTo(answer);
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }
    
    public void andStubReturn(Object value) {
        try {
            state.andStubReturn(value);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void andStubThrow(Throwable throwable) {
        try {
            state.andStubThrow(throwable);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void andStubAnswer(IAnswer<? extends Object> answer) {
        try {
            state.andStubAnswer(answer);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }


    public void andStubDelegateTo(Object delegateTo) {
        try {
            state.andStubDelegateTo(delegateTo);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }
    
    public void asStub() {
        try {
            state.asStub();
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters<Object> times(int times) {
        try {
            state.times(new Range(times));
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters<Object> times(int min, int max) {
        try {
            state.times(new Range(min, max));
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters<Object> once() {
        try {
            state.times(ONCE);
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters<Object> atLeastOnce() {
        try {
            state.times(AT_LEAST_ONCE);
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters<Object> anyTimes() {
        try {
            state.times(ZERO_OR_MORE);
            return this;
        } catch (RuntimeExceptionWrapper e) {
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

    @SuppressWarnings("deprecation")
    public void setLegacyDefaultMatcher(org.easymock.ArgumentsMatcher matcher) {
        try {
            state.setDefaultMatcher(matcher);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public void setLegacyMatcher(org.easymock.ArgumentsMatcher matcher) {
        try {
            state.setMatcher(null, matcher);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void setLegacyDefaultReturnValue(Object value) {
        try {
            state.setDefaultReturnValue(value);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void setLegacyDefaultVoidCallable() {
        state.setDefaultVoidCallable();
    }

    public void setLegacyDefaultThrowable(Throwable throwable) {
        try {
            state.setDefaultThrowable(throwable);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }
}
