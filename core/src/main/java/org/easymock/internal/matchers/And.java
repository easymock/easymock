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
import java.util.Iterator;
import java.util.List;

import org.easymock.IArgumentMatcher;

/**
 * Matches if all given argument matchers match.
 *
 * @author OFFIS, Tammo Freese
 */
public class And implements IArgumentMatcher, Serializable {

    private static final long serialVersionUID = 3874580646798403818L;

    private final List<IArgumentMatcher> matchers;

    /**
     * Creates a new And matcher from a list of matchers that should match altogether.
     *
     * @param matchers the matchers to match altogether
     */
    public And(List<IArgumentMatcher> matchers) {
        this.matchers = matchers;
    }

    @Override
    public boolean matches(Object actual) {
        for (IArgumentMatcher matcher : matchers) {
            if (!matcher.matches(actual)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void appendTo(StringBuffer buffer) {
        buffer.append("and(");
        for (Iterator<IArgumentMatcher> it = matchers.iterator(); it.hasNext();) {
            it.next().appendTo(buffer);
            if (it.hasNext()) {
                buffer.append(", ");
            }
        }
        buffer.append(")");
    }
}
