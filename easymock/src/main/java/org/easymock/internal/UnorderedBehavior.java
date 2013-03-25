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

/**
 * @author OFFIS, Tammo Freese
 */
public class UnorderedBehavior implements Serializable {

    private static final long serialVersionUID = 2185791334636597469L;

    private final List<ExpectedInvocationAndResults> results = new ArrayList<ExpectedInvocationAndResults>();

    private final boolean checkOrder;

    public UnorderedBehavior(final boolean checkOrder) {
        this.checkOrder = checkOrder;
    }

    public void addExpected(final ExpectedInvocation expected, final Result result, final Range count) {
        for (final ExpectedInvocationAndResults entry : results) {
            if (entry.getExpectedInvocation().equals(expected)) {
                entry.getResults().add(result, count);
                return;
            }
        }
        final Results list = new Results();
        list.add(result, count);
        results.add(new ExpectedInvocationAndResults(expected, list));
    }

    public Result addActual(final Invocation actual) {
        for (final ExpectedInvocationAndResults entry : results) {
            try {
                // if no results are available anymore, it's worthless to try to match
                if (!entry.getResults().hasResults()) {
                    continue;
                }
                // if it doesn't match, keep searching
                if (!entry.getExpectedInvocation().matches(actual)) {
                    continue;
                }
                final Result result = entry.getResults().next();
                // actual and expected matched, validate the capture
                actual.validateCaptures();
                return result;
            } finally {
                // reset the capture (already validated or expected didn't
                // matched)
                actual.clearCaptures();
            }
        }
        return null;
    }

    public boolean verify() {
        for (final ExpectedInvocationAndResults entry : results) {
            if (!entry.getResults().hasValidCallCount()) {
                return false;
            }
        }
        return true;
    }

    public List<ErrorMessage> getMessages(final Invocation invocation) {
        final List<ErrorMessage> messages = new ArrayList<ErrorMessage>(results.size());
        for (final ExpectedInvocationAndResults entry : results) {
            final boolean unordered = !checkOrder;
            final boolean validCallCount = entry.getResults().hasValidCallCount();
            final boolean match = invocation != null && entry.getExpectedInvocation().matches(invocation);

            if (unordered && validCallCount && !match) {
                continue;
            }

            final ErrorMessage message = new ErrorMessage(match, entry.toString(), entry.getResults()
                    .getCallCount());
            messages.add(message);
        }
        return messages;

    }

    public boolean allowsExpectedInvocation(final ExpectedInvocation expected, final boolean checkOrder) {
        if (this.checkOrder != checkOrder) {
            return false;
        } else if (results.isEmpty() || !this.checkOrder) {
            return true;
        } else {
            final ExpectedInvocation lastMethodCall = results.get(results.size() - 1).getExpectedInvocation();
            return lastMethodCall.equals(expected);
        }
    }

}