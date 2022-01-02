/*
 * Copyright 2001-2022 the original author or authors.
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
package org.easymock.internal;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class InjectorTest {

    private static class StaticClass {}

    private class InnerClass {}

    private String noArg;
    private URL noNoArg;
    private PrivateNoArgConstructor privateNoArgConstructor;
    private ThrowingNoArgConstructor throwingNoArgConstructor;
    private StaticClass staticClass;
    private InnerClass innerClass;

    @Test
    public void testInstantiateTestSubject_existingNoArg() {
        String actual = Injector.instantiateTestSubject(field("noArg"));
        assertEquals("", actual);
    }

    @Test
    public void testInstantiateTestSubject_noNoArg() {
        AssertionError e = assertThrows(AssertionError.class, () -> Injector.instantiateTestSubject(field("noNoArg")));
        assertEquals("TestSubject is null and has no default constructor. You need to instantiate 'noNoArg' manually", e.getMessage());
    }

    @Test
    public void testInstantiateTestSubject_noArgWithException() {
        AssertionError e = assertThrows(AssertionError.class, () -> Injector.instantiateTestSubject(field("throwingNoArgConstructor")));
        assertEquals("TestSubject is null and default constructor fails on invocation. You need to instantiate 'throwingNoArgConstructor' manually", e.getMessage());
        assertEquals(InvocationTargetException.class, e.getCause().getClass());
        assertEquals("Test", e.getCause().getCause().getMessage());
    }

    @Test
    public void testInstantiateTestSubject_privateNoArg() {
        PrivateNoArgConstructor actual = Injector.instantiateTestSubject(field("privateNoArgConstructor"));
        assertNotNull(actual);
    }

    @Test
    public void testInstantiateTestSubject_staticClass() {
        StaticClass actual = Injector.instantiateTestSubject(field("staticClass"));
        assertNotNull(actual);
    }

    @Test
    public void testInstantiateTestSubject_innerClass() {
        AssertionError e = assertThrows(AssertionError.class, () -> Injector.instantiateTestSubject(field("innerClass")));
        assertEquals("TestSubject is an inner class. You need to instantiate 'innerClass' manually", e.getMessage());
    }

    private Field field(String name) {
        try {
            return getClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
