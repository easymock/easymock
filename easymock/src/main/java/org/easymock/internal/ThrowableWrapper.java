/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

public class ThrowableWrapper extends Throwable {

    private static final long serialVersionUID = -4434322855124959723L;
    
    private final Throwable throwable;

    public ThrowableWrapper(final Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
