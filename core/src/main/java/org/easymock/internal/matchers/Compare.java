/*
 * Copyright 2001-2023 the original author or authors.
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
import java.util.Comparator;

import org.easymock.IArgumentMatcher;
import org.easymock.LogicalOperator;

/**
 * Matches if the argument, when compared (<code>Comparator.compare()</code>), agrees with the logical operator.
 *
 * @param <T> type of the values compared
 *
 * @author Henri Tremblay
 */
public class Compare<T> implements IArgumentMatcher, Serializable {

    private static final long serialVersionUID = -4859402739599754147L;

    private final T expected;

    private final Comparator<? super T> comparator;

    private final LogicalOperator operator;

    public Compare(T expected, Comparator<? super T> comparator, LogicalOperator result) {
        this.expected = expected;
        this.comparator = comparator;
        this.operator = result;
    }

    @Override
    public void appendTo(StringBuffer buffer) {
        buffer.append(comparator).append("(").append(expected).append(") ").append(operator.getSymbol()).append(" 0");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean matches(Object actual) {
        if (actual == null) {
            return false;
        }
        return operator.matchResult(comparator.compare((T) actual, expected));
    }

}
