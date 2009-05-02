/*
 * Copyright (c) 2001-2009 OFFIS, Henri Tremblay.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal.matchers;

import java.io.Serializable;
import java.util.Comparator;

import org.easymock.IArgumentMatcher;
import org.easymock.LogicalOperator;

public class Compare<T> implements IArgumentMatcher, Serializable {

    private static final long serialVersionUID = -4859402739599754147L;

    private T expected;

    private Comparator<? super T> comparator;

    private LogicalOperator operator;

    public Compare(T expected, Comparator<? super T> comparator, LogicalOperator result) {
        this.expected = expected;
        this.comparator = comparator;
        this.operator = result;
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append(comparator + "(" + expected + ") " + operator.getSymbol()
                + " 0");
    }

    @SuppressWarnings("unchecked")
    public boolean matches(Object actual) {
        if(actual == null) {
            return false;
        }
        return operator.matchResult(comparator.compare((T) actual, expected));
    }

}
