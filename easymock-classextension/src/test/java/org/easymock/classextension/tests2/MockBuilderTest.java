/**
 * Copyright 2003-2013 the original author or authors.
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
package org.easymock.classextension.tests2;

import static org.easymock.classextension.EasyMock.*;
import org.easymock.internal.MocksControl;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.easymock.classextension.ConstructorArgs;
import org.easymock.classextension.IMocksControl;
import org.easymock.classextension.internal.MockBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Henri Tremblay
 */
@SuppressWarnings("deprecation")
public class MockBuilderTest {

    private MockBuilder<ArrayList<String>> builder;

    private ArrayList<String> mock;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Before
    public void setUp() throws Exception {
        builder = new MockBuilder(ArrayList.class);
    }

    @Test
    public void testAddMockedMethod() throws NoSuchMethodException {
        builder.addMockedMethod(ArrayList.class.getMethod("size"))
                .addMockedMethod("contains")
                .addMockedMethod("add", Object.class)
                .addMockedMethods("clear", "isEmpty")
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
        } catch (final RuntimeException e) {
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithConstructor_WrongClass() {
        builder.withConstructor(long.class);
    }

    @Test
    public void testWithEmptyConstructor() throws Exception {
        mock = builder.withConstructor().createMock();
        final Field field = ArrayList.class.getDeclaredField("elementData");
        field.setAccessible(true);
        final int expected = ((Object[]) field.get(new ArrayList<String>())).length;
        final int actual = ((Object[]) field.get(mock)).length;
        assertEquals(expected, actual);
    }

    @Test
    public void testWithEmptyConstructor_NoEmptyConstructor() throws Exception {
        try {
            createMockBuilder(Integer.class).withConstructor().createMock();
            fail("no empty constructor should be found");
        } catch (final IllegalArgumentException e) {
        }
    }

    @Test
    public void testWithConstructor() throws NoSuchMethodException {
        builder.withConstructor(ArrayList.class.getConstructor(int.class)).withArgs(-3);
        try {
            builder.createMock();
            fail("instantiation should fail because of negative");
        } catch (final RuntimeException e) {
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testWithConstructor_Twice() {
        builder.withConstructor(int.class).withConstructor(int.class);
    }

    @Test
    public void testWithConstructorConstructorArgs() throws NoSuchMethodException {
        final ConstructorArgs args = new ConstructorArgs(ArrayList.class.getConstructor(int.class),
                Integer.valueOf(-3));
        builder.withConstructor(args);
        try {
            builder.createMock();
            fail("instantiation should fail because of negative");
        } catch (final RuntimeException e) {
        }
    }

    @Test
    public void testWithConstructorWithArgs() throws NoSuchMethodException {
        builder.withConstructor(-3);
        try {
            builder.createMock();
            fail("instantiation should fail because of negative");
        } catch (final RuntimeException e) {
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithConstructorWithArgs_NotExisting() throws NoSuchMethodException {
        builder.withConstructor("string");
    }

    @Test
    public void testWithArgsTwice() {
        try {
            builder.withConstructor(int.class).withArgs(3).withArgs(2);
            fail("withArgs called twice");
        } catch (final IllegalStateException e) {
            assertEquals("Trying to define the constructor arguments more than once.", e.getMessage());
        }
    }

    @Test
    public void testWithArgs_WithoutConstructor() {
        try {
            builder.withArgs(2);
            fail("withArgs without constructor");
        } catch (final IllegalStateException e) {
            assertEquals("Trying to define constructor arguments without first setting their type.",
                    e.getMessage());
        }
    }

    @Test
    public void testCreateMockIMocksControl() {
        final IMocksControl ctrl = createControl();
        mock = builder.createMock(ctrl);
        assertSame(MocksControl.getControl(mock), ctrl);
    }

    @Test
    public void testCreateMock() {
        mock = builder.addMockedMethod("size").addMockedMethod("toString").createMock();
        replay(mock);
        try {
            mock.size();
            fail("Unexpected call");
        } catch (final AssertionError e) {
        }
    }

    @Test
    public void testCreateNiceMock() {
        mock = builder.addMockedMethod("size").addMockedMethod("toString").createNiceMock();
        replay(mock);
        assertEquals(0, mock.size());
        verify(mock);
    }

    @Test
    public void testCreateStrictMock() {
        mock = builder.addMockedMethod("size").addMockedMethod("clear").addMockedMethod("toString")
                .createStrictMock();
        expect(mock.size()).andReturn(1);
        mock.clear();
        replay(mock);
        try {
            mock.clear();
            fail("Unexpected call");
        } catch (final AssertionError e) {
        }
    }

    @Test
    public void testCreateMockStringIMocksControl() {
        final IMocksControl ctrl = createControl();
        mock = builder.addMockedMethod("toString").createMock("myName", ctrl);
        assertSame(MocksControl.getControl(mock), ctrl);
        assertTrue(mock.toString().contains("myName"));
    }

    @Test
    public void testCreateMockString() {
        mock = builder.addMockedMethod("size").addMockedMethod("toString").createMock("myName");
        replay(mock);
        try {
            mock.size();
            fail("Unexpected call");
        } catch (final AssertionError e) {
            assertTrue(e.getMessage().contains("myName"));
        }
    }

    @Test
    public void testCreateNiceMockString() {
        mock = builder.addMockedMethod("size").addMockedMethod("toString").createNiceMock("myName");
        replay(mock);
        assertEquals(0, mock.size());
        verify(mock);
        assertTrue(mock.toString().contains("myName"));
    }

    @Test
    public void testCreateStrictMockString() throws Throwable {
        mock = builder.addMockedMethod("size").addMockedMethod("clear").addMockedMethod("toString")
                .createStrictMock("myName");
        expect(mock.size()).andReturn(1);
        mock.clear();
        replay(mock);
        try {
            mock.clear();
            fail("Unexpected call");
        } catch (final AssertionError e) {
            assertTrue(e.getMessage().contains("myName"));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateMock_ConstructorWithoutArgs() {
        builder.withConstructor(int.class).createMock();
    }
}
