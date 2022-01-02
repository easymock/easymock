/*
 * Copyright 2001-2022 the original author or authors.
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
import org.droidparts.dexmaker.stock.ProxyBuilder;
import org.easymock.EasyMock;
import org.easymock.internal.AndroidSupport;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.easymock.internal.MocksControl.*;
import static org.junit.Assert.*;

/**
 * @author Henri Tremblay
 */
public class ClassExtensionHelperTest {

    private static final InvocationHandler NOOP_INVOCATION_HANDLER = (proxy, method, args) -> null;

    @Test
    public void testGetControl_EasyMock() {
        List<?> mock = EasyMock.createMock(List.class);
        assertNotNull(getControl(mock));
    }

    @Test
    public void testGetControl_EasyMockClassExtension() {
        ArrayList<?> mock = EasyMock.createMock(ArrayList.class);
        assertNotNull(getControl(mock));
    }

    @Test
    public void testGetControl_EnhancedButNotAMock() throws Exception {
        Object o;
        if (AndroidSupport.isAndroid()) {
            o = ProxyBuilder.forClass(ArrayList.class)
                    .handler(NOOP_INVOCATION_HANDLER)
                    .build();
        } else {
            o = Enhancer.create(ArrayList.class, NoOp.INSTANCE);
        }
        try {
            getControl(o);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Not a mock: " + o.getClass().getName(), e.getMessage());
        }
    }

    @Test
    public void testGetControl_ProxyButNotMock() {
        Object o = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { List.class },
            (proxy, method, args) -> null);
        try {
            getControl(o);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Not a mock: " + o.getClass().getName(), e.getMessage());
        }
    }

    @Test
    public void testGetControl_NotAMock() {
        try {
            getControl("");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void testMockType_Class() {
        Object o = createMock(ArrayList.class);
        Class<?> c = getMockedClass(o);
        assertSame(ArrayList.class, c);
    }

    @Test
    public void testMockType_Interface() {
        Object o = createMock(List.class);
        Class<?> c = getMockedClass(o);
        assertSame(List.class, c);
    }
}
