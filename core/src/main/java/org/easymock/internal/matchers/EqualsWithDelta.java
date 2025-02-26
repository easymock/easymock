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
package org.easymock.internal.matchers;

import java.io.Serializable;

import org.easymock.IArgumentMatcher;

/**
 * Matches if the argument is a number equal to the given value with some tolerance equal to delta.
 *
 * @author OFFIS, Tammo Freese
 */
public class EqualsWithDelta implements IArgumentMatcher, Serializable {

    private static final long serialVersionUID = -3018631689416120154L;

    private final Number expected;

    private final Number delta;

    public EqualsWithDelta(Number value, Number delta) {
        this.expected = value;
        this.delta = delta;
    }

    @Override
    public boolean matches(Object actual) {
        Number actualNumber = (Number) actual;
        return expected.doubleValue() - delta.doubleValue() <= actualNumber.doubleValue()
                && actualNumber.doubleValue() <= expected.doubleValue() + delta.doubleValue();
    }

    @Override
    public void appendTo(StringBuffer buffer) {
        buffer.append("eq(").append(expected).append(", ").append(delta).append(")");
    }
}
