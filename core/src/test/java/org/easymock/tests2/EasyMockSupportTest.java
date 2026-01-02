/*
 * Copyright 2001-2026 the original author or authors.
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

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.easymock.EasyMockSupport;
import org.easymock.IMocksControl;
import org.easymock.tests.IMethods;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.verifyRecording;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Henri Tremblay
 */
class EasyMockSupportTest extends EasyMockSupport {

    private IMethods mock1;

    private IMethods mock2;

    @Test
    void testCreateControl() {
        IMocksControl ctrl = createControl();
        mock1 = ctrl.createMock(IMethods.class);
        mock2 = ctrl.createMock(IMethods.class);
        testDefaultMock();
    }

    @Test
    void testCreateMock() {
        mock1 = createMock(IMethods.class);
        mock2 = createMock(IMethods.class);
        testDefaultMock();
    }

    @Test
    void testCreateNamedMock() {
        mock1 = createMock("a", IMethods.class);
        mock2 = createMock("b", IMethods.class);
        testDefaultMock();
        assertEquals("a", mock1.toString());
        assertEquals("b", mock2.toString());
    }

    private void testDefaultMock() {
        mock1.simpleMethod();
        expect(mock1.oneArg(true)).andReturn("foo");
        mock2.simpleMethod();
        expect(mock2.oneArg(false)).andReturn("foo");
        replayAll();
        assertEquals("foo", mock1.oneArg(true));
        assertEquals("foo", mock2.oneArg(false));
        mock2.simpleMethod();
        mock1.simpleMethod();
        verifyAll();
    }

    @Test
    void testCreateNiceControl() {
        IMocksControl ctrl = createNiceControl();
        mock1 = ctrl.createMock(IMethods.class);
        mock2 = ctrl.createMock(IMethods.class);
        testNiceMock();
    }

    @Test
    void testCreateNiceMock() {
        mock1 = createNiceMock(IMethods.class);
        mock2 = createNiceMock(IMethods.class);
        testNiceMock();
    }

    @Test
    void testCreateNamedNiceMock() {
        mock1 = createNiceMock("a", IMethods.class);
        mock2 = createNiceMock("b", IMethods.class);
        testNiceMock();
        assertEquals("a", mock1.toString());
        assertEquals("b", mock2.toString());
    }

    private void testNiceMock() {
        expect(mock1.oneArg(true)).andReturn("foo");
        expect(mock2.oneArg(false)).andReturn("foo");
        replayAll();
        assertNull(mock1.oneArg(false));
        assertNull(mock2.oneArg(true));
        assertEquals("foo", mock1.oneArg(true));
        assertEquals("foo", mock2.oneArg(false));
        verifyAll();
    }

    @Test
    void testCreateStrictControl() {
        IMocksControl ctrl = createStrictControl();
        mock1 = ctrl.createMock(IMethods.class);
        mock2 = ctrl.createMock(IMethods.class);
        testStrictMock();
        resetAll();
        mock1.simpleMethod();
        mock2.simpleMethod();
        replayAll();

        assertThrows(AssertionError.class, () -> mock2.simpleMethod(), "Should be ordered");

        mock1.simpleMethod();
        mock2.simpleMethod();
        verifyAllRecordings();
    }

    @Test
    void testCreateStrictMock() {
        mock1 = createStrictMock(IMethods.class);
        mock2 = createStrictMock(IMethods.class);
        testStrictMock();
    }

    @Test
    void testCreateNamedStrictMock() {
        mock1 = createStrictMock("a", IMethods.class);
        mock2 = createStrictMock("b", IMethods.class);
        testStrictMock();
        assertEquals("a", mock1.toString());
        assertEquals("b", mock2.toString());
    }

    private void testStrictMock() {
        expect(mock1.oneArg(true)).andReturn("foo");
        expect(mock1.oneArg(false)).andReturn("foo");
        expect(mock2.oneArg(false)).andReturn("foo");
        expect(mock2.oneArg(true)).andReturn("foo");
        replayAll();

        assertThrows(AssertionError.class, () -> mock1.oneArg(false), "Should be ordered");

        mock1.oneArg(true);
        mock1.oneArg(false);

        assertThrows(AssertionError.class, () -> mock1.oneArg(true), "Should be ordered");

        mock2.oneArg(false);
        mock2.oneArg(true);
        verifyRecording();
    }

    @Test
    void testVerify() {
        mock1 = createMock(IMethods.class);
        mock2 = createMock(IMethods.class);
        mock1.simpleMethod();
        mock2.simpleMethod();
        replayAll();
        mock1.simpleMethod();
        mock2.simpleMethod();
        verifyAll();
        resetAll();
        mock1.simpleMethod();
        mock2.simpleMethod();
        resetAll();
        replayAll();
        verifyAll();
    }

