/**
 * Copyright 2001-2017 the original author or authors.
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author OFFIS, Tammo Freese
 */
public class Results implements Serializable {

    private static final long serialVersionUID = -2722051869610289637L;

    private int callCount;

    private final List<Range> ranges = new ArrayList<Range>();

    private final List<Result> results = new ArrayList<Result>();

    public void add(Result result, Range range) {
        if (!ranges.isEmpty()) {
            Range lastRange = ranges.get(ranges.size() - 1);
            if (!lastRange.hasFixedCount()) {
                throw new RuntimeExceptionWrapper(new IllegalStateException(
                        "last method called on mock already has a non-fixed count set."));
            }
        }
        ranges.add(range);
        results.add(result);
    }

    public boolean hasResults() {
        int currentPosition = 0;
        for (int i = 0; i < ranges.size(); i++) {
            Range interval = ranges.get(i);
            if (interval.hasOpenCount()) {
                return true;
            }
            currentPosition += interval.getMaximum();
            if (currentPosition > callCount) {
                return true;
            }
        }
        return false;
    }

    public Result next() {
        int currentPosition = 0;
        for (int i = 0; i < ranges.size(); i++) {
            Range interval = ranges.get(i);
            if (interval.hasOpenCount()) {
                callCount += 1;
                return results.get(i);
            }
            currentPosition += interval.getMaximum();
            if (currentPosition > callCount) {
                callCount += 1;
                return results.get(i);
            }
        }
        return null;
    }

    public boolean hasValidCallCount() {
        return getMainInterval().contains(callCount);
    }

    @Override
    public String toString() {
        return getMainInterval().expectedCount();
    }

    private Range getMainInterval() {
        int min = 0, max = 0;

        for (Range interval : ranges) {
            min += interval.getMinimum();
            if (interval.hasOpenCount() || max == Integer.MAX_VALUE) {
                max = Integer.MAX_VALUE;
            } else {
                max += interval.getMaximum();
            }
        }

        return new Range(min, max);
    }

    public int getCallCount() {
        return callCount;
    }
}
