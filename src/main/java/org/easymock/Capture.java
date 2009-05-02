/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock;

import java.io.Serializable;

/**
 * Will contain what was captured by the <code>capture()</code> matcher. Knows if
 * something was captured or not (allows to capture a null value).
 *
 * @param <T> Type of the captured element
 */
public class Capture<T> implements Serializable {

    private static final long serialVersionUID = -4214363692271370781L;

    private boolean captured = false;

    private T value;

    /**
     * Will reset capture to a "nothing captured yet" state
     */
    public void reset() {
        value = null;
        captured = false;
    }
    
    /**
     * @return true if something was captured 
     */
    public boolean hasCaptured() {
        return captured;
    }

    /**
     * Return the captured value
     * 
     * @throws AssertionError if nothing was captured yet
     * @return What was captured
     */
    public T getValue() {
        if (!captured) {
            throw new AssertionError("Nothing captured yet");
        }
        return value;
    }

    /**
     * Used internally by the EasyMock framework to set the captured value
     * @param value Value captured
     */
    public void setValue(T value) {
        this.value = value;
        this.captured = true;
    }

    @Override
    public String toString() {
        if (!captured) {
            return "Nothing captured yet";
        }
        return value == null ? null : value.toString();
    }
}
