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

import static java.lang.Character.*;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.easymock.internal.matchers.Captures;

public class Invocation implements Serializable {

    private static final long serialVersionUID = 1604995470419943411L;

    private final Object mock;

    private transient Method method;

    private final Object[] arguments;
    
    private final Collection<Captures<?>> currentCaptures = new ArrayList<Captures<?>>(
            0);

    public Invocation(Object mock, Method method, Object[] args) {
        this.mock = mock;
        this.method = method;
        this.arguments = expandVarArgs(method.isVarArgs(), args);
    }

    private static Object[] expandVarArgs(final boolean isVarArgs,
            final Object[] args) {
        if (!isVarArgs) {
            return args == null ? new Object[0] : args;
        }
        if (args[args.length - 1] == null) {
            return args;
        }
        Object[] varArgs = createObjectArray(args[args.length - 1]);
        final int nonVarArgsCount = args.length - 1;
        final int varArgsCount = varArgs.length;
        Object[] newArgs = new Object[nonVarArgsCount + varArgsCount];
        System.arraycopy(args, 0, newArgs, 0, nonVarArgsCount);
        System.arraycopy(varArgs, 0, newArgs, nonVarArgsCount, varArgsCount);
        return newArgs;
    }
    
    private static Object[] createObjectArray(Object array) {
        if (array instanceof Object[]) {
            return (Object[]) array;
        }
        Object[] result = new Object[Array.getLength(array)];
        for (int i = 0; i < Array.getLength(array); i++) {
            result[i] = Array.get(array, i);
        }
        return result;
    }

    public Object getMock() {
        return mock;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !o.getClass().equals(this.getClass()))
            return false;

        Invocation other = (Invocation) o;

        return this.mock.equals(other.mock) && this.method.equals(other.method)
                && this.equalArguments(other.arguments);
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("hashCode() is not implemented");
    }

    private boolean equalArguments(Object[] arguments) {
        if (this.arguments.length != arguments.length) {
            return false;
        }
        for (int i = 0; i < this.arguments.length; i++) {
            Object myArgument = this.arguments[i];
            Object otherArgument = arguments[i];

            if (isPrimitiveParameter(i)) {
                if (!myArgument.equals(otherArgument)) {
                    return false;
                }
            } else {
                if (myArgument != otherArgument) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isPrimitiveParameter(int parameterPosition) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (method.isVarArgs()) {
            parameterPosition = Math.min(parameterPosition,
                    parameterTypes.length - 1);
        }
        return parameterTypes[parameterPosition].isPrimitive();
    }

    @SuppressWarnings("deprecation")
    public boolean matches(Invocation actual, org.easymock.ArgumentsMatcher matcher) {
        return this.mock.equals(actual.mock)
                && this.method.equals(actual.method)
                && matcher.matches(this.arguments, actual.arguments);
    }

    @SuppressWarnings("deprecation")
    public String toString(org.easymock.ArgumentsMatcher matcher) {
        return getMockAndMethodName() + "(" + matcher.toString(arguments) + ")";
    }

    public String getMockAndMethodName() {
        String mockName = mock.toString();
        String methodName = method.getName();
        if (toStringIsDefined(mock) && isJavaIdentifier(mockName)) {
            return mockName + "." + methodName;
        } else {
            return methodName;
        }
    }
    
    public void addCapture(Captures<Object> capture, Object value) {
        capture.setPotentialValue(value);
        currentCaptures.add(capture);
    }

    public void validateCaptures() {
        for (Captures<?> c : currentCaptures) {
            c.validateCapture();
        }
    }

    public void clearCaptures() {
        for (Captures<?> c : currentCaptures) {
            c.setPotentialValue(null);
        }
        currentCaptures.clear();
    }    
    
    private boolean toStringIsDefined(Object o) {
        try {
            o.getClass().getDeclaredMethod("toString", (Class[]) null)
                    .getModifiers();
            return true;
        } catch (SecurityException ignored) {
            // ///CLOVER:OFF
            return false;
            // ///CLOVER:ON
        } catch (NoSuchMethodException shouldNeverHappen) {
            // ///CLOVER:OFF
            throw new RuntimeException("The toString() method could not be found!");
            // ///CLOVER:ON
        }
    }

    public static boolean isJavaIdentifier(String mockName) {
        if (mockName.length() == 0 || mockName.indexOf(' ') > -1
                || !Character.isJavaIdentifierStart(mockName.charAt(0))) {
            return false;
        }
        for (char c : mockName.substring(1).toCharArray()) {
            if (!isJavaIdentifierPart(c)) {
                return false;
            }
        }
        return true;
    }
    
    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        try {
            method = ((MethodSerializationWrapper) stream.readObject()).getMethod();
        } catch (NoSuchMethodException e) {
            // ///CLOVER:OFF
            throw new IOException(e.toString());
            // ///CLOVER:ON
        }
    }
    
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(new MethodSerializationWrapper(method));
    }
}
