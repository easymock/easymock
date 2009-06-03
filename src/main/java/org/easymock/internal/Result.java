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
import java.lang.reflect.InvocationTargetException;

import org.easymock.IAnswer;

public class Result implements IAnswer<Object>, Serializable {

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
                    return invocation.getMethod().invoke(value,
                            invocation.getArguments());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Delegation to object ["
                            + String.valueOf(value)
                            + "] is not implementing the mocked method ["
                            + invocation.getMethod() + "]", e);
                } catch (InvocationTargetException e) {
                    throw e.getCause();
                }
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
