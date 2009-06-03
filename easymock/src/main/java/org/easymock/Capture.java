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
package org.easymock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Will contain what was captured by the <code>capture()</code> matcher. Knows
 * if something was captured or not (allows to capture a null value).
 * 
 * @param <T>
 *            Type of the captured element
 */
public class Capture<T> implements Serializable {

    private static final long serialVersionUID = -4214363692271370781L;

    private CaptureType type;

    private final List<T> values = new ArrayList<T>(2);

    /**
     * Default constructor. Only the last element will be captured
     */
    public Capture() {
        this(CaptureType.LAST);
    }

    /**
     * Constructor allowing to select the capture type
     * 
     * @param type
     *            capture type
     */
    public Capture(CaptureType type) {
        this.type = type;
    }
    
    /**
     * Will reset capture to a "nothing captured yet" state
     */
    public void reset() {
        values.clear();
    }

    /**
     * @return true if something was captured
     */
    public boolean hasCaptured() {
        return !values.isEmpty();
    }

    /**
     * Return captured value
     * 
     * @throws AssertionError
     *             if nothing was captured yet or if more than one value was
     *             captured
     * @return The last captured value
     */
    public T getValue() {
        if (values.isEmpty()) {
            throw new AssertionError("Nothing captured yet");
        }
        if (values.size() > 1) {
            throw new AssertionError("More than one value captured: "
                    + getValues());
        }
        return values.get(values.size() - 1);
    }

    /**
     * Return all captured values. It returns the actual list so you can modify
     * it's content if needed
     * 
     * @return The currently captured values
     */
    public List<T> getValues() {
        return values;
    }

    /**
     * Used internally by the EasyMock framework to add a new captured value
     * 
     * @param value
     *            Value captured
     */
    public void setValue(T value) {
        switch (type) {
        case NONE:
            break;
        case ALL:
            values.add(value);
            break;
        case FIRST:
            if (!hasCaptured()) {
                values.add(value);
            }
            break;
        case LAST:
            if (hasCaptured()) {
                reset();
            }
            values.add(value);
            break;
        // ///CLOVER:OFF
        default:
            throw new IllegalArgumentException("Unknown capture type: " + type);
        // ///CLOVER:ON
        }
    }

    @Override
    public String toString() {
        if (values.isEmpty()) {
            return "Nothing captured yet";
        }
        if (values.size() == 1) {
            return String.valueOf(values.get(0));
        }
        return values.toString();
    }
}
