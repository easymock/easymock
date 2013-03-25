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

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.IAnswer;
import org.easymock.IArgumentMatcher;

/**
 * @author OFFIS, Tammo Freese
 */
public class RecordState implements IMocksControlState, Serializable {

    private static final long serialVersionUID = -5418279681566430252L;

    private ExpectedInvocation lastInvocation = null;

    private boolean lastInvocationUsed = true;

    private Result lastResult;

    private final IMocksBehavior behavior;

    private static Map<Class<?>, Object> emptyReturnValues = new HashMap<Class<?>, Object>();

    static {
        emptyReturnValues.put(Void.TYPE, null);
        emptyReturnValues.put(Boolean.TYPE, Boolean.FALSE);
        emptyReturnValues.put(Byte.TYPE, Byte.valueOf((byte) 0));
        emptyReturnValues.put(Short.TYPE, Short.valueOf((short) 0));
        emptyReturnValues.put(Character.TYPE, Character.valueOf((char) 0));
        emptyReturnValues.put(Integer.TYPE, Integer.valueOf(0));
        emptyReturnValues.put(Long.TYPE, Long.valueOf(0));
        emptyReturnValues.put(Float.TYPE, Float.valueOf(0));
        emptyReturnValues.put(Double.TYPE, Double.valueOf(0));
    }

    private static Map<Class<?>, Class<?>> primitiveToWrapperType = new HashMap<Class<?>, Class<?>>();

    static {
        primitiveToWrapperType.put(Boolean.TYPE, Boolean.class);
        primitiveToWrapperType.put(Byte.TYPE, Byte.class);
        primitiveToWrapperType.put(Short.TYPE, Short.class);
        primitiveToWrapperType.put(Character.TYPE, Character.class);
        primitiveToWrapperType.put(Integer.TYPE, Integer.class);
        primitiveToWrapperType.put(Long.TYPE, Long.class);
        primitiveToWrapperType.put(Float.TYPE, Float.class);
        primitiveToWrapperType.put(Double.TYPE, Double.class);
    }

    public RecordState(final IMocksBehavior behavior) {
        this.behavior = behavior;
    }

    public void assertRecordState() {
    }

    public java.lang.Object invoke(final Invocation invocation) {
        closeMethod();
        final List<IArgumentMatcher> lastMatchers = LastControl.pullMatchers();
        lastInvocation = new ExpectedInvocation(invocation, lastMatchers);
        lastInvocationUsed = false;
        return emptyReturnValueFor(invocation.getMethod().getReturnType());
    }

    public void replay() {
        closeMethod();
        if (LastControl.pullMatchers() != null) {
            throw new IllegalStateException("matcher calls were used outside expectations");
        }
    }

    public void verify() {
        throw new RuntimeExceptionWrapper(new IllegalStateException(
                "calling verify is not allowed in record state"));
    }

    public void andReturn(Object value) {
        requireMethodCall("return value");
        value = convertNumberClassIfNeccessary(value);
        requireAssignable(value);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        lastResult = Result.createReturnResult(value);
    }

    public void andThrow(final Throwable throwable) {
        requireMethodCall("Throwable");
        requireValidThrowable(throwable);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        lastResult = Result.createThrowResult(throwable);
    }

    public void andAnswer(final IAnswer<?> answer) {
        requireMethodCall("answer");
        requireValidAnswer(answer);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        lastResult = Result.createAnswerResult(answer);
    }

    public void andDelegateTo(final Object delegateTo) {
        requireMethodCall("delegate");
        requireValidDelegation(delegateTo);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        lastResult = Result.createDelegatingResult(delegateTo);
    }

    public void andStubReturn(Object value) {
        requireMethodCall("stub return value");
        value = convertNumberClassIfNeccessary(value);
        requireAssignable(value);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        behavior.addStub(lastInvocation, Result.createReturnResult(value));
        lastInvocationUsed = true;
    }

    public void asStub() {
        requireMethodCall("stub behavior");
        requireVoidMethod();
        behavior.addStub(lastInvocation, Result.createReturnResult(null));
        lastInvocationUsed = true;
    }

    public void andStubThrow(final Throwable throwable) {
        requireMethodCall("stub Throwable");
        requireValidThrowable(throwable);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        behavior.addStub(lastInvocation, Result.createThrowResult(throwable));
        lastInvocationUsed = true;
    }

    public void andStubAnswer(final IAnswer<?> answer) {
        requireMethodCall("stub answer");
        requireValidAnswer(answer);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        behavior.addStub(lastInvocation, Result.createAnswerResult(answer));
        lastInvocationUsed = true;
    }

    public void andStubDelegateTo(final Object delegateTo) {
        requireMethodCall("stub delegate");
        requireValidDelegation(delegateTo);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        behavior.addStub(lastInvocation, Result.createDelegatingResult(delegateTo));
        lastInvocationUsed = true;
    }

    public void times(final Range range) {
        requireMethodCall("times");
        requireLastResultOrVoidMethod();
        behavior.addExpected(lastInvocation, lastResult != null ? lastResult : Result
                .createReturnResult(null), range);
        lastInvocationUsed = true;
        lastResult = null;
    }

