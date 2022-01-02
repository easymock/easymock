/*
 * Copyright 2001-2022 the original author or authors.
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

/**
 * Utility class to convert method arguments to Strings
 *
 * @author Henri Tremblay
 */
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
            int length = Array.getLength(value);
            int printedLength = Math.min(100, length);
            for (int i = 0; i < printedLength; i++) {
                if (i > 0) {
                    buffer.append(", ");
                }
                appendArgument(Array.get(value, i), buffer);
            }
            if(length > printedLength) {
                buffer.append("... (length=").append(length).append(")");
            }
            buffer.append("]");
        } else if (PrimitiveUtils.isPrimitiveWrapper(value.getClass())) {
            buffer.append(value)
                .append(" (")
                .append(PrimitiveUtils.getPrimitiveTypeNameFromWrapper(value.getClass()))
                .append(")");
        } else {
            buffer.append(value);
        }
    }

    /**
     * Converts an argument to a String using
     * {@link #appendArgument(Object, StringBuffer)}
     *
     * @param argument
     *            the argument to convert to a String.
     * @return a {@code String} representation of the argument.
     */
    public static String argumentToString(Object argument) {
        StringBuffer result = new StringBuffer();
        ArgumentToString.appendArgument(argument, result);
        return result.toString();
    }

    /**
     * Returns a string representation of the arguments. This convenience
     * implementation calls {@link #argumentToString(Object)} for every argument
     * in the given array and returns the string representations of the
     * arguments separated by commas.
     *
     * @param arguments
     *            the arguments to be used in the string representation.
     * @return a string representation of the matcher.
     */
    public static String argumentsToString(Object... arguments) {
        if (arguments == null) {
            arguments = new Object[0];
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < arguments.length; i++) {
            if (i > 0) {
                result.append(", ");
            }
            result.append(argumentToString(arguments[i]));
        }
        return result.toString();
    }
}
