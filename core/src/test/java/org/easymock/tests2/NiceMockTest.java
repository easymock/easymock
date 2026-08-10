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
package org.easymock.tests2;

import org.easymock.tests.IMethods;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author OFFIS, Tammo Freese
 */
class NiceMockTest {

    IMethods mock;

    @BeforeEach
    void setup() {
        mock = createNiceMock(IMethods.class);
        replay(mock);
    }

    @Test
    void defaultReturnValueBoolean() {
        assertFalse(mock.booleanReturningMethod(12));
        verify(mock);
    }

    @Test
    void defaultReturnValueFloat() {
        assertEquals(0.0f, mock.floatReturningMethod(12), 0.0f);
        verify(mock);
    }

    @Test
    void defaultReturnValueDouble() {
        assertEquals(0.0d, mock.doubleReturningMethod(12), 0.0d);
        verify(mock);
    }

    @Test
    void defaultReturnValueObject() {
        assertNull(mock.objectReturningMethod(12));
        verify(mock);
    }
}
