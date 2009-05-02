/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal.matchers;

import java.io.Serializable;

import org.easymock.Capture;
import org.easymock.IArgumentMatcher;

public class Captures<T> implements IArgumentMatcher, Serializable {

    private static final long serialVersionUID = 5370646694100653250L;
    
    private Capture<T> capture;

    public Captures(Capture<T> captured) {
        this.capture = captured;
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("capture(").append(capture).append(")");
    }

    @SuppressWarnings("unchecked")
    public boolean matches(Object actual) {
        capture.setValue((T) actual);
        return true;
    }
}
