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
package org.easymock.tests;

import org.easymock.EasyMock;
import org.easymock.IMockBuilder;
import org.easymock.IMocksControl;
import org.easymock.MockType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.partialMockBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Testing that all default methods on IMockBuilder are delegating to the right method.
 *
 * @author Henri Tremblay
 */
class IMockBuilderTest {

    private final IMockBuilder<IMockBuilderTest> builder = partialMockBuilder(IMockBuilderTest.class);

    private void assertMock(IMockBuilderTest mock, String name, MockType type) {
        Assertions.assertEquals(name, Util.getName(mock));
        Assertions.assertEquals(type, Util.getType(mock));
        Assertions.assertNotNull(Util.getControl(mock));
    }

    private void assertMock(IMockBuilderTest mock, String name, MockType type, IMocksControl control) {
        assertMock(mock, name, type);
        Assertions.assertSame(control, Util.getControl(mock));
    }

    @Test
    void testMock() {
        IMockBuilderTest mock = builder.mock();assertMock(mock, null, MockType.DEFAULT);
    }

    @Test
    void testNiceMock() {
        IMockBuilderTest mock = builder.niceMock();
        assertMock(mock, null, MockType.NICE);
    }

    @Test
    void testStrictMock() {
        IMockBuilderTest mock = builder.strictMock();
        assertMock(mock, null, MockType.STRICT);
    }

    @Test
    void testMockWithName() {
        IMockBuilderTest mock = builder.mock("a");
        assertMock(mock, "a", MockType.DEFAULT);
    }

    @Test
    void testNiceMockWithName() {
        IMockBuilderTest mock = builder.niceMock("a");
        assertMock(mock, "a", MockType.NICE);
    }

    @Test
    void testStrictMockWithName() {
        IMockBuilderTest mock = builder.strictMock("a");
        assertMock(mock, "a", MockType.STRICT);
    }

    @Test
    void testMockWithType() {
        IMockBuilderTest mock = builder.mock(MockType.NICE);
        assertMock(mock, null, MockType.NICE);
    }

    @Test
    void testMockWithNameAndType() {
        IMockBuilderTest mock = builder.mock("a", MockType.NICE);
        assertMock(mock, "a", MockType.NICE);
    }

    @Test
    void testMockWithControl() {
        IMocksControl control = EasyMock.createNiceControl();
        IMockBuilderTest mock = builder.mock(control);
        assertMock(mock, null, MockType.NICE, control);
    }

    @Test
    void testMockWithNameAndControl() {
        IMocksControl control = EasyMock.createNiceControl();
        IMockBuilderTest mock = builder.mock("a", control);
        assertMock(mock, "a", MockType.NICE, control);
    }
}
