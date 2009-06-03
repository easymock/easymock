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
import java.util.ArrayList;
import java.util.List;

public class UnorderedBehavior implements Serializable {

    private static final long serialVersionUID = 2185791334636597469L;

    private final List<ExpectedInvocationAndResults> results = new ArrayList<ExpectedInvocationAndResults>();

    private final boolean checkOrder;

    public UnorderedBehavior(boolean checkOrder) {
        this.checkOrder = checkOrder;
    }

    public void addExpected(ExpectedInvocation expected, Result result,
            Range count) {
        for (ExpectedInvocationAndResults entry : results) {
            if (entry.getExpectedInvocation().equals(expected)) {
                entry.getResults().add(result, count);
                return;
            }
        }
        Results list = new Results();
        list.add(result, count);
        results.add(new ExpectedInvocationAndResults(expected, list));
    }

    public Result addActual(Invocation actual) {
        for (ExpectedInvocationAndResults entry : results) {
            try {
                if (!entry.getExpectedInvocation().matches(actual)) {
                    continue;
                }
                Result result = entry.getResults().next();
                if (result != null) {
                    // actual and expected matched, validate the capture
                    actual.validateCaptures();
                    return result;
                }
            }
            finally {
                // reset the capture (already validated or expected didn't
                // matched)
                actual.clearCaptures();
            }
        }
        return null;
    }

    public boolean verify() {
        for (ExpectedInvocationAndResults entry : results) {
            if (!entry.getResults().hasValidCallCount()) {
                return false;
            }
        }
        return true;
    }
    
    public List<ErrorMessage> getMessages(Invocation invocation) {
        List<ErrorMessage> messages = new ArrayList<ErrorMessage>(results
                .size());
        for (ExpectedInvocationAndResults entry : results) {
            boolean unordered = !checkOrder;
            boolean validCallCount = entry.getResults().hasValidCallCount();
            boolean match = invocation != null
                    && entry.getExpectedInvocation().matches(invocation);

            if (unordered && validCallCount && !match) {
                continue;
            }

            ErrorMessage message = new ErrorMessage(match, entry.toString(),
                    entry.getResults()
                    .getCallCount());
            messages.add(message);
        }
        return messages;

    }

    public boolean allowsExpectedInvocation(ExpectedInvocation expected,
            boolean checkOrder) {
        if (this.checkOrder != checkOrder) {
            return false;
        } else if (results.isEmpty() || !this.checkOrder) {
            return true;
        } else {
            ExpectedInvocation lastMethodCall = results.get(results.size() - 1)
                    .getExpectedInvocation();
            return lastMethodCall.equals(expected);
        }
    }

}