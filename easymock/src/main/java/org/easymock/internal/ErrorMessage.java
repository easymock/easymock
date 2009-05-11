/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

public class ErrorMessage {

    private final boolean matching;

    private final String message;

    private final int actualCount;

    public ErrorMessage(boolean matching, String message, int actualCount) {
        this.matching = matching;
        this.message = message;
        this.actualCount = actualCount;
    }

    public boolean isMatching() {
        return matching;
    }

    public String getMessage() {
        return message;
    }

    public int getActualCount() {
        return actualCount;
    }

    public void appendTo(StringBuilder buffer, int matches) {
        buffer.append("\n    ").append(message).append(", actual: ");
        if (matching) {
            if (matches == 1) {
                buffer.append(getActualCount() + 1);
            } else {
                buffer.append(getActualCount());
                buffer.append(" (+1)");
            }
        }
        else {
            buffer.append(getActualCount());
        }        
    }    
}
