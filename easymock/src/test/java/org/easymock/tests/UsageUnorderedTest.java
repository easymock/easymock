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
import org.junit.Test;

@SuppressWarnings("deprecation")
public class UsageUnorderedTest {

    public interface Interface {
        void method(int number);
    }

    @Test
    public void message() {
        MockControl<Interface> control = MockControl
                .createControl(Interface.class);
        Interface mock = control.getMock();

        mock.method(0);
        control.setMatcher(MockControl.ALWAYS_MATCHER);
        control.setVoidCallable(1);
        mock.method(0);
        control.setVoidCallable(2);
        mock.method(1);

        control.replay();

        mock.method(6);
        mock.method(7);
        mock.method(1);
        mock.method(25);

        try {
            mock.method(42);
            fail();
        } catch (AssertionError expected) {
            assertEquals(
                    "\n  Unexpected method call method(42). Possible matches are marked with (+1):"
                    + "\n    method(<any>): expected: 3, actual: 3 (+1)"
                    + "\n    method(<any>): expected: 1, actual: 1 (+1)",
                    expected.getMessage());
        }
    }
}
