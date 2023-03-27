/*
 * Copyright 2001-2023 the original author or authors.
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class NiceMockControlTest {

    private IMethods mock;

    @BeforeEach
    public void setup() {
        mock = createNiceMock(IMethods.class);
        replay(mock);
    }

    @Test
    public void defaultReturnValueBoolean() {
        Assertions.assertFalse(mock.booleanReturningMethod(12));
        verify(mock);
    }

    @Test
    public void defaultReturnValueFloat() {
        Assertions.assertEquals(0.0f, mock.floatReturningMethod(12), 0.0f);
        verify(mock);
    }

    @Test
    public void defaultReturnValueDouble() {
        Assertions.assertEquals(0.0d, mock.doubleReturningMethod(12), 0.0d);
        verify(mock);
    }

    @Test
    public void defaultReturnValueObject() {
        Assertions.assertNull(mock.objectReturningMethod(12));
        verify(mock);
    }
}
