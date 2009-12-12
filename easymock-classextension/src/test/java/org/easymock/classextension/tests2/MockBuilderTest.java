/*
 * Copyright (c) 2003-2009 Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.classextension.tests2;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.easymock.classextension.ConstructorArgs;
import org.easymock.classextension.IMocksControl;
import org.easymock.classextension.internal.ClassExtensionHelper;
import org.easymock.classextension.internal.MockBuilder;
import org.junit.Before;
import org.junit.Test;

public class MockBuilderTest {

    private MockBuilder<ArrayList<String>> builder;

    private ArrayList<String> mock;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        builder = new MockBuilder(ArrayList.class);
    }

    @Test
    public void testAddMockedMethod() throws NoSuchMethodException {
        builder.addMockedMethod(ArrayList.class.getMethod("size"))
                .addMockedMethod("contains").addMockedMethod("add",
                        Object.class).addMockedMethods("clear", "isEmpty")
                .addMockedMethods(ArrayList.class.getMethod("get", int.class),
                        ArrayList.class.getMethod("indexOf", Object.class));

        mock = builder.createMock();

        expect(mock.size()).andReturn(3);
        expect(mock.contains("test")).andReturn(true);
        expect(mock.add("added")).andReturn(true);
        mock.clear();
        expect(mock.isEmpty()).andReturn(false);
        expect(mock.get(1)).andReturn("result");
        expect(mock.indexOf("t")).andReturn(2);

        replay(mock);

        assertEquals(3, mock.size());
        assertEquals(true, mock.contains("test"));
        assertEquals(true, mock.add("added"));
        mock.clear();
        assertEquals(false, mock.isEmpty());
        assertEquals("result", mock.get(1));
        assertEquals(2, mock.indexOf("t"));

        verify(mock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMethod_NotExisting() {
        builder.addMockedMethod("..");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMethodWithParams_NotExisting() {
        builder.addMockedMethod("..", String.class);
    }

    @Test
    public void testWithConstructorParams() {
        builder.withConstructor(int.class).withArgs(-3);
        try {
            builder.createMock();
            fail("instantiation should fail because of negative");
        } catch (RuntimeException e) {
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithConstructor_WrongClass() {
        builder.withConstructor(long.class);
    }

    @Test
    public void testWithConstructor() throws NoSuchMethodException {
        builder.withConstructor(ArrayList.class.getConstructor(int.class))
                .withArgs(-3);
        try {
            builder.createMock();
            fail("instantiation should fail because of negative");
        } catch (RuntimeException e) {
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testWithConstructor_Twice() {
        builder.withConstructor(int.class).withConstructor(int.class);
    }

    @Test
    public void testWithConstructorConstructorArgs()
            throws NoSuchMethodException {
        ConstructorArgs args = new ConstructorArgs(ArrayList.class
                .getConstructor(int.class), Integer.valueOf(-3));
        builder.withConstructor(args);
        try {
            builder.createMock();
            fail("instantiation should fail because of negative");
        } catch (RuntimeException e) {
        }
    }

    @Test
    public void testWithConstructorWithArgs() throws NoSuchMethodException {
        builder.withConstructor(-3);
        try {
            builder.createMock();
            fail("instantiation should fail because of negative");
        } catch (RuntimeException e) {
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithConstructorWithArgs_NotExisting()
            throws NoSuchMethodException {
        builder.withConstructor("string");
    }

    @Test
    public void testWithArgsTwice() {
        try {
            builder.withConstructor(int.class).withArgs(3).withArgs(2);
            fail("withArgs called twice");
        } catch (IllegalStateException e) {
            assertEquals(
                    "Trying to define the constructor arguments more than once.",
                    e.getMessage());
        }
    }

    @Test
    public void testWithArgs_WithoutConstructor() {
        try {
            builder.withArgs(2);
            fail("withArgs without constructor");
        } catch (IllegalStateException e) {
            assertEquals(
                    "Trying to define constructor arguments without first setting their type.",
                    e.getMessage());
        }
    }

    @Test
    public void testCreateMockIMocksControl() {
        IMocksControl ctrl = createControl();
        mock = builder.createMock(ctrl);
        assertSame(ClassExtensionHelper.getControl(mock), ctrl);
    }

    @Test
    public void testCreateMock() {
        mock = builder.addMockedMethod("size").addMockedMethod("toString")
                .createMock();
        replay(mock);
        try {
            mock.size();
            fail("Unexpected call");
        } catch (AssertionError e) {
        }
    }

    @Test
    public void testCreateNiceMock() {
        mock = builder.addMockedMethod("size").addMockedMethod("toString")
                .createNiceMock();
        replay(mock);
        assertEquals(0, mock.size());
        verify(mock);
    }

    @Test
    public void testCreateStrictMock() {
        mock = builder.addMockedMethod("size").addMockedMethod("clear")
                .addMockedMethod("toString")
                .createStrictMock();
        expect(mock.size()).andReturn(1);
        mock.clear();
        replay(mock);
        try {
            mock.clear();
            fail("Unexpected call");
        } catch (AssertionError e) {
        }
    }

    @Test
    public void testCreateMockStringIMocksControl() {
        IMocksControl ctrl = createControl();
        mock = builder.addMockedMethod("toString").createMock("myName", ctrl);
        assertSame(ClassExtensionHelper.getControl(mock), ctrl);
        assertTrue(mock.toString().contains("myName"));
    }

    @Test
    public void testCreateMockString() {
        mock = builder.addMockedMethod("size").addMockedMethod("toString")
                .createMock("myName");
        replay(mock);
        try {
            mock.size();
            fail("Unexpected call");
        } catch (AssertionError e) {
            assertTrue(e.getMessage().contains("myName"));
        }
    }

    @Test
    public void testCreateNiceMockString() {
        mock = builder.addMockedMethod("size").addMockedMethod("toString")
                .createNiceMock("myName");
        replay(mock);
        assertEquals(0, mock.size());
        verify(mock);
        assertTrue(mock.toString().contains("myName"));
    }

    @Test
    public void testCreateStrictMockString() throws Throwable {
        mock = builder.addMockedMethod("size").addMockedMethod("clear")
                .addMockedMethod("toString").createStrictMock("myName");
        expect(mock.size()).andReturn(1);
        mock.clear();
        replay(mock);
        try {
            mock.clear();
            fail("Unexpected call");
        } catch (AssertionError e) {
            assertTrue(e.getMessage().contains("myName"));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateMock_ConstructorWithoutArgs() {
        builder.withConstructor(int.class).createMock();
    }
}
