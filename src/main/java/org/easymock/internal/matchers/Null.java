/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal.matchers;

import java.io.Serializable;

import org.easymock.IArgumentMatcher;

public class Null implements IArgumentMatcher, Serializable {

    private static final long serialVersionUID = 6077244839421122011L;

    public static final Null NULL = new Null();

    private Null() {
    }

    public boolean matches(Object actual) {
        return actual == null;
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("isNull()");
    }
}
