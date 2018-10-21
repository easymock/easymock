/*
 * Copyright 2001-2018 the original author or authors.
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
import org.easymock.MockType;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.fail;

/**
 * We are making sure here that the typing here is generic friendly. Not much assertions are made, if
 * it's compiling we are happy.
 *
 * @author Henri Tremblay
 */
public class TypingTest {

    private List<String> list;

    @Test
    public void testCreateMock() {
        list = EasyMock.createMock(List.class);
        list = EasyMock.createMock("a", List.class);
        list = EasyMock.createMock("a", MockType.DEFAULT, List.class);
        list = EasyMock.createMock(MockType.DEFAULT, List.class);
        list = EasyMock.createNiceMock(List.class);
        list = EasyMock.createNiceMock("a", List.class);
        list = EasyMock.createStrictMock(List.class);
        list = EasyMock.createStrictMock("a", List.class);
        list = EasyMock.createControl().createMock(List.class);
        list = EasyMock.createControl().createMock("a", List.class);
        list = EasyMock.createControl(MockType.DEFAULT).createMock(List.class);
        list = EasyMock.createControl(MockType.DEFAULT).createMock("a", List.class);
    }

    @Test
    public void testMock() {
        list = EasyMock.mock(List.class);
        list = EasyMock.mock("a", List.class);
        list = EasyMock.mock("a", MockType.DEFAULT, List.class);
        list = EasyMock.mock(MockType.DEFAULT, List.class);
        list = EasyMock.niceMock(List.class);
        list = EasyMock.niceMock("a", List.class);
        list = EasyMock.strictMock(List.class);
        list = EasyMock.strictMock("a", List.class);
    }

    @Test
    public void testSupportCreateMock() {
        EasyMockSupport support = new EasyMockSupport();

        list = support.createMock(List.class);
        list = support.createMock("a", List.class);
        list = support.createMock("a", MockType.DEFAULT, List.class);
        list = support.createMock(MockType.DEFAULT, List.class);
        list = support.createNiceMock(List.class);
        list = support.createNiceMock("a", List.class);
        list = support.createStrictMock(List.class);
        list = support.createStrictMock("a", List.class);
        list = support.createControl().createMock(List.class);
        list = support.createControl().createMock("a", List.class);
        list = support.createControl(MockType.DEFAULT).createMock(List.class);
        list = support.createControl(MockType.DEFAULT).createMock("a", List.class);
    }

    @Test
    public void testSupportMock() {
        EasyMockSupport support = new EasyMockSupport();

        list = support.mock(List.class);
        list = support.mock("a", List.class);
        list = support.mock("a", MockType.DEFAULT, List.class);
        list = support.mock(MockType.DEFAULT, List.class);
        list = support.niceMock(List.class);
        list = support.niceMock("a", List.class);
        list = support.strictMock(List.class);
        list = support.strictMock("a", List.class);
        list = support.createControl().createMock(List.class);
        list = support.createControl().createMock("a", List.class);
        list = support.createControl(MockType.DEFAULT).createMock(List.class);
        list = support.createControl(MockType.DEFAULT).createMock("a", List.class);
    }

    @Test
    public void testNonMatchingClass() {
        try {
            list = EasyMock.mock(Set.class);
            fail("Can't cast Set to List");
        }
        catch(ClassCastException e) {
            // Expected
        }
    }
}
