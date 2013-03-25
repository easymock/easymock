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
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;

/**
 * @author OFFIS, Tammo Freese
 */
public class MocksBehavior implements IMocksBehavior, Serializable {

    private static final long serialVersionUID = 3265727009370529027L;

    private final List<UnorderedBehavior> behaviorLists = new ArrayList<UnorderedBehavior>();

    private final List<ExpectedInvocationAndResult> stubResults = new ArrayList<ExpectedInvocationAndResult>();

    private final boolean nice;

    private boolean checkOrder;

    private boolean isThreadSafe;

    private boolean shouldBeUsedInOneThread;

    private int position = 0;

    private transient volatile Thread lastThread;

    public MocksBehavior(final boolean nice) {
        this.nice = nice;
        this.isThreadSafe = !Boolean.valueOf(EasyMockProperties.getInstance().getProperty(
                EasyMock.NOT_THREAD_SAFE_BY_DEFAULT));
        this.shouldBeUsedInOneThread = Boolean.valueOf(EasyMockProperties.getInstance().getProperty(
                EasyMock.ENABLE_THREAD_SAFETY_CHECK_BY_DEFAULT));
    }

    public final void addStub(final ExpectedInvocation expected, final Result result) {
        stubResults.add(new ExpectedInvocationAndResult(expected, result));
    }

    public void addExpected(final ExpectedInvocation expected, final Result result, final Range count) {
        addBehaviorListIfNecessary(expected);
        lastBehaviorList().addExpected(expected, result, count);
    }

    private Result getStubResult(final Invocation actual) {
        for (final ExpectedInvocationAndResult each : stubResults) {
            if (each.getExpectedInvocation().matches(actual)) {
                return each.getResult();
            }
        }
        return null;
    }

    private void addBehaviorListIfNecessary(final ExpectedInvocation expected) {
        if (behaviorLists.isEmpty() || !lastBehaviorList().allowsExpectedInvocation(expected, checkOrder)) {
            behaviorLists.add(new UnorderedBehavior(checkOrder));
        }
    }

    private UnorderedBehavior lastBehaviorList() {
        return behaviorLists.get(behaviorLists.size() - 1);
    }

    public final Result addActual(final Invocation actual) {
        final int initialPosition = position;

        while (position < behaviorLists.size()) {
            final Result result = behaviorLists.get(position).addActual(actual);
            if (result != null) {
                return result;
            }
            if (!behaviorLists.get(position).verify()) {
                break;
            }
            position++;
        }
        Result stubOrNice = getStubResult(actual);
        if (stubOrNice == null && nice) {
            stubOrNice = Result.createReturnResult(RecordState.emptyReturnValueFor(actual.getMethod()
                    .getReturnType()));
        }

        int endPosition = position;

        // Do not move the cursor in case of stub, nice or error
        position = initialPosition;

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
        final StringBuilder errorMessage = new StringBuilder(70 * (endPosition - initialPosition + 1)); // rough approximation of the length
        errorMessage.append("\n  Unexpected method call ").append(actual.toString());

        final List<ErrorMessage> messages = new ArrayList<ErrorMessage>();

        int matches = 0;

        // First find how many match we have
        for (int i = initialPosition; i <= endPosition; i++) {
            final List<ErrorMessage> thisListMessages = behaviorLists.get(i).getMessages(actual);
            messages.addAll(thisListMessages);
            for (final ErrorMessage m : thisListMessages) {
                if (m.isMatching()) {
                    matches++;
                }
            }
        }

        if (matches > 1) {
            errorMessage.append(". Possible matches are marked with (+1):");
        } else {
            errorMessage.append(":");
        }

        for (final ErrorMessage m : messages) {
            m.appendTo(errorMessage, matches);
        }

        // And finally throw the error
        throw new AssertionErrorWrapper(new AssertionError(errorMessage));
    }

    public void verify() {
        boolean verified = true;

        for (final UnorderedBehavior behaviorList : behaviorLists.subList(position, behaviorLists.size())) {
            if (!behaviorList.verify()) {
                verified = false;
                break;
            }
        }
        if (verified) {
            return;
        }

        final StringBuilder errorMessage = new StringBuilder(70 * (behaviorLists.size() - position + 1));

        errorMessage.append("\n  Expectation failure on verify:");
        for (final UnorderedBehavior behaviorList : behaviorLists.subList(position, behaviorLists.size())) {
            for (final ErrorMessage m : behaviorList.getMessages(null)) {
                m.appendTo(errorMessage, 0);
            }
        }

        throw new AssertionErrorWrapper(new AssertionError(errorMessage.toString()));
    }

    public void checkOrder(final boolean value) {
        this.checkOrder = value;
    }

    public void makeThreadSafe(final boolean isThreadSafe) {
        this.isThreadSafe = isThreadSafe;
    }

    public void shouldBeUsedInOneThread(final boolean shouldBeUsedInOneThread) {
        this.shouldBeUsedInOneThread = shouldBeUsedInOneThread;
    }

    public boolean isThreadSafe() {
        return this.isThreadSafe;
    }

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
