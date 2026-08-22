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
