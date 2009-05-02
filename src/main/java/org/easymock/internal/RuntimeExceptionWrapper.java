/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

public class RuntimeExceptionWrapper extends RuntimeException {

    private static final long serialVersionUID = -3483500330975410177L;
    
    private final RuntimeException runtimeException;

    public RuntimeExceptionWrapper(final RuntimeException runtimeException) {
        this.runtimeException = runtimeException;
    }

    public RuntimeException getRuntimeException() {
        return runtimeException;
    }
}
