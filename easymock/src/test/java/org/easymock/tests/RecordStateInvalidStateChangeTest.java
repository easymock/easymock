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

import org.easymock.internal.RecordState;
import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class RecordStateInvalidStateChangeTest {

    private IMethods mock;

    @Before
    public void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    public void activateWithoutReturnValue() {
        expect(mock.oneArg(false));
        try {
            replay(mock);
            fail("IllegalStateException expected");
        } catch (final IllegalStateException e) {
            assertEquals("missing behavior definition for the preceding method call:\nIMethods.oneArg(false)"
                    + "\nUsage is: expect(a.foo()).andXXX()", e.getMessage());
            assertTrue("stack trace must be cut",
                    Util.getStackTrace(e).indexOf(RecordState.class.getName()) == -1);
        }
    }

    @Test
    public void secondCallWithoutReturnValue() {
        mock.oneArg(false);
        try {
            mock.oneArg(false);
            fail("IllegalStateException expected");
        } catch (final IllegalStateException e) {
            assertEquals("missing behavior definition for the preceding method call:\nIMethods.oneArg(false)"
                    + "\nUsage is: expect(a.foo()).andXXX()", e.getMessage());
            assertTrue("stack trace must be cut",
                    Util.getStackTrace(e).indexOf(RecordState.class.getName()) == -1);
        }
    }

    @Test
    public void verifyWithoutActivation() {
        try {
            verify(mock);
            fail("IllegalStateException expected");
        } catch (final IllegalStateException e) {
            assertEquals("calling verify is not allowed in record state", e.getMessage());
        }
    }
}
