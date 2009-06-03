/*
 * Copyright 2001-2009 OFFIS, Tammo Freese
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

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.easymock.MockControl;
import org.easymock.internal.ObjectMethodsFilter;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class ObjectMethodsTest {
    private MockControl<EmptyInterface> control;

    private EmptyInterface mock;

    private interface EmptyInterface {
    }

    @Before
    public void setup() {
        control = MockControl.createControl(EmptyInterface.class);
        mock = control.getMock();
    }

    @Test
    public void equalsBeforeActivation() {
        assertEquals(mock, mock);
        assertTrue(!mock.equals(null));
    }

    @Test
    public void equalsAfterActivation() {
        control.replay();
        assertEquals(mock, mock);
        assertTrue(!mock.equals(null));
    }

    @Test
    public void testHashCode() {
        int hashCodeBeforeActivation = mock.hashCode();
        control.replay();
        int hashCodeAfterActivation = mock.hashCode();
        assertEquals(hashCodeBeforeActivation, hashCodeAfterActivation);
    }

    @Test
    public void toStringBeforeActivation() {
        assertEquals("EasyMock for " + EmptyInterface.class.toString(), mock
                .toString());
    }

    @Test
    public void toStringAfterActivation() {
        control.replay();
        assertEquals("EasyMock for " + EmptyInterface.class.toString(), mock
                .toString());
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
        Method toString = Object.class.getMethod("toString", new Class[0]);
        assertEquals("EasyMock for " + MockedClass.class.toString(), filter
                .invoke(new DummyProxy(), toString, new Object[0]));
    }

}
