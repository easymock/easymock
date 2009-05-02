/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock;

/**
 * Allows setting expectations for an associated expected invocation.
 * Implementations of this interface are returned by
 * {@link EasyMock#expect(Object)}, and by {@link EasyMock#expectLastCall()}.
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
     * @return the value returned by the delegated call.
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
     * Expect the last invocation between <code>min</code> and
     * <code>max</code> times.
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
