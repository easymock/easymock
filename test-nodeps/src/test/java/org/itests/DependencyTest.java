/**
 * Copyright 2015-2017 the original author or authors.
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
package org.itests;

import org.easymock.EasyMockSupport;
import org.junit.Rule;
import org.junit.Test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Henri Tremblay
 */
public class DependencyTest {

    public interface IMethods {
        boolean booleanReturningMethod(int i);
    }

    private final EasyMockSupport support = new EasyMockSupport();

    @Rule
    public FilteringRule rule = new FilteringRule("org.objenesis");

    @Test
    public void testInterfaceMocking() {
        IMethods mock = createMock(IMethods.class);
        expect(mock.booleanReturningMethod(1)).andReturn(true);
        replay(mock);
        assertTrue(mock.booleanReturningMethod(1));
        verify(mock);
    }

    @Test
    public void testClassMocking() {
        try {
            createMock(DependencyTest.class);
            fail("Should throw an exception due to a NoClassDefFoundError");
        } catch (RuntimeException e) {
            assertEquals("Class mocking requires to have objenesis library in the classpath", e
                    .getMessage());
            assertTrue(e.getCause() instanceof NoClassDefFoundError);
        }
    }

    @Test
    public void testInterfaceMockingSupport() {
        IMethods mock = support.createMock(IMethods.class);
        expect(mock.booleanReturningMethod(1)).andReturn(true);
        support.replayAll();
        assertTrue(mock.booleanReturningMethod(1));
        support.verifyAll();
    }

    @Test
    public void testClassMockingSupport() {
        try {
            support.createMock(DependencyTest.class);
            fail("Should throw an exception due to a NoClassDefFoundError");
        } catch (RuntimeException e) {
            assertEquals("Class mocking requires to have objenesis library in the classpath", e
                    .getMessage());
            assertTrue(e.getCause() instanceof NoClassDefFoundError);
        }
    }
}
