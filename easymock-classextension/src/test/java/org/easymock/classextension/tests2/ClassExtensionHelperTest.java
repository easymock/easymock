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

import static org.easymock.internal.MocksControl.*;
import static org.junit.Assert.*;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import org.easymock.classextension.EasyMock;
import org.easymock.internal.MocksControl;
import org.junit.Test;

/**
 * @author Henri Tremblay
 */
@SuppressWarnings("deprecation")
public class ClassExtensionHelperTest {

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
    public void testGetControl_EnhancedButNotAMock() {
        final Object o = Enhancer.create(ArrayList.class, NoOp.INSTANCE);
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
                NopInvocationHandler.NOP);
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
        } catch (final IllegalArgumentException e) {
            assertEquals("Not a mock: " + String.class.getName(), e.getMessage());
        }
    }
}
