/**
 * Copyright 2001-2010 the original author or authors.
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
import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
@SuppressWarnings("deprecation")
public class RecordStateInvalidDefaultThrowableTest {
    MockControl<IMethods> control;

    IMethods mock;

    private class CheckedException extends Exception {
        private static final long serialVersionUID = 1L;
    }

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void throwNull() {
        mock.throwsNothing(false);
        try {
            control.setDefaultThrowable(null);
            fail("NullPointerException expected");
        } catch (NullPointerException expected) {
            assertEquals("null cannot be thrown", expected.getMessage());
        }

    }

    @Test
    public void throwCheckedExceptionWhereNoCheckedExceptionIsThrown() {
        mock.throwsNothing(false);
        try {
            control.setDefaultThrowable(new CheckedException());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            assertEquals("last method called on mock cannot throw "
                    + this.getClass().getName() + "$CheckedException", expected
                    .getMessage());
        }
    }

    @Test
    public void throwWrongCheckedException() throws IOException {
        mock.throwsIOException(0);
        try {
            control.setDefaultThrowable(new CheckedException());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            assertEquals("last method called on mock cannot throw "
                    + this.getClass().getName() + "$CheckedException", expected
                    .getMessage());
        }
    }

    @Test
    public void setDefaultThrowableWithoutMethodCall() throws IOException {
        try {
            control.setDefaultThrowable(new RuntimeException());
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting default Throwable",
                    expected.getMessage());
        }
    }

}
