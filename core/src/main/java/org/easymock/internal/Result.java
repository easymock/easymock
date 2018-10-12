/*
 * Copyright 2001-2018 the original author or authors.
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

import org.easymock.IAnswer;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author OFFIS, Tammo Freese
 */
public final class Result implements IAnswer<Object>, Serializable {

    private static final long serialVersionUID = 5476251941213917681L;

    private final IAnswer<?> value;

    private final boolean shouldFillInStackTrace;

    private Result(IAnswer<?> value, boolean shouldFillInStackTrace) {
        this.value = value;
        this.shouldFillInStackTrace = shouldFillInStackTrace;
    }

    public static Result createThrowResult(final Throwable throwable) {
        class ThrowingAnswer implements IAnswer<Object>, Serializable {

            private static final long serialVersionUID = -332797751209289222L;

            public Object answer() throws Throwable {
                throw throwable;
            }

            @Override
            public String toString() {
                return "Answer throwing " + throwable;
            }
        }
        return new Result(new ThrowingAnswer(), true);
    }

    public static Result createReturnResult(final Object value) {
        class ReturningAnswer implements IAnswer<Object>, Serializable {

            private static final long serialVersionUID = 6973893913593916866L;

            public Object answer() throws Throwable {
                return value;
            }

            @Override
            public String toString() {
                return "Answer returning " + value;
            }
        }
        return new Result(new ReturningAnswer(), true);
    }

    public static Result createDelegatingResult(final Object value) {
        class DelegatingAnswer implements IAnswer<Object>, Serializable {

            private static final long serialVersionUID = -5699326678580460103L;

            public Object answer() throws Throwable {
                Invocation invocation = LastControl.getCurrentInvocation();
                try {
                    Method m = invocation.getMethod();
                    if(!m.isAccessible()) {
                        m.setAccessible(true);
                    }
                    return m.invoke(value, convertVarargs(m, invocation.getArguments()));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Delegation to object [" + value
                            + "] is not implementing the mocked method [" + invocation.getMethod() + "]", e);
                } catch (InvocationTargetException e) {
                    throw e.getCause();
                }
            }

            private Object[] convertVarargs(Method method, Object[] arguments) {
                if(!method.isVarArgs()) {
                    return arguments;
                }
                Class<?>[] paramTypes = method.getParameterTypes();
                Object[] packedArguments = new Object[paramTypes.length];

                int normalArgLength = paramTypes.length - 1;
                int varargLength = arguments.length - paramTypes.length + 1;

                // Copy all the non varargs arguments
                System.arraycopy(arguments, 0, packedArguments, 0, normalArgLength);

                // Now copy the varargs ones to a new array
                Object varargs = Array.newInstance(paramTypes[packedArguments.length-1].getComponentType(), varargLength);
                for (int i = 0; i < varargLength; i++) {
                    Array.set(varargs, i, arguments[normalArgLength + i]);
                }

                // Put the vararg at the last place
                packedArguments[packedArguments.length - 1] = varargs;
                return packedArguments;
            }

            @Override
            public String toString() {
                return "Delegated to " + value;
            }
        }
        return new Result(new DelegatingAnswer(), false);
    }

    public static Result createAnswerResult(IAnswer<?> answer) {
        return new Result(answer, false);
    }

    public Object answer() throws Throwable {
        return value.answer();
    }

    public boolean shouldFillInStackTrace() {
        return shouldFillInStackTrace;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
