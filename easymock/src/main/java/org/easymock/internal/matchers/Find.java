/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal.matchers;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.easymock.IArgumentMatcher;

public class Find implements IArgumentMatcher, Serializable {

    private static final long serialVersionUID = -7104607303959381785L;

    private final String regex;

    public Find(String regex) {
        this.regex = regex;
    }

    public boolean matches(Object actual) {
        return (actual instanceof String)
                && Pattern.compile(regex).matcher((String) actual).find();
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("find(\"" + regex.replaceAll("\\\\", "\\\\\\\\") + "\")");
    }
}
