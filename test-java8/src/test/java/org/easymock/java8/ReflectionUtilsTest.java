/*
 * Copyright 2018-2025 the original author or authors.
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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.easymock.internal.ReflectionUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Henri Tremblay
 */
class ReflectionUtilsTest {

    private static final String DEFAULT_INTERFACE_METHOD = "defaultInterfaceMethod";

    @Test
    void defaultOverride() throws Exception {
        IMethods o = new Methods.DefaultOverride();
        Method method = ReflectionUtils.findMethod(o.getClass(), DEFAULT_INTERFACE_METHOD, NOT_PRIVATE);
        assertEquals(2, method.invoke(o));
    }

    @Test
    void noOverride() throws Exception {
        IMethods o = new Methods.NoDefaultOverride();
        Method method = ReflectionUtils.findMethod(o.getClass(), DEFAULT_INTERFACE_METHOD, NOT_PRIVATE);
        assertEquals(1, method.invoke(o));
    }

    @Test
    void overrideOnBaseClass() throws Exception {
        IMethods o = new Methods.B();
        Method method = ReflectionUtils.findMethod(o.getClass(), DEFAULT_INTERFACE_METHOD, NOT_PRIVATE);
        assertEquals(3, method.invoke(o));
    }
}
