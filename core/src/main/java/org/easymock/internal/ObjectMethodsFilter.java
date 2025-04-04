/*
 * Copyright 2001-2025 the original author or authors.
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
import java.util.function.Predicate;

/**
 * The filter catching all calls to the mock. It handles <code>equals</code>, <code>hashCode</code>, <code>toString</code>,
 * and <code>finalize</code> in a special way. Then, for other calls, it delegates to the mock handler.
 *
 * @author OFFIS, Tammo Freese
 * @author Henri Tremblay
 */
public final class ObjectMethodsFilter implements InvocationHandler, Serializable {

    private static final long serialVersionUID = -1726286682930686024L;

    private static final Predicate<Method> NOT_PRIVATE = method -> !Modifier.isPrivate(method.getModifiers());

    private transient Method equalsMethod;

    private transient Method hashCodeMethod;

    private transient Method toStringMethod;

    private transient Method finalizeMethod;

    private final MockInvocationHandler delegate;

    private final String name;

    public ObjectMethodsFilter(Class<?> toMock, MockInvocationHandler delegate, String name) {
        if (toMock.isInterface()) {
            equalsMethod = ReflectionUtils.OBJECT_EQUALS;
            hashCodeMethod = ReflectionUtils.OBJECT_HASHCODE;
            toStringMethod = ReflectionUtils.OBJECT_TOSTRING;
            finalizeMethod = ReflectionUtils.OBJECT_FINALIZE;
        } else {
            try {
                equalsMethod = extractMethod(toMock, "equals", Object.class);
                hashCodeMethod = extractMethod(toMock, "hashCode", (Class<?>[]) null);
                toStringMethod = extractMethod(toMock, "toString", (Class<?>[]) null);
                finalizeMethod = ReflectionUtils.findMethod(toMock, "finalize", NOT_PRIVATE, (Class<?>[]) null);
            } catch (NoSuchMethodException e) {
                // ///CLOVER:OFF
                throw new RuntimeException("An Object method could not be found!", e);
                // ///CLOVER:ON
            }
        }

        this.delegate = delegate;
        this.name = name;
    }

    public String getName() {
        return name;
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

    public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (equalsMethod.equals(method)) {
            return proxy == args[0];
        }
        if (hashCodeMethod.equals(method)) {
            return System.identityHashCode(proxy);
        }
        if (toStringMethod.equals(method)) {
            return mockToString(proxy);
        }
        if (finalizeMethod.equals(method)) {
            return null; // ignore finalize completely to prevent any unexpected side-effect from the GC
        }
        return delegate.invoke(proxy, method, args);
    }

    private String mockToString(Object proxy) {
        return (name != null) ? name : "EasyMock for " + MocksControl.getMockedClass(proxy);
    }

    public MockInvocationHandler getDelegate() {
        return delegate;
    }

    private void readObject(java.io.ObjectInputStream stream) throws IOException,
            ClassNotFoundException {
        stream.defaultReadObject();
        try {
            toStringMethod = ((MethodSerializationWrapper) stream.readObject()).getMethod();
            equalsMethod = ((MethodSerializationWrapper) stream.readObject()).getMethod();
            hashCodeMethod = ((MethodSerializationWrapper) stream.readObject()).getMethod();
            finalizeMethod = ((MethodSerializationWrapper) stream.readObject()).getMethod();
        } catch (NoSuchMethodException e) {
            // ///CLOVER:OFF
            throw new IOException(e.toString());
            // ///CLOVER:ON
        }
    }

    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(new MethodSerializationWrapper(toStringMethod));
        stream.writeObject(new MethodSerializationWrapper(equalsMethod));
        stream.writeObject(new MethodSerializationWrapper(hashCodeMethod));
        stream.writeObject(new MethodSerializationWrapper(finalizeMethod));
    }
}
