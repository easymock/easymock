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

import java.io.IOException;

import org.easymock.MockControl;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
@SuppressWarnings("deprecation")
public class LegacyBehaviorTests {

    @Test
    public void throwAfterThrowable() throws IOException {

        MockControl<IMethods> control = MockControl
                .createControl(IMethods.class);
        IMethods mock = control.getMock();

        mock.throwsIOException(0);
        control.setThrowable(new IOException());
        control.setThrowable(new IOException(), MockControl.ONE_OR_MORE);

        control.replay();

        try {
            mock.throwsIOException(0);
            fail("IOException expected");
        } catch (IOException expected) {
        }

        boolean exceptionOccured = true;
        try {
            control.verify();
            exceptionOccured = false;
        } catch (AssertionError expected) {
            assertEquals(
                    "\n  Expectation failure on verify:"
                            + "\n    throwsIOException(0): expected: at least 2, actual: 1",
                    expected.getMessage());
        }

        if (!exceptionOccured)
            fail("exception expected");

        try {
            mock.throwsIOException(0);
            fail("IOException expected");
        } catch (IOException expected) {
        }

        control.verify();

        try {
            mock.throwsIOException(0);
            fail("IOException expected");
        } catch (IOException expected) {
        }

        control.verify();
    }
}
