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
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantLock;

import org.easymock.IAnswer;

public class ReplayState implements IMocksControlState, Serializable {

    private static final long serialVersionUID = 6314142602251047572L;

    private final IMocksBehavior behavior;
    
    private final ReentrantLock lock = new ReentrantLock();

    public ReplayState(IMocksBehavior behavior) {
        this.behavior = behavior;
    }

    public Object invoke(Invocation invocation) throws Throwable {

        behavior.checkThreadSafety();
        
        if (behavior.isThreadSafe()) {
            // If thread safe, synchronize the mock
            lock.lock();
            try {
                return invokeInner(invocation);
            }
            finally {
                lock.unlock();
            }
        }

        return invokeInner(invocation);
    }

    private Object invokeInner(Invocation invocation) throws Throwable {
        LastControl.pushCurrentInvocation(invocation);
        try {
            Result result = behavior.addActual(invocation);                
            try {
                return result.answer();
            } catch (Throwable t) {
                if (result.shouldFillInStackTrace()) {
                    throw new ThrowableWrapper(t);
                }
                throw t;
            }
        } finally {
            LastControl.popCurrentInvocation();
        }
    }

    public void verify() {
        behavior.verify();
    }

    public void replay() {
        throwWrappedIllegalStateException();
    }

    public void callback(Runnable runnable) {
        throwWrappedIllegalStateException();
    }

    public void checkOrder(boolean value) {
        throwWrappedIllegalStateException();
    }

    public void makeThreadSafe(boolean threadSafe) {
        throwWrappedIllegalStateException();
    }
    
    public void checkIsUsedInOneThread(boolean shouldBeUsedInOneThread) {
        throwWrappedIllegalStateException();
    }

    public void andReturn(Object value) {
        throwWrappedIllegalStateException();
    }

    public void andThrow(Throwable throwable) {
        throwWrappedIllegalStateException();
    }

    public void andAnswer(IAnswer<?> answer) {
        throwWrappedIllegalStateException();
    }

    public void andDelegateTo(Object answer) {
        throwWrappedIllegalStateException();
    }
    
    public void andStubReturn(Object value) {
        throwWrappedIllegalStateException();
    }

    public void andStubThrow(Throwable throwable) {
        throwWrappedIllegalStateException();
    }

    public void andStubAnswer(IAnswer<?> answer) {
        throwWrappedIllegalStateException();
    }

    public void andStubDelegateTo(Object delegateTo) {
        throwWrappedIllegalStateException();
    }
    
    public void asStub() {
        throwWrappedIllegalStateException();
    }

    public void times(Range range) {
        throwWrappedIllegalStateException();
    }

    @SuppressWarnings("deprecation")
    public void setMatcher(Method method, org.easymock.ArgumentsMatcher matcher) {
        throwWrappedIllegalStateException();
    }

    @SuppressWarnings("deprecation")
    public void setDefaultMatcher(org.easymock.ArgumentsMatcher matcher) {
        throwWrappedIllegalStateException();
    }

    public void setDefaultReturnValue(Object value) {
        throwWrappedIllegalStateException();
    }

    public void setDefaultThrowable(Throwable throwable) {
        throwWrappedIllegalStateException();
    }

    public void setDefaultVoidCallable() {
        throwWrappedIllegalStateException();
    }

    private void throwWrappedIllegalStateException() {
        throw new RuntimeExceptionWrapper(new IllegalStateException(
                "This method must not be called in replay state."));
    }

    public void assertRecordState() {
        throwWrappedIllegalStateException();
    }
}
