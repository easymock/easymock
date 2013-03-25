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

import org.easymock.IArgumentMatcher;
import org.easymock.internal.ArgumentToString;

/**
 * @author OFFIS, Tammo Freese
 */
public class Equals implements IArgumentMatcher, Serializable {

    private static final long serialVersionUID = 583055160049982067L;

    private final Object expected;

    public Equals(final Object expected) {
        this.expected = expected;
    }

    public boolean matches(final Object actual) {
        if (this.expected == null) {
            return actual == null;
        }
        return expected.equals(actual);
    }

    public void appendTo(final StringBuffer buffer) {
        ArgumentToString.appendArgument(expected, buffer);
    }

    protected final Object getExpected() {
        return expected;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || !this.getClass().equals(o.getClass())) {
            return false;
        }
        final Equals other = (Equals) o;
        return this.expected == null && other.expected == null || this.expected != null
                && this.expected.equals(other.expected);
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("hashCode() is not supported");
    }

}
