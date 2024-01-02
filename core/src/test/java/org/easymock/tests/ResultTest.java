/*
 * Copyright 2001-2024 the original author or authors.
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
package org.easymock.tests;

import org.easymock.internal.Result;
import org.easymock.internal.Results;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author OFFIS, Tammo Freese
 * @author Henri Tremblay
 */
public class ResultTest {

    @Test
    public void createThrowResultToString() {
        Exception e = new Exception("Error message");
        Result r = Result.createThrowResult(e);
        Assertions.assertEquals("Answer throwing " + e, r.toString());
    }

    @Test
    public void createReturnResultToString() {
        String value = "My value";
        Result r = Result.createReturnResult(value);
        Assertions.assertEquals("Answer returning " + value, r.toString());
    }

    @Test
    public void createDelegateResultToString() {
        String value = "my value";
        Result r = Result.createDelegatingResult(value);
        Assertions.assertEquals("Delegated to " + value, r.toString());
    }

    @Test
    public void emptyResults() {
        // We never create a Results without at least one Range
        // This test is only to unit test Results with this to cover the case anyway
        Results results = new Results();
        Assertions.assertFalse(results.hasResults());
        Assertions.assertNull(results.next());
    }
}
