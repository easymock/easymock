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

import org.easymock.EasyMock;
import org.easymock.MockType;
import org.easymock.internal.MocksControl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReifiedMockTest {

    @Test
    void testMock() {
        List<String> mock = EasyMock.mock();
        assertInstanceOf(List.class, mock);
        assertEquals(MockType.DEFAULT, MocksControl.getControl(mock).getType());
    }

    @Test
    void testNiceMock() {
        List<String> mock = EasyMock.niceMock();
        assertInstanceOf(List.class, mock);
        assertEquals(MockType.NICE, MocksControl.getControl(mock).getType());
    }

    @Test
    void testStrictMock() {
        List<String> mock = EasyMock.strictMock();
        assertInstanceOf(List.class, mock);
        assertEquals(MockType.STRICT, MocksControl.getControl(mock).getType());
    }

    @Test
    void testMockWithName() {
        List<String> mock = EasyMock.mock("myMock");
        assertInstanceOf(List.class, mock);
        assertTrue(mock.toString().contains("myMock"));
        assertEquals(MockType.DEFAULT, MocksControl.getControl(mock).getType());
    }

    @Test
    void testNiceMockWithName() {
        List<String> mock = EasyMock.niceMock("myNiceMock");
        assertInstanceOf(List.class, mock);
        assertTrue(mock.toString().contains("myNiceMock"));
        assertEquals(MockType.NICE, MocksControl.getControl(mock).getType());
    }

    @Test
    void testStrictMockWithName() {
        List<String> mock = EasyMock.strictMock("myStrictMock");
        assertInstanceOf(List.class, mock);
        assertTrue(mock.toString().contains("myStrictMock"));
        assertEquals(MockType.STRICT, MocksControl.getControl(mock).getType());
    }

    @Test
    void testMockWithMockTypeStrict() {
        List<String> mock = EasyMock.mock(MockType.STRICT);
        assertInstanceOf(List.class, mock);
        assertEquals(MockType.STRICT, MocksControl.getControl(mock).getType());
    }

    @Test
    void testMockWithMockTypeStrictAndName() {
        List<String> mock = EasyMock.mock("myStrictMock", MockType.STRICT);
        assertInstanceOf(List.class, mock);
        assertTrue(mock.toString().contains("myStrictMock"));
        assertEquals(MockType.STRICT, MocksControl.getControl(mock).getType());
    }

}
