package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.IMockBuilder;
import org.easymock.MockType;
import org.easymock.internal.MocksControl;
import org.junit.Test;

public class MockTypeTest {

    private final EasyMockSupport support = new EasyMockSupport();

    private final IMockBuilder<MockTypeTest> builder = EasyMock.createMockBuilder(MockTypeTest.class);

    @Test
    public void fromEasyMockClass() {
        final IMethods mock = EasyMock.createMock(MockType.STRICT, IMethods.class);
        final MockType type = MocksControl.getControl(mock).getType();
        assertEquals(MockType.STRICT, type);
        assertEquals("EasyMock for interface " + IMethods.class.getName(), mock.toString());
    }

    @Test
    public void fromEasyMockClassWithName() {
        final IMethods mock = EasyMock.createMock("test", MockType.STRICT, IMethods.class);
        final MockType type = MocksControl.getControl(mock).getType();
        assertEquals(MockType.STRICT, type);
        assertEquals("test", mock.toString());
    }

    @Test
    public void fromEasyMockControlWithName() {
        final MocksControl ctrl = (MocksControl) EasyMock.createControl(MockType.STRICT);
        assertEquals(MockType.STRICT, ctrl.getType());
    }

    @Test
    public void fromEasyMockSupportClass() {
        final IMethods mock = support.createMock(MockType.STRICT, IMethods.class);
        final MockType type = MocksControl.getControl(mock).getType();
        assertEquals(MockType.STRICT, type);
        assertEquals("EasyMock for interface " + IMethods.class.getName(), mock.toString());
    }

    @Test
    public void fromEasyMockSupportClassWithName() {
        final IMethods mock = support.createMock("test", MockType.STRICT, IMethods.class);
        final MockType type = MocksControl.getControl(mock).getType();
        assertEquals(MockType.STRICT, type);
        assertEquals("test", mock.toString());
    }

    @Test
    public void fromEasyMockSupportControlWithName() {
        final MocksControl ctrl = (MocksControl) support.createControl(MockType.STRICT);
        assertEquals(MockType.STRICT, ctrl.getType());
    }

    // The two following tests are showing a strange behavior. The toString doesn't return the
    // default EasyMock implementation. I won't change it right now but it doesn't feel right
    @Test
    public void fromMockBuilderClass() {
        final MockTypeTest mock = builder.addMockedMethod("toString").createMock(MockType.STRICT);
        final MockType type = MocksControl.getControl(mock).getType();
        assertEquals(MockType.STRICT, type);
        assertEquals("EasyMock for class " + MockTypeTest.class.getName(), mock.toString());
    }

    @Test
    public void fromMockBuilderClassWithName() {
        final MockTypeTest mock = builder.addMockedMethod("toString").createMock("test", MockType.STRICT);
        final MockType type = MocksControl.getControl(mock).getType();
        assertEquals(MockType.STRICT, type);
        assertEquals("test", mock.toString());
    }

    @Override
    public String toString() {
        return "this is a toString";
    }
}
