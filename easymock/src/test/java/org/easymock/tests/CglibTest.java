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
package org.easymock.tests;

import java.lang.reflect.Method;
import java.util.ArrayList;

import net.sf.cglib.proxy.*;

import org.easymock.internal.ClassInstantiatorFactory;
import org.junit.Test;

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
     * @throws Exception
     */
    @Test
    public void test() throws Exception {

//        final Factory f1 = createMock();
//        final Factory f2 = createMock();
//
//        assertNotSame(f1.getCallback(0), f2.getCallback(0));
    }

    private Factory createMock() throws Exception {
        final MethodInterceptor interceptor = new MethodInterceptor() {
            public Object intercept(final Object obj, final Method method, final Object[] args,
                    final MethodProxy proxy) throws Throwable {
                return proxy.invokeSuper(obj, args);
            }
        };

        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ArrayList.class);
        enhancer.setCallbackType(MethodInterceptor.class);

        final Class<?> mockClass = enhancer.createClass();

        Enhancer.registerCallbacks(mockClass, new Callback[] { interceptor });

        final Factory f = (Factory) ClassInstantiatorFactory.getInstantiator().newInstance(mockClass);

        f.getCallback(0);

        return f;
    }
}
