/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock;

/**
 * Defines how arguments will be captured by a <tt>Capture</tt> object
 * 
 * @see Capture
 */
public enum CaptureType {
    /**
     * Do not capture anything
     */
    NONE,

    /**
     * Will capture the argument of the first matching call
     */
    FIRST,

    /**
     * Will capture the argument of the last matching call
     */
    LAST,

    /**
     * Will capture, in order, the arguments of each matching calls
     */
    ALL
}
