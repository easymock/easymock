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

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import net.sf.cglib.proxy.Callback;

import org.easymock.internal.ClassProxyFactory;
import org.junit.Test;

/**
 * @author Henri Tremblay
 */
public class ClassProxyFactoryTest {

    private final ClassProxyFactory factory = new ClassProxyFactory();

    @SuppressWarnings("unchecked")
    @Test
    public void testRegisterClassNotLeaking() throws Exception {
        final ClassProxyFactoryTest mock = factory.createProxy(ClassProxyFactoryTest.class,
                NopInvocationHandler.NOP, null, null);
        // Go deep in the cglib implementation to make sure the callback was released on the mocked class
        final Field field = mock.getClass().getDeclaredField("CGLIB$THREAD_CALLBACKS");
        field.setAccessible(true);
        final ThreadLocal<Callback> tl = (ThreadLocal<Callback>) field.get(mock);
        final Callback callback = tl.get();
        assertNull(callback);
    }

}
