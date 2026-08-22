package org.tests;

import org.easymock.EasyMockSupport;
import org.easymock.MockType;
import org.easymock.internal.MocksControl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReifiedMockSupportTest extends EasyMockSupport {

    @Test
    void testMock() {
        List<String> mock = mock();
        assertInstanceOf(List.class, mock);
        assertEquals(MockType.DEFAULT, MocksControl.getControl(mock).getType());
    }

    @Test
    void testNiceMock() {
        List<String> mock = niceMock();
        assertInstanceOf(List.class, mock);
        assertEquals(MockType.NICE, MocksControl.getControl(mock).getType());
    }

    @Test
    void testStrictMock() {
        List<String> mock = strictMock();
        assertInstanceOf(List.class, mock);
        assertEquals(MockType.STRICT, MocksControl.getControl(mock).getType());
    }

    @Test
    void testMockWithName() {
        List<String> mock = mock("myMock");
        assertInstanceOf(List.class, mock);
        assertTrue(mock.toString().contains("myMock"));
        assertEquals(MockType.DEFAULT, MocksControl.getControl(mock).getType());
    }

    @Test
    void testNiceMockWithName() {
        List<String> mock = niceMock("myNiceMock");
        assertInstanceOf(List.class, mock);
        assertTrue(mock.toString().contains("myNiceMock"));
        assertEquals(MockType.NICE, MocksControl.getControl(mock).getType());
    }

    @Test
    void testStrictMockWithName() {
        List<String> mock = strictMock("myStrictMock");
        assertInstanceOf(List.class, mock);
        assertTrue(mock.toString().contains("myStrictMock"));
        assertEquals(MockType.STRICT, MocksControl.getControl(mock).getType());
    }

    @Test
    void testMockWithMockTypeStrict() {
        List<String> mock = mock(MockType.STRICT);
        assertInstanceOf(List.class, mock);
        assertEquals(MockType.STRICT, MocksControl.getControl(mock).getType());
    }

    @Test
    void testMockWithMockTypeStrictAndName() {
        List<String> mock = mock("myStrictMock", MockType.STRICT);
        assertInstanceOf(List.class, mock);
        assertTrue(mock.toString().contains("myStrictMock"));
        assertEquals(MockType.STRICT, MocksControl.getControl(mock).getType());
    }

}
