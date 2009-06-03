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
