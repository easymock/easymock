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
import java.util.Iterator;
import java.util.List;

import org.easymock.IArgumentMatcher;

/**
 * @author OFFIS, Tammo Freese
 */
public class And implements IArgumentMatcher, Serializable {

    private static final long serialVersionUID = 3874580646798403818L;

    private final List<IArgumentMatcher> matchers;

    public And(final List<IArgumentMatcher> matchers) {
        this.matchers = matchers;
    }

    public boolean matches(final Object actual) {
        for (final IArgumentMatcher matcher : matchers) {
            if (!matcher.matches(actual)) {
                return false;
            }
        }
        return true;
    }

    public void appendTo(final StringBuffer buffer) {
        buffer.append("and(");
        for (final Iterator<IArgumentMatcher> it = matchers.iterator(); it.hasNext();) {
            it.next().appendTo(buffer);
            if (it.hasNext()) {
                buffer.append(", ");
            }
        }
        buffer.append(")");
    }
}
