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
package org.easymock;

/**
 * Controls all the mock objects created by it.
 * For details, see the EasyMock documentation.
 */
public interface IMocksControl {
    /**
     * Creates a mock object that implements the given interface.
     * @param <T> the interface that the mock object should implement.
     * @param toMock the class of the interface that the mock object should implement.
     * @return the mock object.
     */
    <T> T createMock(Class<T> toMock);

    /**
     * Creates a mock object that implements the given interface.
     * @param name the name of the mock object .
     * @param toMock the class of the interface that the mock object should implement.
     * @param <T> the interface that the mock object should implement.
     * @return the mock object.
     * @throws IllegalArgumentException if the name is not a valid Java identifier.
     */
    <T> T createMock(String name, Class<T> toMock);

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
     * @param state <code>true</code> switches order checking on, <code>false</code> switches it off.
     */
    void checkOrder(boolean state);
    
    /**
     * Makes the mock thread safe.
     * 
     * @param threadSafe If the mock should be thread safe or not
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
