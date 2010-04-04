/**
 * Copyright 2003-2010 the original author or authors.
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

import org.easymock.MockControl;
import org.easymock.internal.MocksControl;

/**
 * Instances of <code>MockClassControl</code> control the behavior of their
 * associated mock objects. For more information, see the EasyMock
 * documentation.
 * 
 * @param <T>
 * 
 * @see <a href="http://www.easymock.org/">EasyMock</a>
 * 
 * @author Henri Tremblay
 * 
 * @deprecated Use org.easymock.classextension.EasyMock instead
 */
@Deprecated
public class MockClassControl<T> extends MockControl<T> {

    private static final long serialVersionUID = 6516759409071770165L;

    protected MockClassControl(MocksControl ctrl, Class<T> toMock) {
        super(ctrl, toMock);
    }
}
