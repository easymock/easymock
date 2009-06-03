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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.IAnswer;
import org.easymock.IArgumentMatcher;

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
        emptyReturnValues.put(Character.TYPE, Character.valueOf((char)0));
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

    public RecordState(IMocksBehavior behavior) {
        this.behavior = behavior;
    }

    public void assertRecordState() {
    }

    public java.lang.Object invoke(Invocation invocation) {
        closeMethod();
        List<IArgumentMatcher> lastMatchers = LastControl.pullMatchers();
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

    public void andThrow(Throwable throwable) {
        requireMethodCall("Throwable");
        requireValidThrowable(throwable);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        lastResult = Result.createThrowResult(throwable);
    }
    
    public void andAnswer(IAnswer<?> answer) {
        requireMethodCall("answer");
        requireValidAnswer(answer);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        lastResult = Result.createAnswerResult(answer);
    }

    public void andDelegateTo(Object delegateTo) {
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

    @SuppressWarnings("deprecation")
    public void setDefaultReturnValue(Object value) {
        requireMethodCall("default return value");
        value = convertNumberClassIfNeccessary(value);
        requireAssignable(value);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        behavior.addStub(
                lastInvocation.withMatcher(org.easymock.MockControl.ALWAYS_MATCHER), Result
                        .createReturnResult(value));
        lastInvocationUsed = true;
    }

    public void asStub() {
        requireMethodCall("stub behavior");
        requireVoidMethod();
        behavior.addStub(lastInvocation, Result.createReturnResult(null));
        lastInvocationUsed = true;
    }

    @SuppressWarnings("deprecation")
    public void setDefaultVoidCallable() {
        requireMethodCall("default void callable");
        requireVoidMethod();
        behavior.addStub(
                lastInvocation.withMatcher(org.easymock.MockControl.ALWAYS_MATCHER), Result
                        .createReturnResult(null));
        lastInvocationUsed = true;
    }

    public void andStubThrow(Throwable throwable) {
        requireMethodCall("stub Throwable");
        requireValidThrowable(throwable);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        behavior.addStub(lastInvocation, Result.createThrowResult(throwable));
        lastInvocationUsed = true;
    }

    @SuppressWarnings("deprecation")
    public void setDefaultThrowable(Throwable throwable) {
        requireMethodCall("default Throwable");
        requireValidThrowable(throwable);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        behavior.addStub(
                lastInvocation.withMatcher(org.easymock.MockControl.ALWAYS_MATCHER), Result
                        .createThrowResult(throwable));
        lastInvocationUsed = true;
    }

    public void andStubAnswer(IAnswer<?> answer) {
        requireMethodCall("stub answer");
        requireValidAnswer(answer);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        behavior.addStub(lastInvocation, Result.createAnswerResult(answer));
        lastInvocationUsed = true;
    }

    public void andStubDelegateTo(Object delegateTo) {
        requireMethodCall("stub delegate");
        requireValidDelegation(delegateTo);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        behavior.addStub(lastInvocation, Result
                .createDelegatingResult(delegateTo));
        lastInvocationUsed = true;
    }
    
    public void times(Range range) {
        requireMethodCall("times");
        requireLastResultOrVoidMethod();

        behavior.addExpected(lastInvocation, lastResult != null ? lastResult
                : Result.createReturnResult(null), range);
        lastInvocationUsed = true;
        lastResult = null;
    }

    private Object createNumberObject(Object value, Class<?> returnType) {
        if (!(value instanceof Number)) {
            return value;
        }
        Number number = (Number) value;        
        if (returnType.equals(Byte.TYPE)) {
            return number.byteValue();
        } else if (returnType.equals(Short.TYPE)) {
            return number.shortValue();
        } else if (returnType.equals(Character.TYPE)) {
            return (char) number.intValue();
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

    private Object convertNumberClassIfNeccessary(Object o) {
        Class<?> returnType = lastInvocation.getMethod().getReturnType();
        return createNumberObject(o, returnType);
    }

    @SuppressWarnings("deprecation")
    private void closeMethod() {
        if (lastInvocationUsed && lastResult == null) {
            return;
        }
        if (!isLastResultOrVoidMethod()) {
            throw new RuntimeExceptionWrapper(new IllegalStateException(
                    "missing behavior definition for the preceding method call "
                            + lastInvocation.toString()));
        }
        this.times(org.easymock.MockControl.ONE);
    }

    public static Object emptyReturnValueFor(Class<?> type) {
        return type.isPrimitive() ? emptyReturnValues.get(type) : null;
    }

    private void requireMethodCall(String failMessage) {
        if (lastInvocation == null) {
            throw new RuntimeExceptionWrapper(new IllegalStateException(
                    "method call on the mock needed before setting "
                            + failMessage));
        }
    }

    private void requireAssignable(Object returnValue) {
        if (lastMethodIsVoidMethod()) {
            throw new RuntimeExceptionWrapper(new IllegalStateException(
                    "void method cannot return a value"));
        }
        if (returnValue == null) {
            return;
        }
        Class<?> returnedType = lastInvocation.getMethod().getReturnType();
        if (returnedType.isPrimitive()) {
            returnedType = primitiveToWrapperType.get(returnedType);

        }
        if (!returnedType.isAssignableFrom(returnValue.getClass())) {
            throw new RuntimeExceptionWrapper(new IllegalStateException(
                    "incompatible return value type"));
        }
    }

    private void requireValidThrowable(Throwable throwable) {
        if (throwable == null)
            throw new RuntimeExceptionWrapper(new NullPointerException(
                    "null cannot be thrown"));
        if (isValidThrowable(throwable))
            return;

        throw new RuntimeExceptionWrapper(new IllegalArgumentException(
                "last method called on mock cannot throw "
                        + throwable.getClass().getName()));
    }

    private void requireValidAnswer(IAnswer<?> answer) {
        if (answer == null)
            throw new RuntimeExceptionWrapper(new NullPointerException(
                    "answer object must not be null"));
    }

    private void requireValidDelegation(Object delegateTo) {
        if (delegateTo == null)
            throw new RuntimeExceptionWrapper(new NullPointerException(
                    "delegated to object must not be null"));
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
        Class<?> returnType = lastInvocation.getMethod().getReturnType();
        return returnType.equals(Void.TYPE);
    }

    private boolean isValidThrowable(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            return true;
        }
        if (throwable instanceof Error) {
            return true;
        }
        Class<?>[] exceptions = lastInvocation.getMethod().getExceptionTypes();
        Class<?> throwableClass = throwable.getClass();
        for (Class<?> exception : exceptions) {
            if (exception.isAssignableFrom(throwableClass))
                return true;
        }
        return false;
    }

    public void checkOrder(boolean value) {
        closeMethod();
        behavior.checkOrder(value);
    }

    public void makeThreadSafe(boolean threadSafe) {
        behavior.makeThreadSafe(threadSafe);
    }

    public void checkIsUsedInOneThread(boolean shouldBeUsedInOneThread) {
        behavior.shouldBeUsedInOneThread(shouldBeUsedInOneThread);
    }
    
    @SuppressWarnings("deprecation")
    public void setDefaultMatcher(org.easymock.ArgumentsMatcher matcher) {
        behavior.setDefaultMatcher(matcher);
    }

    @SuppressWarnings("deprecation")
    public void setMatcher(Method method, org.easymock.ArgumentsMatcher matcher) {
        requireMethodCall("matcher");
        behavior.setMatcher(lastInvocation.getMethod(), matcher);
    }
}