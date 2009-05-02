/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.io.Serializable;

public class ExpectedInvocationAndResults implements Serializable {
    
    private static final long serialVersionUID = 8189985418895395472L;

    ExpectedInvocation expectedInvocation;

    Results results;

    public ExpectedInvocationAndResults(ExpectedInvocation expectedInvocation,
            Results results) {
        this.expectedInvocation = expectedInvocation;
        this.results = results;
    }

    public ExpectedInvocation getExpectedInvocation() {
        return expectedInvocation;
    }

    public Results getResults() {
        return results;
    }

    @Override
    public String toString() {
        return expectedInvocation.toString() + ": " + results.toString();
    }
}