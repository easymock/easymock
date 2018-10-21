package org.easymock.tests;

import org.easymock.EasyMock;
import org.easymock.IMockBuilder;
import org.easymock.IMocksControl;
import org.easymock.MockType;
import org.junit.Test;

import static org.easymock.EasyMock.partialMockBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Testing that all default methods on IMockBuilder are delegating to the right method.
 *
 * @author Henri Tremblay
 */
public class IMockBuilderTest {

    private final IMockBuilder<IMockBuilderTest> builder = partialMockBuilder(IMockBuilderTest.class);

    private void assertMock(IMockBuilderTest mock, String name, MockType type) {
        assertEquals(name, Util.getName(mock));
        assertEquals(type, Util.getType(mock));
        assertNotNull(Util.getControl(mock));
    }

    private void assertMock(IMockBuilderTest mock, String name, MockType type, IMocksControl control) {
        assertMock(mock, name, type);
        assertSame(control, Util.getControl(mock));
    }
    @Test
    public void testMock() {
        IMockBuilderTest mock = builder.mock();
        assertMock(mock, null, MockType.DEFAULT);
    }

    @Test
    public void testNiceMock() {
        IMockBuilderTest mock = builder.niceMock();
        assertMock(mock, null, MockType.NICE);
    }

    @Test
    public void testStrictMock() {
        IMockBuilderTest mock = builder.strictMock();
        assertMock(mock, null, MockType.STRICT);
    }

    @Test
    public void testMockWithName() {
        IMockBuilderTest mock = builder.mock("a");
        assertMock(mock, "a", MockType.DEFAULT);
    }

    @Test
    public void testNiceMockWithName() {
        IMockBuilderTest mock = builder.niceMock("a");
        assertMock(mock, "a", MockType.NICE);
    }

    @Test
    public void testStrictMockWithName() {
        IMockBuilderTest mock = builder.strictMock("a");
        assertMock(mock, "a", MockType.STRICT);
    }

    @Test
    public void testMockWithType() {
        IMockBuilderTest mock = builder.mock(MockType.NICE);
        assertMock(mock, null, MockType.NICE);
    }

    @Test
    public void testMockWithNameAndType() {
        IMockBuilderTest mock = builder.mock("a", MockType.NICE);
        assertMock(mock, "a", MockType.NICE);
    }

    @Test
    public void testMockWithControl() {
        IMocksControl control = EasyMock.createNiceControl();
        IMockBuilderTest mock = builder.mock(control);
        assertMock(mock, null, MockType.NICE, control);
    }

    @Test
    public void testMockWithNameAndControl() {
        IMocksControl control = EasyMock.createNiceControl();
        IMockBuilderTest mock = builder.mock("a", control);
        assertMock(mock, "a", MockType.NICE, control);
    }
}
