/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import java.io.PrintWriter;
import java.io.StringWriter;

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
}
