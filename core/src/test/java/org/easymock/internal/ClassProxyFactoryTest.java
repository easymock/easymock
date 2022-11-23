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
package org.easymock.internal;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ClassProxyFactoryTest {

    @Test
    public void testInterception() throws Exception {
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
        ClassProxyFactoryTest t = (ClassProxyFactoryTest) clazz.newInstance();
        t.hello();
        assertTrue(called.get());
    }

    public void hello() {
        fail("Should not be called since it's proxied");
    }
}
