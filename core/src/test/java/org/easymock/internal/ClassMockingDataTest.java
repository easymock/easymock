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

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class ClassMockingDataTest {

    public static abstract class A {
        public int a() {
            return 42;
        }

        public int b() {
            return 100;
        }

        public abstract int c();
    }

    private final InvocationHandler nop = (InvocationHandler & Serializable) (proxy, method, args) -> 111;
    private final Method a = method("a");
    private final Method b = method("b");
    private final Method c = method("c");

    private static Method method(String name) {
        try {
            return A.class.getDeclaredMethod(name);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void isMocked_noMockedMethods() {
        ClassMockingData data = new ClassMockingData(nop, (Method[]) null);
        assertTrue(data.isMocked(a));
        assertTrue(data.isMocked(b));
        assertTrue(data.isMocked(c));
    }

    @Test
    void isMocked_mockedMethods() {
        ClassMockingData data = new ClassMockingData(nop, b);
        assertFalse(data.isMocked(a));
        assertTrue(data.isMocked(b));
        assertTrue(data.isMocked(c));
    }

    @Test
    void serialize_noMockedMethods() throws Throwable {
        ClassMockingData data = new ClassMockingData(nop, (Method[]) null);
        ClassMockingData result = serialize(data);
        assertTrue(result.isMocked(a));
        assertTrue(result.isMocked(b));
        assertTrue(result.isMocked(c));
    }

    @Test
    void serialize_mockedMethods() throws Throwable {
        ClassMockingData data = new ClassMockingData(nop, b);
        ClassMockingData result = serialize(data);
        assertFalse(result.isMocked(a));
        assertTrue(result.isMocked(b));
        assertTrue(result.isMocked(c));
    }

    private ClassMockingData serialize(ClassMockingData data) throws Throwable {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bOut)) {
            out.writeObject(data);
        }
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bOut.toByteArray()))) {
            ClassMockingData result = (ClassMockingData) in.readObject();
            assertEquals(111, result.handler().invoke(null, null, null));
            return result;
        }
    }
}
