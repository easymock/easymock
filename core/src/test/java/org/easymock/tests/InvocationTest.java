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

import java.lang.reflect.Method;

import org.easymock.internal.Invocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class InvocationTest {

    private Invocation call;

    private Invocation equalCall;

    private Invocation nonEqualCall;

    @BeforeEach
    public void setup() throws SecurityException, NoSuchMethodException {
        Object[] arguments1 = new Object[] { "" };
        Object[] arguments2 = new Object[] { "" };
        Object[] arguments3 = new Object[] { "X" };
        Method m = Object.class.getMethod("equals", Object.class);
        Object mock = new Object();
        call = new Invocation(mock, m, arguments1);
        equalCall = new Invocation(mock, m, arguments2);
        nonEqualCall = new Invocation(mock, m, arguments3);
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testEquals() {
        Assertions.assertNotEquals(null, call);
        Assertions.assertNotEquals("", call);
        Assertions.assertEquals(call, equalCall);
        Assertions.assertNotEquals(call, nonEqualCall);
    }

    @Test
    public void testHashCode() {
        try {
            call.hashCode();
            Assertions.fail();
        } catch (UnsupportedOperationException expected) {
            Assertions.assertEquals("hashCode() is not implemented", expected.getMessage());
        }
    }

    @Test
    public void testShouldDisplayMocksToStringIfValidJavaIdentifier() throws NoSuchMethodException {
        class ToString {
            private final String name;

            public ToString(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return name;
            }

            public void aMethod() {
            }
        }

        Method method = ToString.class.getMethod("aMethod");
        Invocation invocation = new Invocation(new ToString("validJavaIdentifier"), method, null);

        Assertions.assertEquals("validJavaIdentifier.aMethod()", invocation.toString());

        invocation = new Invocation(new ToString("no-valid-java-identifier"), method, null);

        Assertions.assertEquals("Object.aMethod()", invocation.toString());

    }

    @Test
    public void testShouldDisplayMocksToStringIfNoToStringMethod() throws NoSuchMethodException {
        class NoToString {
            private final String name;

            public NoToString(String name) {
                this.name = name;
            }

            public void aMethod() {
            }
        }

        Method method = NoToString.class.getMethod("aMethod");
        Invocation invocation = new Invocation(new NoToString("validJavaIdentifier"), method, null);

        Assertions.assertEquals("aMethod()", invocation.toString());

        invocation = new Invocation(new NoToString("no-valid-java-identifier"), method, null);

        Assertions.assertEquals("aMethod()", invocation.toString());

    }
}
