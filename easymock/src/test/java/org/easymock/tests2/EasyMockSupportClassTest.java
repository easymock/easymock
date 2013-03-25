/**
 * Copyright 2001-2013 the original author or authors.
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
package org.easymock.tests2;

import static org.easymock.EasyMock.*;
import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.easymock.ConstructorArgs;
import org.easymock.EasyMockSupport;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Henri Tremblay
 */
@SuppressWarnings("deprecation")
public class EasyMockSupportClassTest extends EasyMockSupport {

    public static class ToMock {
        public int foo() {
            return 5;
        }
    }

    private Method foo;

    private ConstructorArgs args;

    @Before
    public void setUp() throws Exception {
        foo = ToMock.class.getMethod("foo");
        args = new ConstructorArgs(ToMock.class.getConstructor());
    }

    @Test
    public void testCreateStrictControl() {
        final IMocksControl ctrl = createStrictControl();
        assertThat(ctrl.createMock(ToMock.class), is(ToMock.class));
    }

    @Test
    public void testCreateControl() {
        final IMocksControl ctrl = createControl();
        assertThat(ctrl.createMock(ToMock.class), is(ToMock.class));
    }

    @Test
    public void testCreateNiceControl() {
        final IMocksControl ctrl = createNiceControl();
        assertThat(ctrl.createMock(ToMock.class), is(ToMock.class));
    }

    @Test
    public void testCreateStrictMockClassOfTMethodArray() {
        assertThat(createStrictMock(ToMock.class, foo), is(ToMock.class));
    }

    @Test
    public void testCreateStrictMockStringClassOfTMethodArray() {
        assertThat(createStrictMock("myMock", ToMock.class, foo), is(ToMock.class));
    }

    @Test
    public void testCreateStrictMockClassOfTConstructorArgsMethodArray() {
        assertThat(createStrictMock(ToMock.class, args, foo), is(ToMock.class));
    }

    @Test
    public void testCreateStrictMockStringClassOfTConstructorArgsMethodArray() {
        assertThat(createStrictMock("myMock", ToMock.class, args, foo), is(ToMock.class));
    }

    @Test
    public void testCreateMockClassOfTMethodArray() {
        assertThat(createMock(ToMock.class, foo), is(ToMock.class));
    }

    @Test
    public void testCreateMockStringClassOfTMethodArray() {
        assertThat(createMock("myMock", ToMock.class, foo), is(ToMock.class));
    }

    @Test
    public void testCreateMockClassOfTConstructorArgsMethodArray() {
        assertThat(createMock(ToMock.class, args, foo), is(ToMock.class));
    }

    @Test
    public void testCreateMockStringClassOfTConstructorArgsMethodArray() {
        assertThat(createMock("myMock", ToMock.class, args, foo), is(ToMock.class));
    }

    @Test
    public void testCreateNiceMockClassOfTMethodArray() {
        assertThat(createNiceMock(ToMock.class, foo), is(ToMock.class));
    }

    @Test
    public void testCreateNiceMockStringClassOfTMethodArray() {
        assertThat(createNiceMock("myMock", ToMock.class, foo), is(ToMock.class));
    }

    @Test
    public void testCreateNiceMockClassOfTConstructorArgsMethodArray() {
        assertThat(createNiceMock(ToMock.class, args, foo), is(ToMock.class));
    }

    @Test
    public void testCreateNiceMockStringClassOfTConstructorArgsMethodArray() {
        assertThat(createNiceMock("myMock", ToMock.class, args, foo), is(ToMock.class));
    }

    @Test
    public void testCreateStrictMockClassOfT() {
        assertThat(createStrictMock(ToMock.class), is(ToMock.class));
    }

    @Test
    public void testCreateStrictMockStringClassOfT() {
        assertThat(createStrictMock("myMock", ToMock.class), is(ToMock.class));
    }

    @Test
    public void testCreateMockClassOfT() {
        assertThat(createMock(ToMock.class), is(ToMock.class));
    }

    @Test
    public void testCreateMockStringClassOfT() {
        assertThat(createMock("myMock", ToMock.class), is(ToMock.class));
    }

    @Test
    public void testCreateNiceMockClassOfT() {
        assertThat(createNiceMock(ToMock.class), is(ToMock.class));
    }

    @Test
    public void testCreateNiceMockStringClassOfT() {
        assertThat(createNiceMock("myMock", ToMock.class), is(ToMock.class));
    }

    @Test
    public void testAll() {
        final ToMock t = createMock(ToMock.class);
        expect(t.foo()).andReturn(1);
        replayAll();
        t.foo();
        verifyAll();
        resetAll();
        resetAllToDefault();
        resetAllToNice();
        resetAllToStrict();
    }

    @Test
    public void testCreateMockBuilder() {
        final ToMock t = createMockBuilder(ToMock.class).addMockedMethod(foo).createMock();
        expect(t.foo()).andReturn(1);
        replayAll();
        assertEquals(1, t.foo());
        verifyAll();
    }

    @Test
    public void testCreateMockBuilder_existingControl() {
        final IMocksControl ctrl = createControl(); // ctrl registered once here
        final ToMock t = createMockBuilder(ToMock.class).addMockedMethod(foo).createMock(ctrl); // should not be registered a second time here
        expect(t.foo()).andReturn(1);
        replayAll();
        assertEquals(1, t.foo());
        verifyAll();
    }

    @Test
    public void testAllMockBuilderFlavors() {
        final ToMock t1 = createMockBuilder(ToMock.class).addMockedMethod(foo).createMock();
        final ToMock t2 = createMockBuilder(ToMock.class).addMockedMethod(foo).createNiceMock();
        final ToMock t3 = createMockBuilder(ToMock.class).addMockedMethod(foo).createStrictMock();
        expect(t1.foo()).andReturn(1);
        expect(t2.foo()).andReturn(2);
        expect(t3.foo()).andReturn(3);
        replayAll();
        assertEquals(1, t1.foo());
        assertEquals(2, t2.foo());
        assertEquals(3, t3.foo());
        verifyAll();
    }
}