    private Object createNumberObject(final Object value, final Class<?> returnType) {
        if (!(value instanceof Number)) {
            return value;
        }
        final Number number = (Number) value;
        if (returnType.equals(Byte.TYPE)) {
            return number.byteValue();
        } else if (returnType.equals(Short.TYPE)) {
            return number.shortValue();
        } else if (returnType.equals(Integer.TYPE)) {
            return number.intValue();
        } else if (returnType.equals(Long.TYPE)) {
            return number.longValue();
        } else if (returnType.equals(Float.TYPE)) {
            return number.floatValue();
        } else if (returnType.equals(Double.TYPE)) {
            return number.doubleValue();
        } else {
            return number;
        }
    }

    private Object convertNumberClassIfNeccessary(final Object o) {
        final Class<?> returnType = lastInvocation.getMethod().getReturnType();
        return createNumberObject(o, returnType);
    }

    private void closeMethod() {
        if (lastInvocationUsed && lastResult == null) {
            return;
        }
        if (!isLastResultOrVoidMethod()) {
            throw new RuntimeExceptionWrapper(new IllegalStateException(
                    "missing behavior definition for the preceding method call:\n"
                            + lastInvocation.toString() + "\nUsage is: expect(a.foo()).andXXX()"));
        }
        times(MocksControl.ONCE);
    }

    public static Object emptyReturnValueFor(final Class<?> type) {
        return type.isPrimitive() ? emptyReturnValues.get(type) : null;
    }

    private void requireMethodCall(final String failMessage) {
        if (lastInvocation == null) {
            throw new RuntimeExceptionWrapper(new IllegalStateException(
                    "method call on the mock needed before setting " + failMessage));
        }
    }

    private void requireAssignable(final Object returnValue) {
        if (lastMethodIsVoidMethod()) {
            throw new RuntimeExceptionWrapper(new IllegalStateException("void method cannot return a value"));
        }
        if (returnValue == null) {
            final Class<?> returnedType = lastInvocation.getMethod().getReturnType();
            if (returnedType.isPrimitive()) {
                throw new RuntimeExceptionWrapper(new IllegalStateException(
                        "can't return null for a method returning a primitive type"));
            }
            return;
        }
        Class<?> returnedType = lastInvocation.getMethod().getReturnType();
        if (returnedType.isPrimitive()) {
            returnedType = primitiveToWrapperType.get(returnedType);

        }
        if (!returnedType.isAssignableFrom(returnValue.getClass())) {
            throw new RuntimeExceptionWrapper(new IllegalStateException("incompatible return value type"));
        }
    }

    private void requireValidThrowable(final Throwable throwable) {
        if (throwable == null) {
            throw new RuntimeExceptionWrapper(new NullPointerException("null cannot be thrown"));
        }
        if (isValidThrowable(throwable)) {
            return;
        }

        throw new RuntimeExceptionWrapper(new IllegalArgumentException(
                "last method called on mock cannot throw " + throwable.getClass().getName()));
    }

    private void requireValidAnswer(final IAnswer<?> answer) {
        if (answer == null) {
            throw new RuntimeExceptionWrapper(new NullPointerException("answer object must not be null"));
        }
    }

    private void requireValidDelegation(final Object delegateTo) {
        if (delegateTo == null) {
            throw new RuntimeExceptionWrapper(
                    new NullPointerException("delegated to object must not be null"));
        }
        // Would be nice to validate delegateTo is implementing the mock
        // interface (not possible right now)
    }

    private void requireLastResultOrVoidMethod() {
        if (isLastResultOrVoidMethod()) {
            return;
        }
        throw new RuntimeExceptionWrapper(new IllegalStateException(
                "last method called on mock is not a void method"));
    }

    private void requireVoidMethod() {
        if (lastMethodIsVoidMethod()) {
            return;
        }
        throw new RuntimeExceptionWrapper(new IllegalStateException(
                "last method called on mock is not a void method"));
    }

    private boolean isLastResultOrVoidMethod() {
        return lastResult != null || lastMethodIsVoidMethod();
    }

    private boolean lastMethodIsVoidMethod() {
        final Class<?> returnType = lastInvocation.getMethod().getReturnType();
        return returnType.equals(Void.TYPE);
    }

    private boolean isValidThrowable(final Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            return true;
        }
        if (throwable instanceof Error) {
            return true;
        }
        final Class<?>[] exceptions = lastInvocation.getMethod().getExceptionTypes();
        final Class<?> throwableClass = throwable.getClass();
        for (final Class<?> exception : exceptions) {
            if (exception.isAssignableFrom(throwableClass)) {
                return true;
            }
        }
        return false;
    }

    public void checkOrder(final boolean value) {
        closeMethod();
        behavior.checkOrder(value);
    }

    public void makeThreadSafe(final boolean threadSafe) {
        behavior.makeThreadSafe(threadSafe);
    }

    public void checkIsUsedInOneThread(final boolean shouldBeUsedInOneThread) {
        behavior.shouldBeUsedInOneThread(shouldBeUsedInOneThread);
    }
}