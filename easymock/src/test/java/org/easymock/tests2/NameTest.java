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
package org.easymock.tests2;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.IMocksControl;
import org.easymock.tests.IMethods;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class NameTest {
    @Test
    public void nameForMock() {
        final IMethods mock = createMock("mock", IMethods.class);
        mock.simpleMethod();
        replay(mock);
        try {
            verify(mock);
        } catch (final AssertionError expected) {
            final String actualMessage = expected.getMessage();
            final String expectedMessage = "\n  Expectation failure on verify:\n    mock.simpleMethod(): expected: 1, actual: 0";
            assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void nameForStrictMock() {
        final IMethods mock = createStrictMock("mock", IMethods.class);
        mock.simpleMethod();
        replay(mock);
        try {
            verify(mock);
        } catch (final AssertionError expected) {
            final String actualMessage = expected.getMessage();
            final String expectedMessage = "\n  Expectation failure on verify:\n    mock.simpleMethod(): expected: 1, actual: 0";
            assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void nameForNiceMock() {
        final IMethods mock = createNiceMock("mock", IMethods.class);
        mock.simpleMethod();
        replay(mock);
        try {
            verify(mock);
        } catch (final AssertionError expected) {
            final String actualMessage = expected.getMessage();
            final String expectedMessage = "\n  Expectation failure on verify:\n    mock.simpleMethod(): expected: 1, actual: 0";
            assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void nameForMocksControl() {
        final IMocksControl control = createControl();
        final IMethods mock = control.createMock("mock", IMethods.class);
        mock.simpleMethod();
        replay(mock);
        try {
            verify(mock);
        } catch (final AssertionError expected) {
            final String actualMessage = expected.getMessage();
            final String expectedMessage = "\n  Expectation failure on verify:\n    mock.simpleMethod(): expected: 1, actual: 0";
            assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfNameIsNoValidJavaIdentifier() {
        try {
            createMock("no-valid-java-identifier", IMethods.class);
            throw new AssertionError();
        } catch (final IllegalArgumentException expected) {
            assertEquals("'no-valid-java-identifier' is not a valid Java identifier.", expected.getMessage());
        }
    }

}
