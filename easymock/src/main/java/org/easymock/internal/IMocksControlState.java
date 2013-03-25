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

import org.easymock.IAnswer;

/**
 * @author OFFIS, Tammo Freese
 */
public interface IMocksControlState {

    Object invoke(Invocation invocation) throws Throwable;

    void assertRecordState();

    void andReturn(Object value);

    void andThrow(Throwable throwable);

    void andAnswer(IAnswer<?> answer);

    void andDelegateTo(Object answer);

    void andStubReturn(Object value);

    void andStubThrow(Throwable throwable);

    void andStubAnswer(IAnswer<?> answer);

    void andStubDelegateTo(Object delegateTo);

    void asStub();

    void times(Range range);

    void checkOrder(boolean value);

    void makeThreadSafe(boolean threadSafe);

    void checkIsUsedInOneThread(boolean shouldBeUsedInOneThread);

    void replay();

    void verify();
}
