/*
 * Copyright 2001-2023 the original author or authors.
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

import org.easymock.internal.ObjectMethodsFilter;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author OFFIS, Tammo Freese
 */
public class ObjectMethodsTest {

    private EmptyInterface mock;

    private interface EmptyInterface {
    }

    @Before
    public void setup() {
        mock = createMock(EmptyInterface.class);
    }

    @Test
    public void equalsBeforeActivation() {
        assertEquals(mock, mock);
        assertTrue(!mock.equals(null));
    }

    @Test
    public void equalsAfterActivation() {
        replay(mock);
        assertEquals(mock, mock);
        assertTrue(!mock.equals(null));
    }

    @Test
    public void testHashCode() {
        int hashCodeBeforeActivation = mock.hashCode();
        replay(mock);
        int hashCodeAfterActivation = mock.hashCode();
        assertEquals(hashCodeBeforeActivation, hashCodeAfterActivation);
    }

    @Test
    public void toStringBeforeActivation() {
        assertEquals("EasyMock for " + EmptyInterface.class, mock.toString());
    }

    @Test
    public void toStringAfterActivation() {
        replay(mock);
        assertEquals("EasyMock for " + EmptyInterface.class, mock.toString());
    }

    private static class MockedClass {
    }

    private static class DummyProxy extends MockedClass {
    }

    // if the class is no Proxy, ObjectMethodFilter should use the
    // superclasses' name. This is needed for the class extension.
    @Test
    public void toStringForClasses() throws Throwable {
        ObjectMethodsFilter filter = new ObjectMethodsFilter(Object.class, null, null);
        Method toString = Object.class.getMethod("toString");
        assertEquals("EasyMock for " + MockedClass.class, filter.invoke(new DummyProxy(),
                toString, new Object[0]));
    }

}
