/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MocksBehavior implements IMocksBehavior, Serializable {

    private static final long serialVersionUID = 3265727009370529027L;

    private final List<UnorderedBehavior> behaviorLists = new ArrayList<UnorderedBehavior>();

    private final List<ExpectedInvocationAndResult> stubResults = new ArrayList<ExpectedInvocationAndResult>();

    private final boolean nice;

    private boolean checkOrder;

    private boolean isThreadSafe;

    private int position = 0;

    private transient volatile Thread lastThread;
    
    private LegacyMatcherProvider legacyMatcherProvider;

    public MocksBehavior(boolean nice) {
        this.nice = nice;
    }

    public final void addStub(ExpectedInvocation expected, Result result) {
        stubResults.add(new ExpectedInvocationAndResult(expected, result));
    }

    public void addExpected(ExpectedInvocation expected, Result result,
            Range count) {
        if (legacyMatcherProvider != null) {
            expected = expected.withMatcher(legacyMatcherProvider
                    .getMatcher(expected.getMethod()));
        }
        addBehaviorListIfNecessary(expected);
        lastBehaviorList().addExpected(expected, result, count);
    }

    private final Result getStubResult(Invocation actual) {
        for (ExpectedInvocationAndResult each : stubResults) {
            if (each.getExpectedInvocation().matches(actual)) {
                return each.getResult();
            }
        }
        return null;
    }

    private void addBehaviorListIfNecessary(ExpectedInvocation expected) {
        if (behaviorLists.isEmpty()
                || !lastBehaviorList().allowsExpectedInvocation(expected,
                        checkOrder)) {
            behaviorLists.add(new UnorderedBehavior(checkOrder));
        }
    }

    private UnorderedBehavior lastBehaviorList() {
        return behaviorLists.get(behaviorLists.size() - 1);
    }

    @SuppressWarnings("deprecation")
    public final Result addActual(Invocation actual) {
        int tempPosition = position;
        StringBuilder errorMessage = new StringBuilder();
        while (position < behaviorLists.size()) {
            Result result = behaviorLists.get(position).addActual(actual);
            if (result != null) {
                return result;
            }
            errorMessage.append(behaviorLists.get(position).toString(actual));
            if (!behaviorLists.get(position).verify()) {
                break;
            }
            position++;
        }
        Result stubOrNice = getStubResult(actual);
        if (stubOrNice == null && nice) {
            stubOrNice = Result.createReturnResult(RecordState
                    .emptyReturnValueFor(actual.getMethod().getReturnType()));
        }

        // Do not move the cursor in case of stub, nice or error
        position = tempPosition;

        if (stubOrNice != null) {
            return stubOrNice;
        }
        throw new AssertionErrorWrapper(
                new AssertionError(
                        "\n  Unexpected method call "
                                + actual
                                        .toString(org.easymock.MockControl.EQUALS_MATCHER)
                                + ":" + errorMessage));
    }

    public void verify() {
        boolean verified = true;
        StringBuilder errorMessage = new StringBuilder();

        for (UnorderedBehavior behaviorList : behaviorLists.subList(position,
                behaviorLists.size())) {
            errorMessage.append(behaviorList.toString());
            if (!behaviorList.verify()) {
                verified = false;
            }
        }
        if (verified) {
            return;
        }

        throw new AssertionErrorWrapper(new AssertionError(
                "\n  Expectation failure on verify:" + errorMessage.toString()));
    }

    public void checkOrder(boolean value) {
        this.checkOrder = value;
    }

    public void makeThreadSafe(boolean isThreadSafe) {
        this.isThreadSafe = isThreadSafe;
    }

    public boolean isThreadSafe() {
        return this.isThreadSafe;
    }

    public void checkCurrentThreadSameAsLastThread() {       
        if (lastThread == null) {
            lastThread = Thread.currentThread();
        } else if(lastThread != Thread.currentThread()) {
            throw new AssertionErrorWrapper(new AssertionError(
                    "\n Un-thread-safe mock called from multiple threads. Last: " + lastThread + 
                    " Current: " + Thread.currentThread()));
        }        
    }

    public LegacyMatcherProvider getLegacyMatcherProvider() {
        if (legacyMatcherProvider == null) {
            legacyMatcherProvider = new LegacyMatcherProvider();
        }
        return legacyMatcherProvider;
    }

    @SuppressWarnings("deprecation")
    public void setDefaultMatcher(org.easymock.ArgumentsMatcher matcher) {
        getLegacyMatcherProvider().setDefaultMatcher(matcher);
    }

    @SuppressWarnings("deprecation")
    public void setMatcher(Method method, org.easymock.ArgumentsMatcher matcher) {
        getLegacyMatcherProvider().setMatcher(method, matcher);
    }
}
