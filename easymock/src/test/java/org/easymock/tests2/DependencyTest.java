/**
 * Copyright 2001-2010 the original author or authors.
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

package org.easymock.tests2;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.EasyMockSupport;
import org.easymock.tests.IMethods;
import org.junit.Rule;
import org.junit.Test;

public class DependencyTest extends EasyMockSupport {

    @Rule
    public FilteringRule rule = new FilteringRule("net.sf.cglib", "org.objenesis");

    @Test
    public void testInterfaceMocking() {
        final IMethods mock = createMock(IMethods.class);
        expect(mock.booleanReturningMethod(1)).andReturn(true);
        replayAll();
        assertTrue(mock.booleanReturningMethod(1));
        verifyAll();
    }

    @Test
    public void testClassMocking() {
        try {
            final DependencyTest mock = createMock(DependencyTest.class);
            fail("Should throw an exception due to a NoClassDefFoundError");
        } catch (final RuntimeException e) {
            assertEquals("Class mocking requires to have cglib and objenesis librairies in the classpath", e
                    .getMessage());
            assertTrue(e.getCause() instanceof NoClassDefFoundError);
        }
    }
}
