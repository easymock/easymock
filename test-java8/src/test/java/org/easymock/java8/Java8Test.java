/*
 * Copyright 2018-2018 the original author or authors.
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
package org.easymock.java8;

import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Henri Tremblay
 */
public class Java8Test {

    @Test
    public void noOverride() {
        Class<Methods.NoDefaultOverride> toMock = Methods.NoDefaultOverride.class;
        partialMockOfDefaultMethod(toMock);
    }

    @Test
    public void defaultOverride() {
        Class<Methods.DefaultOverride> toMock = Methods.DefaultOverride.class;
        partialMockOfDefaultMethod(toMock);
    }

    @Test
    public void baseClassOvverride() {
        Class<Methods.DefaultOverride> toMock = Methods.DefaultOverride.class;
        partialMockOfDefaultMethod(toMock);
    }

    private void partialMockOfDefaultMethod(Class<? extends IMethods> toMock) {
        IMethods mock = partialMockBuilder(toMock)
            .addMockedMethod("defaultInterfaceMethod")
            .createMock();
        expect(mock.defaultInterfaceMethod()).andReturn(10);
        replay(mock);
        assertEquals(10, mock.normalInterfaceMethod());
        verify(mock);
    }
}
