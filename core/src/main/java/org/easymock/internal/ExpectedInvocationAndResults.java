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

import java.io.Serializable;

/**
 * @author OFFIS, Tammo Freese
 */
public class ExpectedInvocationAndResults implements Serializable {

    private static final long serialVersionUID = 8189985418895395472L;

    private final ExpectedInvocation expectedInvocation;

    private final Results results;

    public ExpectedInvocationAndResults(ExpectedInvocation expectedInvocation, Results results) {
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