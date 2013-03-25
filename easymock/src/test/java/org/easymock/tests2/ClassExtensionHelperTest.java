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

import com.google.dexmaker.stock.ProxyBuilder;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.createMock;
import org.easymock.internal.AndroidSupport;
import static org.easymock.internal.MocksControl.getControl;
import static org.easymock.internal.MocksControl.getMockedType;
import org.easymock.internal.MocksControl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * @author Henri Tremblay
 */
public class ClassExtensionHelperTest {

    private static final InvocationHandler NOOP_INVOCATION_HANDLER = new InvocationHandler() {
        public Object invoke(Object proxy, Method method, Object[] args) {
            return null;
        }
    };

    @Test
    public void testGetControl_EasyMock() {
        final List<?> mock = EasyMock.createMock(List.class);
        assertNotNull(getControl(mock));
    }

    @Test
    public void testGetControl_EasyMockClassExtension() {
        final ArrayList<?> mock = EasyMock.createMock(ArrayList.class);
        assertTrue(getControl(mock) instanceof MocksControl);
    }

    @Test
    public void testGetControl_EnhancedButNotAMock() throws Exception {
        final Object o;
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
        } catch (final IllegalArgumentException e) {
            assertEquals("Not a mock: " + o.getClass().getName(), e.getMessage());
        }
    }

    @Test
    public void testGetControl_ProxyButNotMock() {
        final Object o = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { List.class },
                new InvocationHandler() {
                    public Object invoke(final Object proxy, final Method method, final Object[] args)
                            throws Throwable {
                        return null;
                    }
                });
        try {
            getControl(o);
            fail();
        } catch (final IllegalArgumentException e) {
            assertEquals("Not a mock: " + o.getClass().getName(), e.getMessage());
        }
    }

    @Test
    public void testGetControl_NotAMock() {
        try {
            getControl("");
            fail();
        } catch (final IllegalArgumentException expected) {
        }
    }

    @Test
    public void testMockType_Class() {
        final Object o = createMock(ArrayList.class);
        final Class<?> c = getMockedType(o);
        assertEquals(ArrayList.class, c);
    }

    @Test
    public void testMockType_Interface() {
        final Object o = createMock(List.class);
        final Class<?> c = getMockedType(o);
        assertEquals(List.class, c);
    }
}
