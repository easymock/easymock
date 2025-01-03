/*
 * Copyright 2001-2025 the original author or authors.
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

import org.easymock.IMocksControl;
import org.easymock.MockType;
import org.easymock.internal.MocksControl;
import org.easymock.internal.ObjectMethodsFilter;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author OFFIS, Tammo Freese, Henri Tremblay
 */
public final class Util {

    private Util() {
    }

    public static String getStackTrace(Throwable throwable) {
        StringWriter stackTrace = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stackTrace));
        return stackTrace.getBuffer().toString();
    }

    public static boolean startWithClass(Throwable throwable, Class<?> clazz) {
        StackTraceElement[] elements = throwable.getStackTrace();
        return elements[0].getClassName().equals(clazz.getName());
    }

    public static IMocksControl getControl(Object o) {
        return MocksControl.getControl(o);
    }

    public static MockType getType(Object o) {
        MocksControl control = MocksControl.getControl(o);
        return control.getType();
    }

    public static String getName(Object o) {
        ObjectMethodsFilter handler = (ObjectMethodsFilter) MocksControl.getInvocationHandler(o);
        return handler.getName();
    }
}
