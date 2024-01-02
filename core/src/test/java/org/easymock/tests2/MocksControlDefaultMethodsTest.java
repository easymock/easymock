/*
 * Copyright 2001-2024 the original author or authors.
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

import org.easymock.internal.ReflectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.function.Function;

import static org.easymock.EasyMock.createControl;
import static org.junit.Assert.assertEquals;

/**
 * @author Henri Tremblay
 */
public class MocksControlDefaultMethodsTest {

    @Test
    public void emptyInterface() {
        expectPartialMocking("an empty interface", false, Cloneable.class);
    }

    @Test
    public void interfaceWithoutDefaultMethods() {
        expectPartialMocking("an interface without default methods", false, Runnable.class);
    }

    @Test
    public void interfaceWithDefaultMethodButNoMockedMethods() {
        expectPartialMocking("an interface with a default method", true, Function.class);
    }

    @Test
    public void interfaceWithDefaultMethodAndMockedMethods() {
        expectPartialMocking("an interface with a default method and a mocked method", true, Function.class,
            ReflectionUtils.findMethod(Function.class, "andThen", method -> true));
    }

    @Test
    public void interfaceWithInheritedDefaultMethod() {
        expectPartialMocking("an interface with an inherited default method", true,
            InterfaceWithInheritedDefaultMethod.class);
    }

    @Test
    public void interfaceWithMockedMethodThatIsNotADefaultMethod() {
        expectPartialMocking("an interface with a mocked method that is not a default method", false,
            Function.class, ReflectionUtils.findMethod(Function.class, "apply", method -> true));
    }

    private void expectPartialMocking(String caseName, boolean expected, Class<?> toMock, Method... mockedMethods) {
        String allowanceText = "should" + (expected ? "" : "n't") + " be allowed";
        String message = "partial mocking on " + caseName + " " + allowanceText;
        Assertions.assertEquals(expected, tryMock(toMock, mockedMethods), message);
    }

    private boolean tryMock(Class<?> toMock, Method... mockedMethods) {
        try {
            createControl().createMock(null, toMock, null, mockedMethods);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private interface InterfaceWithDefaultMethod {
        default void method() {}
    }

    private interface InterfaceWithInheritedDefaultMethod extends InterfaceWithDefaultMethod {}
}
