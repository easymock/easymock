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

/**
 * @author OFFIS, Tammo Freese
 */
public class Not implements IArgumentMatcher, Serializable {

    private static final long serialVersionUID = -5160559075998939348L;

    private final IArgumentMatcher first;

    public Not(final IArgumentMatcher first) {
        this.first = first;
    }

    public boolean matches(final Object actual) {
        return !first.matches(actual);
    }

    public void appendTo(final StringBuffer buffer) {
        buffer.append("not(");
        first.appendTo(buffer);
        buffer.append(")");
    }
}
