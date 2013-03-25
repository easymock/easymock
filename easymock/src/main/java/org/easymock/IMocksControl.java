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
     * Creates a mock object that implements the given class.
     * 
     * @param <T>
     *            the class that the mock object should extend.
     * @param toMock
     *            the class that the mock object should extend.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link EasyMock#createMockBuilder(Class)} instead
     */
    @Deprecated
    <T> T createMock(Class<T> toMock, Method... mockedMethods);

    /**
     * Creates a mock object that implements the given class.
     * 
     * @param <T>
     *            the class that the mock object should extend.
     * @param toMock
     *            the class that the mock object should extend.
     * @param constructorArgs
     *            constructor and parameters used to instantiate the mock.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link EasyMock#createMockBuilder(Class)} instead
     */
    @Deprecated
    <T> T createMock(Class<T> toMock, ConstructorArgs constructorArgs, Method... mockedMethods);

    /**
     * Creates a mock object that implements the given class.
     * 
     * @param <T>
     *            the class that the mock object should extend.
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class that the mock object should extend.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link EasyMock#createMockBuilder(Class)} instead
     */
    @Deprecated
    <T> T createMock(String name, Class<T> toMock, Method... mockedMethods);

    /**
     * Creates a mock object that implements the given class.
     * 
     * @param <T>
     *            the class that the mock object should extend.
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class that the mock object should extend.
     * @param constructorArgs
     *            constructor and parameters used to instantiate the mock.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link EasyMock#createMockBuilder(Class)} instead
     */
    @Deprecated
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
     * Verifies that all expectations were met.
     */
    void verify();

    /**
     * Switches order checking on and off.
     * 
     * @param state
     *            <code>true</code> switches order checking on,
     *            <code>false</code> switches it off.
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
