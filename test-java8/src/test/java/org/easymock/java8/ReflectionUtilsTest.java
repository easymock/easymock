/*
 * Copyright 2018-2018 the original author or authors.
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
package org.easymock.java8;

import org.easymock.internal.ReflectionUtils;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.easymock.internal.ReflectionUtils.*;
import static org.junit.Assert.*;

/**
 * @author Henri Tremblay
 */
public class ReflectionUtilsTest {

    private static final Class[] NO_PARAMS = new Class[0];
    private static final String DEFAULT_INTERFACE_METHOD = "defaultInterfaceMethod";

    @Test
    public void defaultOverride() throws Exception {
        IMethods o = new Methods.DefaultOverride();
        Method method = ReflectionUtils.findMethod(o.getClass(), DEFAULT_INTERFACE_METHOD, NOT_PRIVATE, NO_PARAMS);
        assertEquals(2, method.invoke(o));
    }

    @Test
    public void noOverride() throws Exception {
        IMethods o = new Methods.NoDefaultOverride();
        Method method = ReflectionUtils.findMethod(o.getClass(), DEFAULT_INTERFACE_METHOD, NOT_PRIVATE, NO_PARAMS);
        assertEquals(1, method.invoke(o));
    }

    @Test
    public void overrideOnBaseClass() throws Exception {
        IMethods o = new Methods.B();
        Method method = ReflectionUtils.findMethod(o.getClass(), DEFAULT_INTERFACE_METHOD, NOT_PRIVATE, NO_PARAMS);
        assertEquals(3, method.invoke(o));
    }
}
