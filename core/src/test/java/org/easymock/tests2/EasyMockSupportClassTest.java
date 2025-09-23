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
package org.easymock.tests2;

import org.easymock.EasyMockSupport;
import org.easymock.IMocksControl;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.easymock.EasyMock.*;
import static org.hamcrest.core.Is.isA;

/**
 * @author Henri Tremblay
 */
class EasyMockSupportClassTest extends EasyMockSupport {

    public static class ToMock {
        public int foo() {
            return 5;
        }
    }

    private Method foo;

    @BeforeEach
    void setUp() throws Exception {
        foo = ToMock.class.getMethod("foo");
    }

    @Test
    void testCreateStrictControl() {
        IMocksControl ctrl = createStrictControl();
        MatcherAssert.assertThat(ctrl.createMock(ToMock.class), isA(ToMock.class));
    }

    @Test
    void testCreateControl() {
        IMocksControl ctrl = createControl();
        MatcherAssert.assertThat(ctrl.createMock(ToMock.class), isA(ToMock.class));
    }

    @Test
    void testCreateNiceControl() {
        IMocksControl ctrl = createNiceControl();
        MatcherAssert.assertThat(ctrl.createMock(ToMock.class), isA(ToMock.class));
    }

    @Test
    void testCreateStrictMockClassOfT() {
        MatcherAssert.assertThat(createStrictMock(ToMock.class), isA(ToMock.class));
    }

    @Test
    void testCreateStrictMockStringClassOfT() {
        MatcherAssert.assertThat(createStrictMock("myMock", ToMock.class), isA(ToMock.class));
    }

    @Test
    void testCreateMockClassOfT() {
        MatcherAssert.assertThat(createMock(ToMock.class), isA(ToMock.class));
    }

    @Test
    void testCreateMockStringClassOfT() {
        MatcherAssert.assertThat(createMock("myMock", ToMock.class), isA(ToMock.class));
    }

    @Test
    void testCreateNiceMockClassOfT() {
        MatcherAssert.assertThat(createNiceMock(ToMock.class), isA(ToMock.class));
    }

    @Test
    void testCreateNiceMockStringClassOfT() {
        MatcherAssert.assertThat(createNiceMock("myMock", ToMock.class), isA(ToMock.class));
    }

    @Test
    void testAll() {
        ToMock t = createMock(ToMock.class);
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
    void testCreateMockBuilder() {
        ToMock t = createMockBuilder(ToMock.class).addMockedMethod(foo).createMock();
        expect(t.foo()).andReturn(1);
        replayAll();
        Assertions.assertEquals(1, t.foo());
        verifyAll();
    }

    @Test
    void testCreateMockBuilder_existingControl() {
        IMocksControl ctrl = createControl(); // ctrl registered once here
        ToMock t = createMockBuilder(ToMock.class).addMockedMethod(foo).createMock(ctrl); // should not be registered a second time here
        expect(t.foo()).andReturn(1);
        replayAll();
        Assertions.assertEquals(1, t.foo());
        verifyAll();
    }

    @Test
    void testAllMockBuilderFlavors() {
        ToMock t1 = createMockBuilder(ToMock.class).addMockedMethod(foo).createMock();
        ToMock t2 = createMockBuilder(ToMock.class).addMockedMethod(foo).createNiceMock();
        ToMock t3 = createMockBuilder(ToMock.class).addMockedMethod(foo).createStrictMock();
        expect(t1.foo()).andReturn(1);
        expect(t2.foo()).andReturn(2);
        expect(t3.foo()).andReturn(3);
        replayAll();
        Assertions.assertEquals(1, t1.foo());
        Assertions.assertEquals(2, t2.foo());
        Assertions.assertEquals(3, t3.foo());
        verifyAll();
    }
}
