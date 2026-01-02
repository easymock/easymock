/*
 * Copyright 2001-2026 the original author or authors.
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
package org.easymock.tests;

import org.easymock.IMocksControl;
import org.easymock.MockType;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.createNiceControl;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.niceMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * @author Henri Tremblay
 */
class UtilTest {

    @Test
    void testGet() {
        IMethods mock = niceMock(IMethods.class);
        assertNull(Util.getName(mock));
        assertNotNull(Util.getControl(mock));
        assertEquals(MockType.NICE, Util.getType(mock));
    }

    @Test
    void testGetSpecificControl() {
        IMocksControl control = createNiceControl();
        IMethods mock = control.mock(IMethods.class);
        assertNull(Util.getName(mock));
        assertSame(control, Util.getControl(mock));
        assertEquals(MockType.NICE, Util.getType(mock));
    }

    @Test
    void testGetSpecificName() {
        IMethods mock = mock("a", IMethods.class);
        assertEquals("a", Util.getName(mock));
    }
}
