/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal.matchers;

import java.io.Serializable;

import org.easymock.IArgumentMatcher;
import org.easymock.internal.ArgumentToString;

public class Equals implements IArgumentMatcher, Serializable {

    private static final long serialVersionUID = 583055160049982067L;

    private final Object expected;

    public Equals(Object expected) {
        this.expected = expected;
    }

    public boolean matches(Object actual) {
        if (this.expected == null) {
            return actual == null;
        }
        return expected.equals(actual);
    }

    public void appendTo(StringBuffer buffer) {
        ArgumentToString.appendArgument(expected, buffer);
    }

    protected final Object getExpected() {
        return expected;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !this.getClass().equals(o.getClass()))
            return false;
        Equals other = (Equals) o;
        return this.expected == null && other.expected == null
                || this.expected != null
                && this.expected.equals(other.expected);
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("hashCode() is not supported");
    }

}
