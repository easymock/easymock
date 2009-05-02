/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal.matchers;

import java.io.Serializable;

import org.easymock.IArgumentMatcher;

public class Contains implements IArgumentMatcher, Serializable {

    private static final long serialVersionUID = -6785245714002503134L;

    private final String substring;

    public Contains(String substring) {
        this.substring = substring;
    }

    public boolean matches(Object actual) {
        return (actual instanceof String)
                && ((String) actual).indexOf(substring) >= 0;
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("contains(\"" + substring + "\")");
    }
}
