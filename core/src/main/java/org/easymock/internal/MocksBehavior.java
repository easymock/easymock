/*
 * Copyright 2001-2025 the original author or authors.
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

import org.easymock.EasyMock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Default implementation of {@link IMocksBehavior}. It keeps the full behavior of mocks from the same {@link org.easymock.IMocksControl}.
 *
 * @author OFFIS, Tammo Freese
 */
public class MocksBehavior implements IMocksBehavior, Serializable {

    private static final long serialVersionUID = 6824996227285837998L;

    private final List<UnorderedBehavior> behaviorLists = new ArrayList<>();

    private final List<ExpectedInvocationAndResult> stubResults = new ArrayList<>();

    private final List<Invocation> unexpectedCalls = new ArrayList<>();

    private final boolean nice;

    private volatile boolean checkOrder;

    private volatile boolean isThreadSafe;

    private volatile boolean shouldBeUsedInOneThread;

    private final AtomicInteger position = new AtomicInteger();

    private transient volatile Thread lastThread;

    public MocksBehavior(boolean nice) {
        this.nice = nice;
        this.isThreadSafe = !Boolean.parseBoolean(EasyMockProperties.getInstance().getProperty(
                EasyMock.NOT_THREAD_SAFE_BY_DEFAULT));
        this.shouldBeUsedInOneThread = Boolean.parseBoolean(EasyMockProperties.getInstance().getProperty(
                EasyMock.ENABLE_THREAD_SAFETY_CHECK_BY_DEFAULT));
    }

    @Override
    public final void addStub(ExpectedInvocation expected, Result result) {
        stubResults.add(new ExpectedInvocationAndResult(expected, result));
    }

    @Override
    public void addExpected(ExpectedInvocation expected, Result result, Range count) {
        addBehaviorListIfNecessary(expected);
        lastBehaviorList().addExpected(expected, result, count);
    }

    private Result getStubResult(Invocation actual) {
        for (ExpectedInvocationAndResult each : stubResults) {
            if (each.getExpectedInvocation().matches(actual)) {
                return each.getResult();
            }
        }
        return null;
    }

    private void addBehaviorListIfNecessary(ExpectedInvocation expected) {
        if (behaviorLists.isEmpty() || !lastBehaviorList().allowsExpectedInvocation(expected, checkOrder)) {
            behaviorLists.add(new UnorderedBehavior(checkOrder));
        }
    }

    private UnorderedBehavior lastBehaviorList() {
        return behaviorLists.get(behaviorLists.size() - 1);
    }

    @Override
    public final Result addActual(Invocation actual) {
        int initialPosition = position.get();

        while (position.get() < behaviorLists.size()) {
            int index = position.get();
            Result result = behaviorLists.get(index).addActual(actual);
            if (result != null) {
                return result;
            }
            if (!behaviorLists.get(index).verify()) {
                break;
            }
            position.incrementAndGet();
        }
        Result stubOrNice = getStubResult(actual);
        if (stubOrNice == null && nice) {
            stubOrNice = Result.createReturnResult(RecordState.emptyReturnValueFor(actual.getMethod()
                    .getReturnType()));
        }

        int endPosition = position.get();

        // Do not move the cursor in case of stub, nice or error
        position.set(initialPosition);

        if (stubOrNice != null) {
            actual.validateCaptures();
            actual.clearCaptures();
            return stubOrNice;
        }

        // Case where the loop was exited at the end of the behaviorLists
        if (endPosition == behaviorLists.size()) {
            endPosition--;
        }

        // Loop all around the behaviors left to generate the message
        StringBuilder errorMessage = new StringBuilder(70 * (endPosition - initialPosition + 1)); // rough approximation of the length
        errorMessage.append("\n  Unexpected method call ").append(actual.toString());

        List<ErrorMessage> messages = new ArrayList<>();

        int matches = 0;

        // First find how many match we have
        for (int i = initialPosition; i <= endPosition; i++) {
            List<ErrorMessage> thisListMessages = behaviorLists.get(i).getMessages(actual);
            messages.addAll(thisListMessages);
            for (ErrorMessage m : thisListMessages) {
                if (m.isMatching()) {
                    matches++;
                }
            }
        }

        if (matches > 1) {
            errorMessage.append(". Possible matches are marked with (+1)");
        }

        if (!messages.isEmpty()) {
            errorMessage.append(":");
            for (ErrorMessage m : messages) {
                m.appendTo(errorMessage, matches);
            }
        }

        // Keep the unexpected invocation to have a look in the verify
        unexpectedCalls.add(actual);

        // And finally throw the error
        throw new AssertionErrorWrapper(new AssertionError(errorMessage));
    }

    @Override
    public void verifyRecording() {
        boolean verified = true;

        for (UnorderedBehavior behaviorList : behaviorLists.subList(position.get(), behaviorLists.size())) {
            if (!behaviorList.verify()) {
                verified = false;
                break;
            }
        }
        if (verified) {
            return;
        }

        StringBuilder errorMessage = new StringBuilder(70 * (behaviorLists.size() - position.get() + 1));

        errorMessage.append("\n  Expectation failure on verify:");
        for (UnorderedBehavior behaviorList : behaviorLists.subList(position.get(), behaviorLists.size())) {
            for (ErrorMessage m : behaviorList.getMessages(null)) {
                m.appendTo(errorMessage, 0);
            }
        }

        throw new AssertionErrorWrapper(new AssertionError(errorMessage.toString()));
    }

    @Override
    public void verifyUnexpectedCalls() {
        if(unexpectedCalls.isEmpty()) {
            return;
        }

        StringBuilder errorMessage = new StringBuilder(70 * unexpectedCalls.size());

        errorMessage.append("\n  Unexpected method calls:");
        for (Invocation invocation : unexpectedCalls) {
            errorMessage.append("\n    ");
            errorMessage.append(invocation.toString());
        }

        throw new AssertionErrorWrapper(new AssertionError(errorMessage.toString()));
    }

    @Override
    public void verify() {
        AssertionErrorWrapper firstError = null;
        try {
            verifyRecording();
        } catch(AssertionErrorWrapper e) {
            firstError = e;
        }
        try {
            verifyUnexpectedCalls();
        } catch(AssertionErrorWrapper e) {
            if(firstError == null) {
                throw e;
            }
            throw new AssertionErrorWrapper(new AssertionError(firstError.getAssertionError().getMessage() + e.getAssertionError().getMessage()));
        }
        if(firstError != null) {
            throw firstError;
        }
    }

    @Override
    public void checkOrder(boolean value) {
        this.checkOrder = value;
    }

    @Override
    public void makeThreadSafe(boolean isThreadSafe) {
        this.isThreadSafe = isThreadSafe;
    }

    @Override
    public void shouldBeUsedInOneThread(boolean shouldBeUsedInOneThread) {
        this.shouldBeUsedInOneThread = shouldBeUsedInOneThread;
    }

    @Override
    public boolean isThreadSafe() {
        return this.isThreadSafe;
    }

    @Override
    public void checkThreadSafety() {
        if (!shouldBeUsedInOneThread) {
            return;
        }
        if (lastThread == null) {
            lastThread = Thread.currentThread();
        } else if (lastThread != Thread.currentThread()) {
            throw new AssertionErrorWrapper(new AssertionError(
                    "\n Mock isn't supposed to be called from multiple threads. Last: " + lastThread
                            + " Current: " + Thread.currentThread()));
        }
    }
}
