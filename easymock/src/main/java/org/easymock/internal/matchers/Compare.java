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
import java.util.Comparator;

import org.easymock.IArgumentMatcher;
import org.easymock.LogicalOperator;

/**
 * @param <T>
 *            Type of the values compared
 * 
 * @author Henri Tremblay
 */
public class Compare<T> implements IArgumentMatcher, Serializable {

    private static final long serialVersionUID = -4859402739599754147L;

    private final T expected;

    private final Comparator<? super T> comparator;

    private final LogicalOperator operator;

    public Compare(final T expected, final Comparator<? super T> comparator, final LogicalOperator result) {
        this.expected = expected;
        this.comparator = comparator;
        this.operator = result;
    }

    public void appendTo(final StringBuffer buffer) {
        buffer.append(comparator + "(" + expected + ") " + operator.getSymbol() + " 0");
    }

    @SuppressWarnings("unchecked")
    public boolean matches(final Object actual) {
        if (actual == null) {
            return false;
        }
        return operator.matchResult(comparator.compare((T) actual, expected));
    }

}
