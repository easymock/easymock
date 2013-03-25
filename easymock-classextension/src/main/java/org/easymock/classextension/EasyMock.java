/**
 * Copyright 2003-2013 the original author or authors.
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
package org.easymock.classextension;

import org.easymock.classextension.internal.MocksClassControl;
import org.easymock.internal.MocksControl;

/**
 * This class is provided solely to allow an easier transition to EasyMock 3.0.
 * You should now use {@link org.easymock.EasyMock} for classes and interfaces
 * mocking.
 * <p>
 * Note: Since the static methods previously available were removed from this
 * class (and are now in the superclass), no binary compatibility is provided
 * 
 * @author Henri Tremblay
 * 
 * @deprecated You can now mock classes directly with
 *             {@link org.easymock.EasyMock}
 */
@Deprecated
public class EasyMock extends org.easymock.EasyMock {

    public static IMocksControl createStrictControl() {
        return new MocksClassControl(MocksControl.MockType.STRICT);
    }

    public static IMocksControl createControl() {
        return new MocksClassControl(MocksControl.MockType.DEFAULT);
    }

    public static IMocksControl createNiceControl() {
        return new MocksClassControl(MocksControl.MockType.NICE);
    }

    public static <T> IMockBuilder<T> createMockBuilder(Class<T> toMock) {
        return new org.easymock.classextension.internal.MockBuilder<T>(toMock);
    }

    // ///CLOVER:OFF
    /** Prevent instantiation but allow inheritance */
    protected EasyMock() {
    }
    // ///CLOVER:ON
}
