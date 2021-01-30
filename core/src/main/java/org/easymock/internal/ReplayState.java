/*
 * Copyright 2001-2021 the original author or authors.
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

import org.easymock.IAnswer;

import java.io.Serializable;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author OFFIS, Tammo Freese
 */
public class ReplayState implements IMocksControlState, Serializable {

    private static final long serialVersionUID = 6314142602251047572L;

    private final IMocksBehavior behavior;

    private final ReentrantLock lock = new ReentrantLock();

    public ReplayState(IMocksBehavior behavior) {
        this.behavior = behavior;
    }

    @Override
    public Object invoke(Invocation invocation) throws Throwable {

        behavior.checkThreadSafety();

        if (behavior.isThreadSafe()) {
            // If thread safe, synchronize the mock
            lock.lock();
            try {
                return invokeInner(invocation);
            } finally {
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

    @Override
    public void verifyRecording() {
        behavior.verifyRecording();
    }

    @Override
    public void verifyUnexpectedCalls() {
        behavior.verifyUnexpectedCalls();
    }

    @Override
    public void verify() {
        behavior.verify();
    }

    @Override
    public void replay() {
        throwWrappedIllegalStateException();
    }

    public void callback(Runnable runnable) {
        throwWrappedIllegalStateException();
    }

    @Override
    public void checkOrder(boolean value) {
        throwWrappedIllegalStateException();
    }

    @Override
    public void makeThreadSafe(boolean threadSafe) {
        throwWrappedIllegalStateException();
    }

    @Override
    public void checkIsUsedInOneThread(boolean shouldBeUsedInOneThread) {
        throwWrappedIllegalStateException();
    }

    @Override
    public void andReturn(Object value) {
        throwWrappedIllegalStateException();
    }

    @Override
    public void andThrow(Throwable throwable) {
        throwWrappedIllegalStateException();
    }

    @Override
    public void andAnswer(IAnswer<?> answer) {
        throwWrappedIllegalStateException();
    }

    @Override
    public void andDelegateTo(Object answer) {
        throwWrappedIllegalStateException();
    }

    @Override
    public void andVoid() {
        throwWrappedIllegalStateException();
    }

    @Override
    public void andStubReturn(Object value) {
        throwWrappedIllegalStateException();
    }

    @Override
    public void andStubThrow(Throwable throwable) {
        throwWrappedIllegalStateException();
    }

    @Override
    public void andStubAnswer(IAnswer<?> answer) {
        throwWrappedIllegalStateException();
    }

    @Override
    public void andStubDelegateTo(Object delegateTo) {
        throwWrappedIllegalStateException();
    }

    @Override
    public void asStub() {
        throwWrappedIllegalStateException();
    }

    @Override
    public void times(Range range) {
        throwWrappedIllegalStateException();
    }

    private void throwWrappedIllegalStateException() {
        throw new RuntimeExceptionWrapper(new IllegalStateException(
                "This method must not be called in replay state."));
    }

    @Override
    public void assertRecordState() {
        throwWrappedIllegalStateException();
    }
}
