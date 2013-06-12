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
package org.easymock.internal;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author OFFIS, Tammo Freese
 * @author Henri Tremblay
 */
public class ObjectMethodsFilter implements InvocationHandler, Serializable {

    private static final long serialVersionUID = -1726286682930686024L;

    private transient Method equalsMethod;

    private transient Method hashCodeMethod;

    private transient Method toStringMethod;

    private transient Method finalizeMethod;

    private final MockInvocationHandler delegate;

    private final String name;

    public ObjectMethodsFilter(final Class<?> toMock, final MockInvocationHandler delegate, final String name) {
        if (name != null && !Invocation.isJavaIdentifier(name)) {
            throw new IllegalArgumentException(String.format("'%s' is not a valid Java identifier.", name));

        }

        if (toMock.isInterface()) {
            equalsMethod = ReflectionUtils.OBJECT_EQUALS;
            hashCodeMethod = ReflectionUtils.OBJECT_HASHCODE;
            toStringMethod = ReflectionUtils.OBJECT_TOSTRING;
            finalizeMethod = ReflectionUtils.OBJECT_FINALIZE;
        } else {
            try {
                equalsMethod = extractMethod(toMock, "equals", Object.class);
                hashCodeMethod = extractMethod(toMock, "hashCode", (Class[]) null);
                toStringMethod = extractMethod(toMock, "toString", (Class[]) null);
                finalizeMethod = ReflectionUtils.findMethod(toMock, "finalize", (Class[]) null);
            } catch (final NoSuchMethodException e) {
                // ///CLOVER:OFF
                throw new RuntimeException("An Object method could not be found!", e);
                // ///CLOVER:ON
            }
        }

        this.delegate = delegate;
        this.name = name;
    }

    private static Method extractMethod(Class<?> toMock, String name, Class<?>... params) throws NoSuchMethodException {
        Method m = toMock.getMethod(name, params);
        // It can occur that the method was bridged. Usually, this means the method was in package scope on a parent class
        // When that occurs, we need to resolve the bridge to always extract the real method
        if(m.isBridge()) {
            m = BridgeMethodResolver.findBridgedMethod(m);
        }
        return m;
    }

    public final Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if (equalsMethod.equals(method)) {
            return Boolean.valueOf(proxy == args[0]);
        }
        if (hashCodeMethod.equals(method)) {
            return Integer.valueOf(System.identityHashCode(proxy));
        }
        if (toStringMethod.equals(method)) {
            return mockToString(proxy);
        }
        if (finalizeMethod.equals(method)) {
            return null; // ignore finalize completely to prevent any unexpected side-effect from the GC
        }
        return delegate.invoke(proxy, method, args);
    }

    private String mockToString(final Object proxy) {
        return (name != null) ? name : "EasyMock for " + MocksControl.getMockedType(proxy);
    }

    public MockInvocationHandler getDelegate() {
        return delegate;
    }

    private void readObject(final java.io.ObjectInputStream stream) throws IOException,
            ClassNotFoundException {
        stream.defaultReadObject();
        try {
            toStringMethod = ((MethodSerializationWrapper) stream.readObject()).getMethod();
            equalsMethod = ((MethodSerializationWrapper) stream.readObject()).getMethod();
            hashCodeMethod = ((MethodSerializationWrapper) stream.readObject()).getMethod();
            finalizeMethod = ((MethodSerializationWrapper) stream.readObject()).getMethod();
        } catch (final NoSuchMethodException e) {
            // ///CLOVER:OFF
            throw new IOException(e.toString());
            // ///CLOVER:ON
        }
    }

    private void writeObject(final java.io.ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(new MethodSerializationWrapper(toStringMethod));
        stream.writeObject(new MethodSerializationWrapper(equalsMethod));
        stream.writeObject(new MethodSerializationWrapper(hashCodeMethod));
        stream.writeObject(new MethodSerializationWrapper(finalizeMethod));
    }
}