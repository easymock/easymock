/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.classextension.tests2;

import static org.easymock.classextension.internal.ClassExtensionHelper.*;
import static org.junit.Assert.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import org.easymock.classextension.EasyMock;
import org.easymock.internal.MocksControl;
import org.junit.Test;

public class ClassExtensionHelperTest {

    @Test
    public void testGetControl_EasyMock() {
        List<?> mock = EasyMock.createMock(List.class);
        assertNotNull(getControl(mock));
    }

    @Test
    public void testGetControl_EasyMockClassExtension() {
        ArrayList<?> mock = EasyMock.createMock(ArrayList.class);
        assertTrue(getControl(mock) instanceof MocksControl);
    }

    @Test
    public void testGetControl_EnhancedButNotAMock() {
        Object o = Enhancer.create(ArrayList.class, NoOp.INSTANCE);
        try {
            getControl(o);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Not a mock: " + o.getClass().getName(), e.getMessage());
        }
    }

    @Test
    public void testGetControl_ProxyButNotMock() {
        Object o = Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[] { List.class }, new InvocationHandler() {
                    public Object invoke(Object proxy, Method method,
                            Object[] args) throws Throwable {
                        return null;
                    }
                });
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
        } catch (IllegalArgumentException e) {
            assertEquals("Not a mock: " + String.class.getName(), e.getMessage());
        }
    }
}
