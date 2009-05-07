/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.Array;

public final class ArgumentToString {

    // ///CLOVER:OFF
    private ArgumentToString() {
    }
    // ///CLOVER:ON
    
    public static void appendArgument(Object value, StringBuffer buffer) {
        if (value == null) {
            buffer.append("null");
        } else if (value instanceof String) {
            buffer.append("\"");
            buffer.append(value);
            buffer.append("\"");
        } else if (value instanceof Character) {
            buffer.append("'");
            buffer.append(value);
            buffer.append("'");
        } else if (value.getClass().isArray()) {
            buffer.append("[");
            for (int i = 0; i < Array.getLength(value); i++) {
                if (i > 0) {
                    buffer.append(", ");   
                }
                appendArgument(Array.get(value, i), buffer);
            }
            buffer.append("]");
        } else {
            buffer.append(value);
        }
    }
}
