/*
 * Copyright 2001-2026 the original author or authors.
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author OFFIS, Tammo Freese
 */
class RecordStateInvalidDefaultThrowableTest {

    private IMethods mock;

    private static class CheckedException extends Exception {
        private static final long serialVersionUID = 1L;
    }

    @BeforeEach
    void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    void throwNull() {
        NullPointerException e = assertThrows(NullPointerException.class, () -> expect(mock.throwsNothing(false)).andStubThrow(null));
        assertEquals("null cannot be thrown", e.getMessage());
    }

    @Test
    void throwCheckedExceptionWhereNoCheckedExceptionIsThrown() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> expect(mock.throwsNothing(false)).andStubThrow(new CheckedException()));
        assertEquals("last method called on mock cannot throw " + this.getClass().getName()
                + "$CheckedException", e.getMessage());
    }

    @Test
    void throwWrongCheckedException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> expect(mock.throwsIOException(0)).andStubThrow(new CheckedException()));
        assertEquals("last method called on mock cannot throw " + this.getClass().getName()
                + "$CheckedException", e.getMessage());
    }
}
