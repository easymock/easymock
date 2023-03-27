/*
 * Copyright 2001-2023 the original author or authors.
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

import static org.easymock.EasyMock.*;

/**
 * @author Henri Tremblay
 */
public class EasyMockSupportTest extends EasyMockSupport {

    private IMethods mock1;

    private IMethods mock2;

    @Test
    public void testCreateControl() {
        IMocksControl ctrl = createControl();
        mock1 = ctrl.createMock(IMethods.class);
        mock2 = ctrl.createMock(IMethods.class);
        testDefaultMock();
    }

    @Test
    public void testCreateMock() {
        mock1 = createMock(IMethods.class);
        mock2 = createMock(IMethods.class);
        testDefaultMock();
    }

    @Test
    public void testCreateNamedMock() {
        mock1 = createMock("a", IMethods.class);
        mock2 = createMock("b", IMethods.class);
        testDefaultMock();
        Assertions.assertEquals("a", mock1.toString());
        Assertions.assertEquals("b", mock2.toString());
    }

    private void testDefaultMock() {
        mock1.simpleMethod();
        expect(mock1.oneArg(true)).andReturn("foo");
        mock2.simpleMethod();
        expect(mock2.oneArg(false)).andReturn("foo");
        replayAll();
        Assertions.assertEquals("foo", mock1.oneArg(true));
        Assertions.assertEquals("foo", mock2.oneArg(false));
        mock2.simpleMethod();
        mock1.simpleMethod();
        verifyAll();
    }

    @Test
    public void testCreateNiceControl() {
        IMocksControl ctrl = createNiceControl();
        mock1 = ctrl.createMock(IMethods.class);
        mock2 = ctrl.createMock(IMethods.class);
        testNiceMock();
    }

    @Test
    public void testCreateNiceMock() {
        mock1 = createNiceMock(IMethods.class);
        mock2 = createNiceMock(IMethods.class);
        testNiceMock();
    }

    @Test
    public void testCreateNamedNiceMock() {
        mock1 = createNiceMock("a", IMethods.class);
        mock2 = createNiceMock("b", IMethods.class);
        testNiceMock();
        Assertions.assertEquals("a", mock1.toString());
        Assertions.assertEquals("b", mock2.toString());
    }

    private void testNiceMock() {
        expect(mock1.oneArg(true)).andReturn("foo");
        expect(mock2.oneArg(false)).andReturn("foo");
        replayAll();
        Assertions.assertNull(mock1.oneArg(false));
        Assertions.assertNull(mock2.oneArg(true));
        Assertions.assertEquals("foo", mock1.oneArg(true));
        Assertions.assertEquals("foo", mock2.oneArg(false));
        verifyAll();
    }

    @Test
    public void testCreateStrictControl() {
        IMocksControl ctrl = createStrictControl();
        mock1 = ctrl.createMock(IMethods.class);
        mock2 = ctrl.createMock(IMethods.class);
        testStrictMock();
        resetAll();
        mock1.simpleMethod();
        mock2.simpleMethod();
        replayAll();
        try {
            mock2.simpleMethod();
            Assertions.fail("Should be ordered");
        } catch (AssertionError e) {
        }
        mock1.simpleMethod();
        mock2.simpleMethod();
        verifyAllRecordings();
    }

    @Test
    public void testCreateStrictMock() {
        mock1 = createStrictMock(IMethods.class);
        mock2 = createStrictMock(IMethods.class);
        testStrictMock();
    }

    @Test
    public void testCreateNamedStrictMock() {
        mock1 = createStrictMock("a", IMethods.class);
        mock2 = createStrictMock("b", IMethods.class);
        testStrictMock();
        Assertions.assertEquals("a", mock1.toString());
        Assertions.assertEquals("b", mock2.toString());
    }

    private void testStrictMock() {
        expect(mock1.oneArg(true)).andReturn("foo");
        expect(mock1.oneArg(false)).andReturn("foo");
        expect(mock2.oneArg(false)).andReturn("foo");
        expect(mock2.oneArg(true)).andReturn("foo");
        replayAll();
        try {
            mock1.oneArg(false);
            Assertions.fail("Should be ordered");
        } catch (AssertionError e) {
        }
        mock1.oneArg(true);
        mock1.oneArg(false);
        try {
            mock2.oneArg(true);
            Assertions.fail("Should be ordered");
        } catch (AssertionError e) {
        }
        mock2.oneArg(false);
        mock2.oneArg(true);
        verifyRecording();
    }

