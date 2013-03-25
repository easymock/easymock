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

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class UsageThrowableTest {

    private IMethods mock;

    @Before
    public void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    public void noUpperLimit() {
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
    public void throwRuntimeException() {
        testThrowUncheckedException(new RuntimeException());
    }

    @Test
    public void throwSubclassOfRuntimeException() {
        testThrowUncheckedException(new RuntimeException() {
            private static final long serialVersionUID = 1L;
        });
    }

    @Test
    public void throwError() {
        testThrowUncheckedException(new Error());
    }

    @Test
    public void throwSubclassOfError() {
        testThrowUncheckedException(new Error() {
            private static final long serialVersionUID = 1L;
        });
    }

    private void testThrowUncheckedException(final Throwable throwable) {
        expect(mock.throwsNothing(true)).andReturn("true");
        expect(mock.throwsNothing(false)).andThrow(throwable);

        replay(mock);

        try {
            mock.throwsNothing(false);
            fail("Trowable expected");
        } catch (final Throwable expected) {
            assertSame(throwable, expected);
        }

        assertEquals("true", mock.throwsNothing(true));
    }

    @Test
    public void throwCheckedException() throws IOException {
        testThrowCheckedException(new IOException());
    }

    @Test
    public void throwSubclassOfCheckedException() throws IOException {
        testThrowCheckedException(new IOException() {
            private static final long serialVersionUID = 1L;
        });
    }

    private void testThrowCheckedException(final IOException expected) throws IOException {
        try {
            expect(mock.throwsIOException(0)).andReturn("Value 0");
            expect(mock.throwsIOException(1)).andThrow(expected);
            expect(mock.throwsIOException(2)).andReturn("Value 2");
        } catch (final IOException e) {
            fail("Unexpected Exception");
        }

        replay(mock);

        assertEquals("Value 0", mock.throwsIOException(0));
        assertEquals("Value 2", mock.throwsIOException(2));

        try {
            mock.throwsIOException(1);
            fail("IOException expected");
        } catch (final IOException expectedException) {
            assertSame(expectedException, expected);
        }
    }

    @Test
    public void throwAfterReturnValue() {
        final RuntimeException expectedException = new RuntimeException();
        expect(mock.throwsNothing(false)).andReturn("").andThrow(expectedException);

        replay(mock);

        assertEquals("", mock.throwsNothing(false));

        try {
            mock.throwsNothing(false);
            fail("RuntimeException expected");
        } catch (final RuntimeException actualException) {
            assertSame(expectedException, actualException);
        }

        verify(mock);
    }

}