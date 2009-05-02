/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.samples;

import static org.easymock.EasyMock.reportMatcher;

import org.easymock.IArgumentMatcher;

public class ThrowableEquals implements IArgumentMatcher {
    private Throwable expected;

    public ThrowableEquals(Throwable expected) {
        this.expected = expected;
    }

    public boolean matches(Object actual) {
        if (!(actual instanceof Throwable)) {
            return false;
        }
        String actualMessage = ((Throwable) actual).getMessage();
        return expected.getClass().equals(actual.getClass())
                && expected.getMessage().equals(actualMessage);
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("<");
        buffer.append(expected.getClass().getName());
        buffer.append(" with message \"");
        buffer.append(expected.getMessage());
        buffer.append("\">");

    }

    public static <T extends Throwable> T eqException(T in) {
        reportMatcher(new ThrowableEquals(in));
        return in;
    }            
}
