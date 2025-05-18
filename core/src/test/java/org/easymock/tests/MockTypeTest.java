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
import org.easymock.EasyMockSupport;
import org.easymock.IMockBuilder;
import org.easymock.MockType;
import org.easymock.internal.MocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test mocks create with a MockType
 *
 * @author Henri Tremblay
 */
public class MockTypeTest {

    private final EasyMockSupport support = new EasyMockSupport();

    private final IMockBuilder<MockTypeTest> builder = EasyMock.createMockBuilder(MockTypeTest.class);

    @Test
    void fromEasyMockClass() {
        IMethods mock = EasyMock.createMock(MockType.STRICT, IMethods.class);
        MockType type = MocksControl.getControl(mock).getType();
        Assertions.assertEquals(MockType.STRICT, type);
        Assertions.assertEquals("EasyMock for interface " + IMethods.class.getName(), mock.toString());
    }

    @Test
    void fromEasyMockClassWithName() {
        IMethods mock = EasyMock.createMock("test", MockType.STRICT, IMethods.class);
        MockType type = MocksControl.getControl(mock).getType();
        Assertions.assertEquals(MockType.STRICT, type);
        Assertions.assertEquals("test", mock.toString());
    }

    @Test
    void fromEasyMockControlWithName() {
        MocksControl ctrl = (MocksControl) EasyMock.createControl(MockType.STRICT);
        Assertions.assertEquals(MockType.STRICT, ctrl.getType());
    }

    @Test
    void fromEasyMockSupportClass() {
        IMethods mock = support.createMock(MockType.STRICT, IMethods.class);
        MockType type = MocksControl.getControl(mock).getType();
        Assertions.assertEquals(MockType.STRICT, type);
        Assertions.assertEquals("EasyMock for interface " + IMethods.class.getName(), mock.toString());
    }

    @Test
    void fromEasyMockSupportClassWithName() {
        IMethods mock = support.createMock("test", MockType.STRICT, IMethods.class);
        MockType type = MocksControl.getControl(mock).getType();
        Assertions.assertEquals(MockType.STRICT, type);
        Assertions.assertEquals("test", mock.toString());
    }

    @Test
    void fromEasyMockSupportControlWithName() {
        MocksControl ctrl = (MocksControl) support.createControl(MockType.STRICT);
        Assertions.assertEquals(MockType.STRICT, ctrl.getType());
    }

    // The two following tests are showing a strange behavior. The toString doesn't return the
    // default EasyMock implementation. I won't change it right now, but it doesn't feel right
    @Test
    void fromMockBuilderClass() {
        MockTypeTest mock = builder.addMockedMethod("toString").createMock(MockType.STRICT);
        MockType type = MocksControl.getControl(mock).getType();
        Assertions.assertEquals(MockType.STRICT, type);
        Assertions.assertEquals("EasyMock for class " + MockTypeTest.class.getName(), mock.toString());
    }

    @Test
    void fromMockBuilderClassWithName() {
        MockTypeTest mock = builder.addMockedMethod("toString").createMock("test", MockType.STRICT);
        MockType type = MocksControl.getControl(mock).getType();
        Assertions.assertEquals(MockType.STRICT, type);
        Assertions.assertEquals("test", mock.toString());
    }

    @Override
    public String toString() {
        return "this is a toString";
    }
}
