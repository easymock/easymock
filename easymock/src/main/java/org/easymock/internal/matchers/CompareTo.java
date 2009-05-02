/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal.matchers;

import java.io.Serializable;

import org.easymock.IArgumentMatcher;

public abstract class CompareTo<T extends Comparable<T>> implements IArgumentMatcher, Serializable {

    private static final long serialVersionUID = -8447010713532143168L;
    
    private final Comparable<T> expected;

    public CompareTo(Comparable<T> value) {
        this.expected = value;
    }

    @SuppressWarnings("unchecked")
    public boolean matches(Object actual) {
        
        if(!(actual instanceof Comparable)) {
            return false;
        }
        
        return matchResult(((Comparable) actual).compareTo(expected));
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append(getName() + "(" + expected + ")");
    }
    
    protected abstract String getName();
    
    protected abstract boolean matchResult(int result);
}
