/**
 * Copyright 2001-2011 the original author or authors.
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
package org.easymock.samples;

import static org.easymock.EasyMock.*;

import org.easymock.IArgumentMatcher;

/**
 * @author OFFIS, Tammo Freese
 */
public class ThrowableEquals implements IArgumentMatcher {
    private final Throwable expected;

    public ThrowableEquals(final Throwable expected) {
        this.expected = expected;
    }

    public boolean matches(final Object actual) {
        if (!(actual instanceof Throwable)) {
            return false;
        }
        final String actualMessage = ((Throwable) actual).getMessage();
        return expected.getClass().equals(actual.getClass()) && expected.getMessage().equals(actualMessage);
    }

    public void appendTo(final StringBuffer buffer) {
        buffer.append("<");
        buffer.append(expected.getClass().getName());
        buffer.append(" with message \"");
        buffer.append(expected.getMessage());
        buffer.append("\">");

    }

    public static <T extends Throwable> T eqException(final T in) {
        reportMatcher(new ThrowableEquals(in));
        return in;
    }
}
