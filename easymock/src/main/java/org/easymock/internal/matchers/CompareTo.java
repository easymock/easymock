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
 * @param <T>
 *            Type of the values compared
 * 
 * @author Henri Tremblay
 */
public abstract class CompareTo<T extends Comparable<T>> implements IArgumentMatcher, Serializable {

    private static final long serialVersionUID = -8447010713532143168L;

    private final Comparable<T> expected;

    public CompareTo(final Comparable<T> value) {
        this.expected = value;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public boolean matches(final Object actual) {

        if (!(actual instanceof Comparable)) {
            return false;
        }

        return matchResult(((Comparable) actual).compareTo(expected));
    }

    public void appendTo(final StringBuffer buffer) {
        buffer.append(getName() + "(" + expected + ")");
    }

    protected abstract String getName();

    protected abstract boolean matchResult(int result);
}
