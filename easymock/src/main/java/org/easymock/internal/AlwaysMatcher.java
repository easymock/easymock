/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import org.easymock.AbstractMatcher;

@SuppressWarnings("deprecation")
public class AlwaysMatcher extends AbstractMatcher {

    private static final long serialVersionUID = 592339838132342008L;

    public boolean matches(Object[] expected, Object[] actual) {
        return true;
    }

    protected String argumentToString(Object argument) {
        return "<any>";
    }
}