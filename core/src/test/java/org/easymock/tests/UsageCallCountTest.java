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
package org.easymock.tests;

import static org.easymock.EasyMock.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author OFFIS, Tammo Freese
 */
class UsageCallCountTest {

    private VoidMethodInterface mock;

    private interface VoidMethodInterface {
        void method();
    }

    @BeforeEach
    void setup() {
        mock = createMock(VoidMethodInterface.class);
    }

    @Test
    void mockWithNoExpectedCallsPassesWithNoCalls() {
        replay(mock);
        verify(mock);
    }

    @Test
    void mockWithNoExpectedCallsFailsAtFirstCall() {
        replay(mock);
        assertMethodCallFails();
    }

    @Test
    void mockWithOneExpectedCallFailsAtVerify() {
        callMethodOnce();
        replay(mock);
        assertVerifyFails();
    }

    @Test
    void mockWithOneExpectedCallPassesWithOneCall() {
        callMethodOnce();
        replay(mock);
        callMethodOnce();
        verify(mock);
    }

    @Test
    void mockWithOneExpectedCallFailsAtSecondCall() {
        callMethodOnce();
        replay(mock);
        callMethodOnce();
        assertMethodCallFails();
    }

    @Test
    void tooFewCalls() {
        callMethodThreeTimes();
        replay(mock);
        callMethodTwice();
        assertVerifyFails();
    }

    @Test
    void correctNumberOfCalls() {
        callMethodThreeTimes();
        replay(mock);
        callMethodThreeTimes();
        verify(mock);
    }

    @Test
    void tooManyCalls() {
        callMethodThreeTimes();
        replay(mock);
        callMethodThreeTimes();
        assertMethodCallFails();
    }

    private void callMethodOnce() {
        mock.method();
    }

    private void callMethodTwice() {
        mock.method();
        mock.method();
    }

    private void callMethodThreeTimes() {
        mock.method();
        mock.method();
        mock.method();
    }

    private void assertVerifyFails() {
        try {
            verify(mock);
            Assertions.fail("Expected AssertionError");
        } catch (AssertionError expected) {
        }
    }

    private void assertMethodCallFails() {
        try {
            mock.method();
            Assertions.fail("Expected AssertionError");
        } catch (AssertionError expected) {
        }
    }

    @Test
    void noUpperLimitWithoutCallCountSet() {
        mock.method();
        expectLastCall().atLeastOnce();
        replay(mock);
        assertVerifyFails();
        mock.method();
        verify(mock);
        mock.method();
        verify(mock);
        mock.method();
        verify(mock);
    }
}
