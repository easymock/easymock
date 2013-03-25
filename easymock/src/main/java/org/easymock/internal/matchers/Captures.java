/**
 * Copyright 2001-2013 the original author or authors.
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
package org.easymock.internal.matchers;

import java.io.Serializable;

import org.easymock.Capture;
import org.easymock.IArgumentMatcher;
import org.easymock.internal.LastControl;

/**
 * @param <T>
 *            Type of the value captured
 * 
 * @author Henri Tremblay
 */
public class Captures<T> implements IArgumentMatcher, Serializable {

    private static final long serialVersionUID = -5048595127450771363L;

    private final Capture<T> capture;

    private T potentialValue;

    public Captures(final Capture<T> captured) {
        this.capture = captured;
    }

    public void appendTo(final StringBuffer buffer) {
        buffer.append("capture(").append(capture).append(")");
    }

    public void setPotentialValue(final T potentialValue) {
        this.potentialValue = potentialValue;
    }

    @SuppressWarnings("unchecked")
    public boolean matches(final Object actual) {
        LastControl.getCurrentInvocation().addCapture((Captures<Object>) this, actual);
        return true;
    }

    public void validateCapture() {
        capture.setValue(potentialValue);
    }
}
