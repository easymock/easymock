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
package org.easymock.tests;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class UsageCallCountTest {

    private VoidMethodInterface mock;

    private interface VoidMethodInterface {
        void method();
    }

    @Before
    public void setup() {
        mock = createMock(VoidMethodInterface.class);
    }

    @Test
    public void mockWithNoExpectedCallsPassesWithNoCalls() {
        replay(mock);
        verify(mock);
    }

    @Test
    public void mockWithNoExpectedCallsFailsAtFirstCall() {
        replay(mock);
        assertMethodCallFails();
    }

    @Test
    public void mockWithOneExpectedCallFailsAtVerify() {
        callMethodOnce();
        replay(mock);
        assertVerifyFails();
    }

    @Test
    public void mockWithOneExpectedCallPassesWithOneCall() {
        callMethodOnce();
        replay(mock);
        callMethodOnce();
        verify(mock);
    }

    @Test
    public void mockWithOneExpectedCallFailsAtSecondCall() {
        callMethodOnce();
        replay(mock);
        callMethodOnce();
        assertMethodCallFails();
    }

    @Test
    public void tooFewCalls() {
        callMethodThreeTimes();
        replay(mock);
        callMethodTwice();
        assertVerifyFails();
    }

    @Test
    public void correctNumberOfCalls() {
        callMethodThreeTimes();
        replay(mock);
        callMethodThreeTimes();
        verify(mock);
    }

    @Test
    public void tooManyCalls() {
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
            fail("Expected AssertionError");
        } catch (final AssertionError expected) {
        }
    }

    private void assertMethodCallFails() {
        try {
            mock.method();
            fail("Expected AssertionError");
        } catch (final AssertionError expected) {
        }
    }

    @Test
    public void noUpperLimitWithoutCallCountSet() {
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
