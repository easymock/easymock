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
 * Same as UsageExpectAndThrowTest except that each mocked method is called
 * twice to make sure the defaulting works fine.
 * 
 * @author OFFIS, Tammo Freese
 */
public class UsageExpectAndDefaultThrowTest {

    private IMethods mock;

    private static RuntimeException EXCEPTION = new RuntimeException();

    @Before
    public void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    public void booleanType() {
        expect(mock.booleanReturningMethod(4)).andStubThrow(EXCEPTION);
        replay(mock);
        try {
            mock.booleanReturningMethod(4);
            fail();
        } catch (final RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.booleanReturningMethod(4);
            fail();
        } catch (final RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    public void longType() {
        expect(mock.longReturningMethod(4)).andStubThrow(EXCEPTION);
        replay(mock);
        try {
            mock.longReturningMethod(4);
            fail();
        } catch (final RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.longReturningMethod(4);
            fail();
        } catch (final RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    public void floatType() {
        expect(mock.floatReturningMethod(4)).andStubThrow(EXCEPTION);
        replay(mock);
        try {
            mock.floatReturningMethod(4);
            fail();
        } catch (final RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.floatReturningMethod(4);
            fail();
        } catch (final RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    public void doubleType() {
        expect(mock.doubleReturningMethod(4)).andStubThrow(EXCEPTION);
        replay(mock);
        try {
            mock.doubleReturningMethod(4);
            fail();
        } catch (final RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.doubleReturningMethod(4);
            fail();
        } catch (final RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    public void object() {
        expect(mock.objectReturningMethod(4)).andStubThrow(EXCEPTION);
        replay(mock);
        try {
            mock.objectReturningMethod(4);
            fail();
        } catch (final RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.objectReturningMethod(4);
            fail();
        } catch (final RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    public void throwableAndDefaultThrowable() throws Exception {

        expect(mock.oneArg("1")).andThrow(new IllegalArgumentException());
        expect(mock.oneArg((String) anyObject())).andStubThrow(new IllegalStateException());

        replay(mock);

        try {
            mock.oneArg("1");
        } catch (final IllegalArgumentException ignored) {
        }
        try {
            mock.oneArg("1");
        } catch (final IllegalStateException ignored) {
        }
        try {
            mock.oneArg("2");
        } catch (final IllegalStateException ignored) {
        }
        verify(mock);
    }

}
