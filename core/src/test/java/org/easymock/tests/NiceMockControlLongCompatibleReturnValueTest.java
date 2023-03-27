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
public class NiceMockControlLongCompatibleReturnValueTest {

    private IMethods mock;

    @BeforeEach
    public void setup() {
        mock = createNiceMock(IMethods.class);
        replay(mock);
    }

    @Test
    public void byteReturningValue() {
        Assertions.assertEquals((byte) 0, mock.byteReturningMethod(12));
        verify(mock);
    }

    @Test
    public void shortReturningValue() {
        Assertions.assertEquals((short) 0, mock.shortReturningMethod(12));
        verify(mock);
    }

    @Test
    public void charReturningValue() {
        Assertions.assertEquals((char) 0, mock.charReturningMethod(12));
        verify(mock);
    }

    @Test
    public void intReturningValue() {
        Assertions.assertEquals(0, mock.intReturningMethod(12));
        verify(mock);
    }

    @Test
    public void longReturningValue() {
        Assertions.assertEquals(0, mock.longReturningMethod(12));
        verify(mock);
    }
}
