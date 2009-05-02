/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal.matchers;

import java.io.Serializable;

import org.easymock.IArgumentMatcher;

public class EndsWith implements IArgumentMatcher, Serializable {

    private static final long serialVersionUID = 5159338714596685067L;

    private final String suffix;

    public EndsWith(String suffix) {
        this.suffix = suffix;
    }

    public boolean matches(Object actual) {
        return (actual instanceof String) && ((String) actual).endsWith(suffix);
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("endsWith(\"" + suffix + "\")");
    }
}
