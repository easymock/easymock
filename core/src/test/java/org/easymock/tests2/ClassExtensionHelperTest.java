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

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.droidparts.dexmaker.stock.ProxyBuilder;
import org.easymock.EasyMock;
import org.easymock.internal.AndroidSupport;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.easymock.internal.MocksControl.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Henri Tremblay
 */
class ClassExtensionHelperTest {

    private static final InvocationHandler NOOP_INVOCATION_HANDLER = (proxy, method, args) -> null;

    @Test
    void testGetControl_EasyMock() {
        List<?> mock = EasyMock.createMock(List.class);
        assertNotNull(getControl(mock));
    }

    @Test
    void testGetControl_EasyMockClassExtension() {
        ArrayList<?> mock = EasyMock.createMock(ArrayList.class);
        assertNotNull(getControl(mock));
    }

    @Test
    void testGetControl_ByteBuddyButNotAMock() throws Exception {
        Object o;
        if (AndroidSupport.isAndroid()) {
            o = ProxyBuilder.forClass(ArrayList.class)
                    .handler(NOOP_INVOCATION_HANDLER)
                    .build();
        } else {
            o = new ByteBuddy()
                .subclass(Object.class)
                .make()
                .load(Object.class.getClassLoader(), new ClassLoadingStrategy.ForUnsafeInjection())
                .getLoaded()
                .getDeclaredConstructor().newInstance();
        }
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> getControl(o));
        assertEquals("Not a mock: " + o.getClass().getName(), e.getMessage());
    }

    @Test
    void testGetControl_ProxyButNotMock() {
        Object o = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { List.class },
            (proxy, method, args) -> null);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> getControl(o));
        assertEquals("Not a mock: " + o.getClass().getName(), e.getMessage());
    }

    @Test
    void testGetControl_NotAMock() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> getControl(""));
        assertEquals("Not a mock: java.lang.String", e.getMessage());
    }

    @Test
    void testMockType_Class() {
        Object o = createMock(ArrayList.class);
        Class<?> c = getMockedClass(o);
        assertSame(ArrayList.class, c);
    }

    @Test
    void testMockType_Interface() {
        Object o = createMock(List.class);
        Class<?> c = getMockedClass(o);
        assertSame(List.class, c);
    }
}
