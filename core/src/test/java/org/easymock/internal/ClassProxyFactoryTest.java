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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;

public class ClassProxyFactoryTest {

    @Test
    public void test() throws Exception {
        Class<?> clazz = new ByteBuddy()
            .subclass(getClass())
            .method(any())
            .intercept(InvocationHandlerAdapter.of(new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("Allo");
                    return null;
                }
            }))
            .make()
            .load(getClass().getClassLoader())
            .getLoaded();
        System.out.println(clazz.getName());
        ClassProxyFactoryTest t = (ClassProxyFactoryTest) clazz.newInstance();
        t.hello();
    }

    public void hello() {
        System.out.println("Hello");
    }
}
