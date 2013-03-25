/**
 * Copyright 2001-2013 the original author or authors.
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

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author OFFIS, Tammo Freese
 */
public final class Util {

    private Util() {
    }

    public static String getStackTrace(final Throwable throwable) {
        final StringWriter stackTrace = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stackTrace));
        return stackTrace.getBuffer().toString();
    }

    public static boolean startWithClass(final Throwable throwable, final Class<?> clazz) {
        final StackTraceElement[] elements = throwable.getStackTrace();
        return elements[0].getClassName().equals(clazz.getName());
    }
}
