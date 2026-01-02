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
package org.easymock.internal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

class InjectorTest {

    private static class StaticClass {}

    private class InnerClass {}

    private String noArg;
    private URL noNoArg;
    private PrivateNoArgConstructor privateNoArgConstructor;
    private ThrowingNoArgConstructor throwingNoArgConstructor;
    private StaticClass staticClass;
    private InnerClass innerClass;

    @Test
    void testInstantiateTestSubject_existingNoArg() {
        String actual = Injector.instantiateTestSubject(field("noArg"));
        Assertions.assertEquals("", actual);
    }

    @Test
    void testInstantiateTestSubject_noNoArg() {
        AssertionError e = assertThrows(AssertionError.class, () -> Injector.instantiateTestSubject(field("noNoArg")));
        Assertions.assertEquals("TestSubject is null and has no default constructor. You need to instantiate 'noNoArg' manually", e.getMessage());
    }

    @Test
    void testInstantiateTestSubject_noArgWithException() {
        AssertionError e = assertThrows(AssertionError.class, () -> Injector.instantiateTestSubject(field("throwingNoArgConstructor")));
        Assertions.assertEquals("TestSubject is null and default constructor fails on invocation. You need to instantiate 'throwingNoArgConstructor' manually", e.getMessage());
        Assertions.assertEquals(InvocationTargetException.class, e.getCause().getClass());
        Assertions.assertEquals("Test", e.getCause().getCause().getMessage());
    }

    @Test
    void testInstantiateTestSubject_privateNoArg() {
        PrivateNoArgConstructor actual = Injector.instantiateTestSubject(field("privateNoArgConstructor"));
        Assertions.assertNotNull(actual);
    }

    @Test
    void testInstantiateTestSubject_staticClass() {
        StaticClass actual = Injector.instantiateTestSubject(field("staticClass"));
        Assertions.assertNotNull(actual);
    }

    @Test
    void testInstantiateTestSubject_innerClass() {
        AssertionError e = assertThrows(AssertionError.class, () -> Injector.instantiateTestSubject(field("innerClass")));
        Assertions.assertEquals("TestSubject is an inner class. You need to instantiate 'innerClass' manually", e.getMessage());
    }

    private Field field(String name) {
        try {
            return getClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
