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
package org.easymock.tests;

import java.lang.reflect.Method;

import org.easymock.internal.MethodSerializationWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class MethodSerializationWrapperTest {

    public static class A {
        public void foo(String s, int i, String[] sArray, int[] iArray,
                String... varargs) {
        }
    }

    @Test
    public void testGetMethod() throws Exception {
        Method foo = A.class.getMethod("foo", String.class, Integer.TYPE, String[].class, int[].class,
                String[].class);
        MethodSerializationWrapper wrapper = new MethodSerializationWrapper(foo);
        Assertions.assertEquals(foo, wrapper.getMethod());
    }

}
