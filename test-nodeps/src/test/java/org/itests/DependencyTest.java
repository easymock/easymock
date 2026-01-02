/*
 * Copyright 2015-2026 the original author or authors.
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
import org.main.Main;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * @author Henri Tremblay
 */
public class DependencyTest {

    public interface IMethods {
        boolean booleanReturningMethod(int i);
    }

    private final EasyMockSupport support = new EasyMockSupport();

    @Rule
    public FilteringRule rule = new FilteringRule("org.objenesis", "org.depends.Dependency");

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
        RuntimeException e = assertThrows(RuntimeException.class, () -> createMock(DependencyTest.class));
        assertEquals("Class mocking requires to have Objenesis library in the classpath", e.getMessage());
        assertInstanceOf(NoClassDefFoundError.class, e.getCause());
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
        RuntimeException e = assertThrows(RuntimeException.class, () -> support.createMock(DependencyTest.class));
        assertEquals("Class mocking requires to have Objenesis library in the classpath", e.getMessage());
        assertInstanceOf(NoClassDefFoundError.class, e.getCause());
    }

    @Test
    public void testStillNoClassDefFoundErrorWhenSomeOtherClassIsMissing() {
        NoClassDefFoundError e = assertThrows(NoClassDefFoundError.class, () -> support.createMock(Main.class));
        assertEquals("org/depends/Dependency", e.getMessage());
    }
}
