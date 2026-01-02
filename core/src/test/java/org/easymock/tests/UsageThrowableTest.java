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

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

/**
 * @author OFFIS, Tammo Freese
 */
class UsageThrowableTest {

    private IMethods mock;

    @BeforeEach
    void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    void noUpperLimit() {
        mock.simpleMethodWithArgument("1");
        expectLastCall().atLeastOnce();
        mock.simpleMethodWithArgument("2");
        replay(mock);
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("1");
        verify(mock);
    }

    @Test
    void throwRuntimeException() {
        testThrowUncheckedException(new RuntimeException());
    }

    @Test
    void throwSubclassOfRuntimeException() {
        testThrowUncheckedException(new RuntimeException() {
            private static final long serialVersionUID = 1L;
        });
    }

    @Test
    void throwError() {
        testThrowUncheckedException(new Error());
    }

    @Test
    void throwSubclassOfError() {
        testThrowUncheckedException(new Error() {
            private static final long serialVersionUID = 1L;
        });
    }

    private void testThrowUncheckedException(Throwable throwable) {
        expect(mock.throwsNothing(true)).andReturn("true");
        expect(mock.throwsNothing(false)).andThrow(throwable);

        replay(mock);

        Throwable expected = assertThrows(Throwable.class, () -> mock.throwsNothing(false));
        assertSame(throwable, expected);

        assertEquals("true", mock.throwsNothing(true));
    }

    @Test
    void throwCheckedException() throws IOException {
        testThrowCheckedException(new IOException());
    }

    @Test
    void throwSubclassOfCheckedException() throws IOException {
        testThrowCheckedException(new IOException() {
            private static final long serialVersionUID = 1L;
        });
    }

    private void testThrowCheckedException(IOException expected) throws IOException {
        expect(mock.throwsIOException(0)).andReturn("Value 0");
        expect(mock.throwsIOException(1)).andThrow(expected);
        expect(mock.throwsIOException(2)).andReturn("Value 2");

        replay(mock);

        assertEquals("Value 0", mock.throwsIOException(0));
        assertEquals("Value 2", mock.throwsIOException(2));

        IOException exception = assertThrows(IOException.class, () -> mock.throwsIOException(1));
        assertSame(exception, expected);
    }

    @Test
    void throwAfterReturnValue() {
        RuntimeException expectedException = new RuntimeException();
        expect(mock.throwsNothing(false)).andReturn("").andThrow(expectedException);

        replay(mock);

        assertEquals("", mock.throwsNothing(false));

        RuntimeException actualException = assertThrows(RuntimeException.class, () -> mock.throwsNothing(false));
        assertSame(expectedException, actualException);

        verify(mock);
    }

}
