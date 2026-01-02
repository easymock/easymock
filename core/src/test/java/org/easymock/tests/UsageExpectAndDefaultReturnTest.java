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

import static org.easymock.EasyMock.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Same as UsageExpectAndReturnTest except that each mocked method is called
 * twice to make sure the defaulting works fine.
 *
 * @author OFFIS, Tammo Freese
 */
class UsageExpectAndDefaultReturnTest {

    private IMethods mock;

    @BeforeEach
    void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    void booleanType() {
        expect(mock.booleanReturningMethod(4)).andStubReturn(true);
        replay(mock);
        Assertions.assertTrue(mock.booleanReturningMethod(4));
        Assertions.assertTrue(mock.booleanReturningMethod(4));
        verify(mock);
    }

    @Test
    void longType() {
        expect(mock.longReturningMethod(4)).andStubReturn(12L);
        replay(mock);
        Assertions.assertEquals(12L, mock.longReturningMethod(4));
        Assertions.assertEquals(12L, mock.longReturningMethod(4));
        verify(mock);
    }

    @Test
    void floatType() {
        expect(mock.floatReturningMethod(4)).andStubReturn(12f);
        replay(mock);
        Assertions.assertEquals(12f, mock.floatReturningMethod(4), 0f);
        Assertions.assertEquals(12f, mock.floatReturningMethod(4), 0f);
        verify(mock);
    }

    @Test
    void doubleType() {
        expect(mock.doubleReturningMethod(4)).andStubReturn(12.0);
        replay(mock);
        Assertions.assertEquals(12.0, mock.doubleReturningMethod(4), 0.0);
        Assertions.assertEquals(12.0, mock.doubleReturningMethod(4), 0.0);
        verify(mock);
    }

    @Test
    void objectType() {
        expect(mock.objectReturningMethod(4)).andStubReturn("12");
        replay(mock);
        Assertions.assertEquals("12", mock.objectReturningMethod(4));
        Assertions.assertEquals("12", mock.objectReturningMethod(4));
        verify(mock);
    }

}
