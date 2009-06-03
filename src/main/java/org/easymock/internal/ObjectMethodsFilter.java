/*
 * Copyright 2001-2009 OFFIS, Tammo Freese
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
import java.lang.reflect.Proxy;

public class ObjectMethodsFilter implements InvocationHandler, Serializable {

    private static final long serialVersionUID = -2120581954279966998L;

    private transient Method equalsMethod;

    private transient Method hashCodeMethod;

    private transient Method toStringMethod;

    private final MockInvocationHandler delegate;

    private final String name;

    public ObjectMethodsFilter(Class<?> toMock, MockInvocationHandler delegate,
            String name) {
        if (name != null && !Invocation.isJavaIdentifier(name)) {
            throw new IllegalArgumentException(String.format("'%s' is not a valid Java identifier.", name));
            
        }
        try {
            if (toMock.isInterface()) {
                toMock = Object.class;
            }
            equalsMethod = toMock.getMethod("equals",
                    new Class[] { Object.class });
            hashCodeMethod = toMock.getMethod("hashCode", (Class[]) null);
            toStringMethod = toMock.getMethod("toString", (Class[]) null);
        } catch (NoSuchMethodException e) {
            // ///CLOVER:OFF
            throw new RuntimeException("An Object method could not be found!");
            // ///CLOVER:ON
        }
        this.delegate = delegate;
        this.name = name;
    }

    public final Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        if (equalsMethod.equals(method)) {
            return Boolean.valueOf(proxy == args[0]);
        }
        if (hashCodeMethod.equals(method)) {
            return Integer.valueOf(System.identityHashCode(proxy));
        }
        if (toStringMethod.equals(method)) {
            return mockToString(proxy);
        }
        return delegate.invoke(proxy, method, args);
    }

    private String mockToString(Object proxy) {
        return (name != null) ? name : "EasyMock for " + mockType(proxy);
    }

    private String mockType(Object proxy) {
        if (Proxy.isProxyClass(proxy.getClass()))
            return proxy.getClass().getInterfaces()[0].toString();
        else
            return proxy.getClass().getSuperclass().toString();
    }

    public MockInvocationHandler getDelegate() {
        return delegate;
    }
    
    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        try {
            toStringMethod = ((MethodSerializationWrapper) stream.readObject()).getMethod();
            equalsMethod = ((MethodSerializationWrapper) stream.readObject()).getMethod();
            hashCodeMethod = ((MethodSerializationWrapper) stream.readObject()).getMethod();
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
    }
}