/*
 * Copyright 2009-2023 the original author or authors.
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
package org.easymock.itests;

import org.easymock.MockType;
import org.easymock.internal.MocksControl;
import org.easymock.internal.matchers.Equals;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * Test that we still can mock interfaces without ByteBuddy and Objenesis as
 * dependencies
 *
 * @author Henri Tremblay
 */
public class InterfaceOnlyTest extends OsgiBaseTest {

    @Test
    public void testCanMock() throws IOException {
        Appendable mock = mock(Appendable.class);
        expect(mock.append("test")).andReturn(mock);
        replayAll();
        assertSame(mock, mock.append("test"));
        verifyAll();
    }

    @Ignore("Doesn't work with pax-exam yet")
    @Test
    public void testCanUseMatchers() {
        new Equals(new Object());
    }

    @Ignore("Doesn't work with pax-exam yet")
    @Test
    public void testCanUseInternal() {
        new MocksControl(MockType.DEFAULT);
    }

    @Ignore("Doesn't work with pax-exam yet")
    @Test
    public void testCannotMock() {
        try {
            mock(ArrayList.class);
            fail("Should throw an exception due to a NoClassDefFoundError");
        } catch (RuntimeException e) {
            assertEquals("Class mocking requires to have objenesis library in the classpath", e
                    .getMessage());
            assertTrue(e.getCause() instanceof NoClassDefFoundError);
        }
    }
}
