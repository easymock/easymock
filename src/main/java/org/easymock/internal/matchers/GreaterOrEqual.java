/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal.matchers;

public class GreaterOrEqual<T extends Comparable<T>> extends CompareTo<T> {

    private static final long serialVersionUID = -504083241204488174L;

    public GreaterOrEqual(Comparable<T> value) {
        super(value);
    }

    @Override
    protected String getName() {
        return "geq";
    }

    @Override
    protected boolean matchResult(int result) {
        return result >= 0;
    }
}
