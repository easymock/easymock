/**
 * Copyright 2001-2017 the original author or authors.
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

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import org.easymock.EasyMockSupport;
import org.easymock.IMocksControl;
import org.easymock.tests.IMethods;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

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
            fail("Should be ordered");
        } catch (AssertionError e) {
        }
        mock1.simpleMethod();
        mock2.simpleMethod();
        verifyAll();
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
        assertEquals("a", mock1.toString());
        assertEquals("b", mock2.toString());
    }

    private void testStrictMock() {
        expect(mock1.oneArg(true)).andReturn("foo");
        expect(mock1.oneArg(false)).andReturn("foo");
        expect(mock2.oneArg(false)).andReturn("foo");
        expect(mock2.oneArg(true)).andReturn("foo");
        replayAll();
        try {
            mock1.oneArg(false);
            fail("Should be ordered");
        } catch (AssertionError e) {
        }
        mock1.oneArg(true);
        mock1.oneArg(false);
        try {
            mock2.oneArg(true);
            fail("Should be ordered");
        } catch (AssertionError e) {
        }
        mock2.oneArg(false);
        mock2.oneArg(true);
        verifyAll();
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
    public void defaultResetToNice() {
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

        assertEquals("foo", mock1.oneArg(false));
        assertEquals("foo", mock1.oneArg(true));
        assertEquals("foo", mock2.oneArg(false));
        assertEquals("foo", mock2.oneArg(true));

        verifyAll();
    }

    @Test
    public void niceToStrict() {
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

        try {
            mock1.oneArg(true);
            fail("Should be strict");
        } catch (AssertionError e) {
        }
        try {
            mock2.oneArg(true);
            fail("Should be strict");
        } catch (AssertionError e) {
        }

        assertEquals("foo", mock1.oneArg(false));
        assertEquals("foo", mock1.oneArg(true));
        assertEquals("foo", mock2.oneArg(false));
        assertEquals("foo", mock2.oneArg(true));

        verifyAll();
    }

    @Test
    public void mockType() {
        assertNull(EasyMockSupport.getMockedType(null));
        assertNull(EasyMockSupport.getMockedType(new Object()));

        // Proxy that is not an EasyMock proxy
        assertNull(EasyMockSupport.getMockedType(Proxy.newProxyInstance(getClass().getClassLoader(),
            new Class<?>[] { IMethods.class }, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
        })));

        // Cglib proxy that is not an EasyMock proxy
        assertNull(EasyMockSupport.getMockedType(Enhancer.create(Object.class, NoOp.INSTANCE)));

        // Really specific case where the cglib proxy is not even implementing Factory
        Enhancer enhancer = new Enhancer();
        enhancer.setUseFactory(false);
        enhancer.setSuperclass(getClass());
        enhancer.setCallback(NoOp.INSTANCE);
        assertNull(EasyMockSupport.getMockedType(enhancer.create()));

        assertEquals(IMethods.class, EasyMockSupport.getMockedType(mock(IMethods.class)));
        assertEquals(getClass(), EasyMockSupport.getMockedType(mock(getClass())));
    }
}
