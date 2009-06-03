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
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class UsageCallCountTest {

    private MockControl<VoidMethodInterface> control;

    private VoidMethodInterface mock;

    private interface VoidMethodInterface {
        void method();
    }

    @Before
    public void setup() {
        control = MockControl.createControl(VoidMethodInterface.class);
        mock = control.getMock();
    }

    @Test
    public void mockWithNoExpectedCallsPassesWithNoCalls() {
        control.replay();
        control.verify();
    }

    @Test
    public void mockWithNoExpectedCallsFailsAtFirstCall() {
        control.replay();
        assertMethodCallFails();
    }

    @Test
    public void mockWithOneExpectedCallFailsAtVerify() {
        callMethodOnce();
        control.replay();
        assertVerifyFails();
    }

    @Test
    public void mockWithOneExpectedCallPassesWithOneCall() {
        callMethodOnce();
        control.replay();
        callMethodOnce();
        control.verify();
    }

    @Test
    public void mockWithOneExpectedCallFailsAtSecondCall() {
        callMethodOnce();
        control.replay();
        callMethodOnce();
        assertMethodCallFails();
    }

    @Test
    public void tooFewCalls() {
        callMethodThreeTimes();
        control.replay();
        callMethodTwice();
        assertVerifyFails();
    }

    @Test
    public void correctNumberOfCalls() {
        callMethodThreeTimes();
        control.replay();
        callMethodThreeTimes();
        control.verify();
    }

    @Test
    public void tooManyCalls() {
        callMethodThreeTimes();
        control.replay();
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
            control.verify();
            fail("Expected AssertionError");
        } catch (AssertionError expected) {
        }
    }

    private void assertMethodCallFails() {
        try {
            mock.method();
            fail("Expected AssertionError");
        } catch (AssertionError expected) {
        }
    }

    @Test
    public void noUpperLimitWithoutCallCountSet() {
        mock.method();
        control.setVoidCallable(MockControl.ONE_OR_MORE);
        control.replay();
        assertVerifyFails();
        mock.method();
        control.verify();
        mock.method();
        control.verify();
        mock.method();
        control.verify();
    }
}