    @Test
    void testVerifyUnexpectedCalls() {
        mock1 = createMock(IMethods.class);
        mock2 = createMock(IMethods.class);
        replayAll();

        assertThrows(AssertionError.class, () -> mock1.simpleMethod());
        assertThrows(AssertionError.class, () -> mock2.simpleMethod());

        AssertionError e = assertThrows(AssertionError.class, () -> verifyAllUnexpectedCalls(), "Should see missing calls");
        // note that only errors from the first mock are shown
        assertEquals("\n" +
            "  Unexpected method calls:\n" +
            "    EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethod()", e.getMessage());
    }

    @Test
    void testVerifyRecording() {
        mock1 = createMock(IMethods.class);
        mock2 = createMock(IMethods.class);
        mock1.simpleMethod();
        mock2.simpleMethod();
        replayAll();
        AssertionError e = assertThrows(AssertionError.class, () -> verifyAllRecordings(), "Should see unexpected calls");
        // note that only errors from the first mock are shown
        assertEquals("\n" +
            "  Expectation failure on verify:\n" +
            "    EasyMock for interface org.easymock.tests.IMethods -> IMethods.simpleMethod(): expected: 1, actual: 0", e.getMessage());
    }

    @Test
    void defaultResetToNice() {
        mock1 = createMock(IMethods.class);
        mock2 = createMock(IMethods.class);

        expect(mock1.oneArg(true)).andReturn("foo");
        expect(mock2.oneArg(false)).andReturn("foo");

        replayAll();

        resetAllToNice();

        replayAll();

        assertNull(mock1.oneArg(true));
        assertNull(mock2.oneArg(false));

        verifyAll();
    }

    @Test
    void strictResetToDefault() {
        mock1 = createStrictMock(IMethods.class);
        mock2 = createStrictMock(IMethods.class);

        expect(mock1.oneArg(true)).andReturn("foo");
        expect(mock1.oneArg(false)).andReturn("foo");
        expect(mock1.oneArg(true)).andReturn("foo");
        expect(mock1.oneArg(false)).andReturn("foo");

        replayAll();

        resetAllToDefault();

        expect(mock1.oneArg(false)).andReturn("foo");
        expect(mock1.oneArg(true)).andReturn("foo");
        expect(mock2.oneArg(false)).andReturn("foo");
        expect(mock2.oneArg(true)).andReturn("foo");

        replayAll();

        assertEquals("foo", mock1.oneArg(false));
        assertEquals("foo", mock1.oneArg(true));
        assertEquals("foo", mock2.oneArg(false));
        assertEquals("foo", mock2.oneArg(true));

        verifyAll();
    }

    @Test
    void niceToStrict() {
        IMethods mock1 = createNiceMock(IMethods.class);
        IMethods mock2 = createNiceMock(IMethods.class);

        expect(mock1.oneArg(false)).andReturn("foo");
        expect(mock2.oneArg(false)).andReturn("foo");

        replayAll();

        assertNull(mock1.oneArg(true));
        assertNull(mock2.oneArg(true));

        resetAllToStrict();

        expect(mock1.oneArg(false)).andReturn("foo");
        expect(mock1.oneArg(true)).andReturn("foo");
        expect(mock2.oneArg(false)).andReturn("foo");
        expect(mock2.oneArg(true)).andReturn("foo");

        replayAll();

        assertThrows(AssertionError.class, () -> mock1.oneArg(true), "Should be strict");
        assertThrows(AssertionError.class, () -> mock2.oneArg(true), "Should be strict");

        assertEquals("foo", mock1.oneArg(false));
        assertEquals("foo", mock1.oneArg(true));
        assertEquals("foo", mock2.oneArg(false));
        assertEquals("foo", mock2.oneArg(true));

        verifyAllRecordings();
    }

    @Test
    void mockType() throws Exception {
        assertNull(EasyMockSupport.getMockedClass(null));
        assertNull(EasyMockSupport.getMockedClass(new Object()));

        // Proxy that is not an EasyMock proxy
        assertNull(EasyMockSupport.getMockedClass(Proxy.newProxyInstance(getClass().getClassLoader(),
            new Class<?>[]{IMethods.class}, (proxy, method, args) -> null)));

        // ByteBuddy proxy that is not an EasyMock proxy
        Class<?> mockClass = new ByteBuddy()
            .subclass(Object.class)
            .make()
            .load(Object.class.getClassLoader(), new ClassLoadingStrategy.ForUnsafeInjection())
            .getLoaded();
        Object mock = mockClass.getDeclaredConstructor().newInstance();
        assertNull(EasyMockSupport.getMockedClass(mock));
    }
}
