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
package org.easymock;

/**
 * Allows setting expectations for an associated expected invocation.
 * Implementations of this interface are returned by
 * {@link EasyMock#expect(Object)}, and by {@link EasyMock#expectLastCall()}.
 * 
 * @param <T>
 *            type of what should be returned by this expected call
 * 
 * @author OFFIS, Tammo Freese
 */
public interface IExpectationSetters<T> {

    /**
     * Sets a return value that will be returned for the expected invocation.
     * 
     * @param value
     *            the value to return.
     * @return this object to allow method call chaining.
     */
    IExpectationSetters<T> andReturn(T value);

    /**
     * Sets a throwable that will be thrown for the expected invocation.
     * 
     * @param throwable
     *            the throwable to throw.
     * @return this object to allow method call chaining.
     */
    IExpectationSetters<T> andThrow(Throwable throwable);

    /**
     * Sets an object that will be used to calculate the answer for the expected
     * invocation (either return a value, or throw an exception).
     * 
     * @param answer
     *            the object used to answer the invocation.
     * @return this object to allow method call chaining.
     */
    IExpectationSetters<T> andAnswer(IAnswer<? extends T> answer);

    /**
     * Sets an object implementing the same interface as the mock. The expected
     * method call will be delegated to it with the actual arguments. The answer
     * returned by this call will then be the answer returned by the mock
     * (either return a value, or throw an exception).
     * 
     * @param delegateTo
     *            the object the call is delegated to.
     * @return the value returned by the delegated call.
     */
    IExpectationSetters<T> andDelegateTo(Object delegateTo);

    /**
     * Sets a stub return value that will be returned for the expected
     * invocation.
     * 
     * @param value
     *            the value to return.
     */
    void andStubReturn(T value);

    /**
     * Sets a stub throwable that will be thrown for the expected invocation.
     * 
     * @param throwable
     *            the throwable to throw.
     */
    void andStubThrow(Throwable throwable);

    /**
     * Sets a stub object that will be used to calculate the answer for the
     * expected invocation (either return a value, or throw an exception).
     * 
     * @param answer
     *            the object used to answer the invocation.
     */
    void andStubAnswer(IAnswer<? extends T> answer);

    /**
     * Sets a stub object implementing the same interface as the mock. The
     * expected method call will be delegated to it with the actual arguments.
     * The answer returned by this call will then be the answer returned by the
     * mock (either return a value, or throw an exception).
     * 
     * @param delegateTo
     *            the object the call is delegated to.
     */
    void andStubDelegateTo(Object delegateTo);

    /**
     * Sets stub behavior for the expected invocation (this is needed for void
     * methods).
     */
    void asStub();

    /**
     * Expect the last invocation <code>count</code> times.
     * 
     * @param count
     *            the number of invocations expected.
     * @return this object to allow method call chaining.
     */
    IExpectationSetters<T> times(int count);

    /**
     * Expect the last invocation between <code>min</code> and <code>max</code>
     * times.
     * 
     * @param min
     *            the minimum number of invocations expected.
     * @param max
     *            the maximum number of invocations expected.
     * @return this object to allow method call chaining.
     */
    IExpectationSetters<T> times(int min, int max);

    /**
     * Expect the last invocation once. This is default in EasyMock.
     * 
     * @return this object to allow method call chaining.
     */
    IExpectationSetters<T> once();

    /**
     * Expect the last invocation at least once.
     * 
     * @return this object to allow method call chaining.
     */
    IExpectationSetters<T> atLeastOnce();

    /**
     * Expect the last invocation any times.
     * 
     * @return this object to allow method call chaining.
     */
    IExpectationSetters<T> anyTimes();
}
