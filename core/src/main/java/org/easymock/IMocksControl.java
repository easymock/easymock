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
package org.easymock;

import java.lang.reflect.Method;

/**
 * Controls all the mock objects created by it. For details, see the EasyMock
 * documentation.
 *
 * @author OFFIS, Tammo Freese
 */
public interface IMocksControl {

    /**
     * Creates a mock object that implements the given interface.
     *
     * @param <T>
     *            the interface or class that the mock object should
     *            implement/extend.
     * @param toMock
     *            the interface or class that the mock object should
     *            implement/extend.
     * @return the mock object.
     */
    <T> T createMock(Class<T> toMock);

    /**
     * Creates a mock object that implements the given interface.
     *
     * @param <T>
     *            the interface or class that the mock object should
     *            implement/extend.
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the interface or class that the mock object should
     *            implement/extend.
     * @return the mock object.
     * @throws IllegalArgumentException
     *             if the name is not a valid Java identifier.
     */
    <T> T createMock(String name, Class<T> toMock);

    /**
     * Creates a mock object that implements the given class. Using this method directly in a test class
     * is not recommended. Only frameworks extending EasyMock should use it. Final users should use
     * the more convenient {@link EasyMock#partialMockBuilder(Class)} method instead
     *
     * @param <T>
     *            the class that the mock object should extend.
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class that the mock object should extend.
     * @param constructorArgs
     *            constructor and parameters used to instantiate the mock. If null, no constructor will be called
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally. If empty, all methods will be mocked
     * @return the mock object.
     */
    <T> T createMock(String name, Class<T> toMock, ConstructorArgs constructorArgs, Method... mockedMethods);

    /**
     * Removes all expectations for the mock objects of this control.
     */
    void reset();

    /**
     * Removes all expectations for the mock objects of this control and turn
     * them to nice mocks.
     */
    void resetToNice();

    /**
     * Removes all expectations for the mock objects of this control and turn
     * them to default mocks.
     */
    void resetToDefault();

    /**
     * Removes all expectations for the mock objects of this control and turn
     * them to strict mocks.
     */
    void resetToStrict();

    /**
     * Switches the control from record mode to replay mode.
     */
    void replay();

    /**
     * Verifies that all expectations were met and that no unexpected
     * call was performed. It has the same effect as calling {@link #verifyRecording()}
     * followed by {@link #verifyUnexpectedCalls()}.
     */
    void verify();

    /**
     * Verifies that all expectations were met.
     *
     * @since 3.5
     */
    void verifyRecording();

    /**
     * Verifies that no unexpected call was performed.
     *
     * @since 3.5
     */
    void verifyUnexpectedCalls();

    /**
     * Switches order checking on and off.
     *
     * @param state
     *            {@code true} switches order checking on,
     *            {@code false} switches it off.
     */
    void checkOrder(boolean state);

    /**
     * Makes the mock thread safe.
     *
     * @param threadSafe
     *            If the mock should be thread safe or not
     */
    void makeThreadSafe(boolean threadSafe);

    /**
     * Check that the mock is called from only one thread
     *
     * @param shouldBeUsedInOneThread
     *            If it should be used in one thread only or not
     */
    void checkIsUsedInOneThread(boolean shouldBeUsedInOneThread);
}
