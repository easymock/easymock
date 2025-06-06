/*
 * Copyright 2001-2025 the original author or authors.
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.IMocksControl;
import org.easymock.tests.IMethods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author OFFIS, Tammo Freese
 */
class NameTest {
    @Test
    void nameForMock() {
        IMethods mock = createMock("mock", IMethods.class);
        mock.simpleMethod();
        replay(mock);
        AssertionError expected = assertThrows(AssertionError.class, () -> verify(mock));
        String actualMessage = expected.getMessage();
        String expectedMessage = "\n  Expectation failure on verify:\n    Mock named mock -> IMethods.simpleMethod(): expected: 1, actual: 0";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void nameForStrictMock() {
        IMethods mock = createStrictMock("mock", IMethods.class);
        mock.simpleMethod();
        replay(mock);
        AssertionError expected = assertThrows(AssertionError.class, () -> verify(mock));
        String actualMessage = expected.getMessage();
        String expectedMessage = "\n  Expectation failure on verify:\n    Mock named mock -> IMethods.simpleMethod(): expected: 1, actual: 0";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void nameForNiceMock() {
        IMethods mock = createNiceMock("mock", IMethods.class);
        mock.simpleMethod();
        replay(mock);
        AssertionError expected = assertThrows(AssertionError.class, () -> verify(mock));
        String actualMessage = expected.getMessage();
        String expectedMessage = "\n  Expectation failure on verify:\n    Mock named mock -> IMethods.simpleMethod(): expected: 1, actual: 0";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void nameForMocksControl() {
        IMocksControl control = createControl();
        IMethods mock = control.createMock("mock", IMethods.class);
        mock.simpleMethod();
        replay(mock);
        AssertionError expected = assertThrows(AssertionError.class, () -> verify(mock));
        String actualMessage = expected.getMessage();
        String expectedMessage = "\n  Expectation failure on verify:\n    Mock named mock -> IMethods.simpleMethod(): expected: 1, actual: 0";
        assertEquals(expectedMessage, actualMessage);
    }

}
