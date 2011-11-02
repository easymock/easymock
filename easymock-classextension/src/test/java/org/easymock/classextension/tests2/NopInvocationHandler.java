/**
 * Copyright 2003-2011 the original author or authors.
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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * InvocationHandler that just do nothing.
 * 
 * @author Henri Tremblay
 */
public class NopInvocationHandler implements InvocationHandler {

    public static final InvocationHandler NOP = new NopInvocationHandler();

    private transient Method equalsMethod;

    private transient Method hashCodeMethod;

    private transient Method toStringMethod;

    private NopInvocationHandler() {
    }

    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        return null;
    }
}