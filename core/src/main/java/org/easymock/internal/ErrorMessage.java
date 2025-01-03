/*
 * Copyright 2001-2025 the original author or authors.
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

/**
 * The full content of an error message reporting to the user.
 *
 * @author OFFIS, Tammo Freese
 */
public class ErrorMessage {

    private final boolean matching;

    private final String message;

    private final int actualCount;

    public ErrorMessage(boolean matching, String message, int actualCount) {
        this.matching = matching;
        this.message = message;
        this.actualCount = actualCount;
    }

    /**
     * If the actual invocation matched the expected invocation. It will be used to write the final error message telling
     * that some recording are matching but were already used.
     *
     * @return if the actual invocation matched the expected invocation
     */
    public boolean isMatching() {
        return matching;
    }

    /**
     * The actual invocation and its result.
     *
     * @return the actual invocation and its result
     */
    public String getMessage() {
        return message;
    }

    /**
     * How many time an expected invocation was actually invoked.
     *
     * @return how many time an expected invocation was actually invoked
     */
    public int getActualCount() {
        return actualCount;
    }

    /**
     * Add the error message to the buffer.
     *
     * @param buffer the buffer to append to
     * @param matches how many times an actual invocation matched expected invocation
     */
    public void appendTo(StringBuilder buffer, int matches) {
        buffer.append("\n    ").append(message).append(", actual: ");
        if (matching) {
            if (matches == 1) {
                buffer.append(actualCount + 1);
            } else {
                buffer.append(actualCount);
                buffer.append(" (+1)");
            }
        } else {
            buffer.append(actualCount);
        }
    }
}
