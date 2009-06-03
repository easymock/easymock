/*
 * Copyright 2003-2009 OFFIS, Henri Tremblay
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
