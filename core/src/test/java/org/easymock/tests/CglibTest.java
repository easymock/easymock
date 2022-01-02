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
package org.easymock.tests;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.easymock.EasyMock;
import org.easymock.internal.ClassInstantiatorFactory;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * This test case is used to make sure that the way cglib is used is providing
 * the expected behavior
 *
 * @author Henri Tremblay
 */
public class CglibTest {

    /**
     * Check that an interceptor is used by only one instance of a class
     *
     * @throws Exception just a test
     */
    @Test
    public void test() throws Exception {
        Factory f1 = createMock();
        Factory f2 = createMock();

        assertNotSame(f1.getCallback(0), f2.getCallback(0));
    }

    private Factory createMock() throws Exception {
        @SuppressWarnings("Convert2Lambda") // if a lambda is used, since it it stateless, the same instance will be reused. This will defeat the purpose of this test
        MethodInterceptor interceptor = new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                return proxy.invokeSuper(obj, args);
            }
        };

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ArrayList.class);
        enhancer.setCallbackType(MethodInterceptor.class);

        Class<?> mockClass = enhancer.createClass();

        Factory f = (Factory) ClassInstantiatorFactory.getInstantiator().newInstance(mockClass);
        f.setCallback(0, interceptor);

        return f;
    }

    /**
     * Mocking the File class isn't working when using an old ASM version. So we make sure our version works
     * fine
     */
    @Test
    public void testJava8() {
        File file = EasyMock.createMock(File.class);
        EasyMock.expect(file.canExecute()).andReturn(true);
        EasyMock.replay(file);
        assertTrue(file.canExecute());
        EasyMock.verify(file);
    }
}
