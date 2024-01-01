/*
 * Copyright 2001-2024 the original author or authors.
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

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Class containing the data required for a class mock to work.
 */
public class ClassMockingData implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * Invocation handler that will intercept all calls
     */
    private final InvocationHandler handler;

    /**
     * List of mocked methods. Null is everything is mocked. Abstract methods won't appear in this list since they
     * are always mocked.
     */
    private transient Set<Method> mockedMethods;

    public ClassMockingData(InvocationHandler handler, Method... mockedMethods) {
        this.handler = handler;
        this.mockedMethods = mockedMethods == null ? null : new HashSet<>(Arrays.asList(mockedMethods));
    }

    public boolean isMocked(Method method) {
        if (mockedMethods == null) {
            return true;
        }
        if (Modifier.isAbstract(method.getModifiers())) {
            return true;
        }

        return mockedMethods.contains(method);
    }

    public InvocationHandler handler() {
        return handler;
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream stream) throws IOException,
        ClassNotFoundException {
        stream.defaultReadObject();
        Set<MethodSerializationWrapper> methods = (Set<MethodSerializationWrapper>) stream.readObject();
        if (methods == null) {
            return;
        }

        mockedMethods = new HashSet<>(methods.size());
        for (MethodSerializationWrapper m : methods) {
            try {
                mockedMethods.add(m.getMethod());
            } catch (NoSuchMethodException e) {
                // ///CLOVER:OFF
                throw new IOException(e.toString());
                // ///CLOVER:ON
            }
        }
    }

    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();

        if (mockedMethods == null) {
            stream.writeObject(null);
            return;
        }

        Set<MethodSerializationWrapper> methods = new HashSet<>(mockedMethods.size());
        for (Method m : mockedMethods) {
            methods.add(new MethodSerializationWrapper(m));
        }

        stream.writeObject(methods);
    }

}
