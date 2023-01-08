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
package org.easymock.internal;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import org.easymock.ConstructorArgs;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ClassProxyFactoryTest {

    @Test
    void testInterception() throws Exception {
        AtomicBoolean called = new AtomicBoolean(false);

        Class<?> clazz = new ByteBuddy()
            .subclass(getClass())
            .method(any())
            .intercept(InvocationHandlerAdapter.of((proxy, method, args) -> {
                called.set(true);
                return null;
            }))
            .make()
            .load(getClass().getClassLoader())
            .getLoaded();
        Constructor<?> constructor = clazz.getConstructor();
        ClassProxyFactoryTest t = (ClassProxyFactoryTest) constructor.newInstance();
        t.hello();
        assertTrue(called.get());
    }

    public void hello() {
        fail("Should not be called since it's proxied");
    }

    @Test
    void testSameClassInstance() throws Exception {
        ClassProxyFactory factory = new ClassProxyFactory();
        Constructor<?> constructor = getClass().getConstructor();
        ClassProxyFactoryTest t1 = factory.createProxy(getClass(), (proxy, method, args) -> null, null, new ConstructorArgs(constructor));
        ClassProxyFactoryTest t2 = factory.createProxy(getClass(), (proxy, method, args) -> null, null, new ConstructorArgs(constructor));
        assertSame(t1.getClass(), t2.getClass());
        assertNotSame(t1, t2);
    }

    @Test
    void twoMocksOfTheSameType_shouldNotInteractWithEachOther() {
        ArrayList<?> list1 = mock(ArrayList.class);
        expect(list1.size()).andReturn(1);
        replay(list1);
        assertEquals(1, list1.size());

        ArrayList<?> list2 = mock(ArrayList.class);
        expect(list2.size()).andReturn(2);
        replay(list2);
        assertEquals(2, list2.size());
    }
}