    @Test
    public void testVerify() {
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
    public void testVerifyUnexpectedCalls() {
        mock1 = createMock(IMethods.class);
        mock2 = createMock(IMethods.class);
        replayAll();

        try {
            mock1.simpleMethod();
        } catch(AssertionError e) {
        }
        try {
            mock2.simpleMethod();
        } catch(AssertionError e) {
        }

        try {
            verifyAllUnexpectedCalls();
            Assertions.fail("Should see missing calls");
        } catch(AssertionError e) {
            // note that only errors from the first mock are shown
            Assertions.assertEquals("\n" +
                "  Unexpected method calls:\n" +
                "    IMethods.simpleMethod()", e.getMessage());
        }
    }

    @Test
    public void testVerifyRecording() {
        mock1 = createMock(IMethods.class);
        mock2 = createMock(IMethods.class);
        mock1.simpleMethod();
        mock2.simpleMethod();
        replayAll();
        try {
            verifyAllRecordings();
            Assertions.fail("Should see unexpected calls");
        } catch(AssertionError e) {
            // note that only errors from the first mock are shown
            Assertions.assertEquals("\n" +
                "  Expectation failure on verify:\n" +
                "    IMethods.simpleMethod(): expected: 1, actual: 0", e.getMessage());
        }
    }

    @Test
    public void defaultResetToNice() {
        mock1 = createMock(IMethods.class);
        mock2 = createMock(IMethods.class);

        expect(mock1.oneArg(true)).andReturn("foo");
        expect(mock2.oneArg(false)).andReturn("foo");

        replayAll();

        resetAllToNice();

        replayAll();

        Assertions.assertNull(mock1.oneArg(true));
        Assertions.assertNull(mock2.oneArg(false));

        verifyAll();
    }

    @Test
    public void strictResetToDefault() {
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

        Assertions.assertEquals("foo", mock1.oneArg(false));
        Assertions.assertEquals("foo", mock1.oneArg(true));
        Assertions.assertEquals("foo", mock2.oneArg(false));
        Assertions.assertEquals("foo", mock2.oneArg(true));

        verifyAll();
    }

    @Test
    public void niceToStrict() {
        IMethods mock1 = createNiceMock(IMethods.class);
        IMethods mock2 = createNiceMock(IMethods.class);

        expect(mock1.oneArg(false)).andReturn("foo");
        expect(mock2.oneArg(false)).andReturn("foo");

        replayAll();

        Assertions.assertNull(mock1.oneArg(true));
        Assertions.assertNull(mock2.oneArg(true));

        resetAllToStrict();

        expect(mock1.oneArg(false)).andReturn("foo");
        expect(mock1.oneArg(true)).andReturn("foo");
        expect(mock2.oneArg(false)).andReturn("foo");
        expect(mock2.oneArg(true)).andReturn("foo");

        replayAll();

        try {
            mock1.oneArg(true);
            Assertions.fail("Should be strict");
        } catch (AssertionError e) {
        }
        try {
            mock2.oneArg(true);
            Assertions.fail("Should be strict");
        } catch (AssertionError e) {
        }

        Assertions.assertEquals("foo", mock1.oneArg(false));
        Assertions.assertEquals("foo", mock1.oneArg(true));
        Assertions.assertEquals("foo", mock2.oneArg(false));
        Assertions.assertEquals("foo", mock2.oneArg(true));

        verifyAllRecordings();
    }

    @Test
    public void mockType() throws Exception {
        Assertions.assertNull(EasyMockSupport.getMockedClass(null));
        Assertions.assertNull(EasyMockSupport.getMockedClass(new Object()));

        // Proxy that is not an EasyMock proxy
        Assertions.assertNull(EasyMockSupport.getMockedClass(Proxy.newProxyInstance(getClass().getClassLoader(),
            new Class<?>[] { IMethods.class }, (proxy, method, args) -> null)));

        // ByteBuddy proxy that is not an EasyMock proxy
        Class<?> mockClass = new ByteBuddy()
            .subclass(Object.class)
            .make()
            .load(Object.class.getClassLoader(), new ClassLoadingStrategy.ForUnsafeInjection())
            .getLoaded();
        Object mock = mockClass.getDeclaredConstructor().newInstance();
        Assertions.assertNull(EasyMockSupport.getMockedClass(mock));
    }
}
