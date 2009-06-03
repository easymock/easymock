/*
 * Copyright 2001-2009 OFFIS, Tammo Freese
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

import java.io.Serializable;

public class Range implements Serializable {

    private static final long serialVersionUID = -6743402320315331536L;

    private final int minimum;

    private final int maximum;

    public Range(int count) {
        this(count, count);
    }

    public Range(int minimum, int maximum) {
        if (!(minimum <= maximum)) {
            throw new RuntimeExceptionWrapper(new IllegalArgumentException(
                    "minimum must be <= maximum"));
        }

        if (!(minimum >= 0)) {
            throw new RuntimeExceptionWrapper(new IllegalArgumentException(
                    "minimum must be >= 0"));
        }

        if (!(maximum >= 1)) {
            throw new RuntimeExceptionWrapper(new IllegalArgumentException(
                    "maximum must be >= 1"));
        }
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public boolean hasFixedCount() {
        return minimum == maximum;
    }

    public int getMaximum() {
        return maximum;
    }

    public int getMinimum() {
        return minimum;
    }

    @Override
    public String toString() {
        if (hasFixedCount()) {
            return "" + minimum;
        } else if (hasOpenCount()) {
            return "at least " + minimum;
        } else {
            return "between " + minimum + " and " + maximum;
        }
    }

    public String expectedCount() {
        return "expected: " + this.toString();
    }

    public boolean contains(int count) {
        return minimum <= count && count <= maximum;
    }

    public boolean hasOpenCount() {
        return maximum == Integer.MAX_VALUE;
    }
}
