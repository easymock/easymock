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
 * Used to answer expected calls.
 * @param <T> the type to return.
 */
public interface IAnswer<T> {
    /**
     * is called by EasyMock to answer an expected call. 
     * The answer may be to return a value, or to throw an exception.
     * The arguments of the call for which the answer is generated 
     * are available via {@link EasyMock#getCurrentArguments()} - be careful
     * here, using the arguments is not refactoring-safe.
     * 
     * @return the value to be returned
     * @throws Throwable the throwable to be thrown
     */
    T answer() throws Throwable;
}
