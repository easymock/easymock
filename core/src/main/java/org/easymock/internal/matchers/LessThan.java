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

/**
 * Match if the argument is less than the given value.
 *
 * @param <T>
 *            Type of the values compared
 *
 * @author OFFIS, Tammo Freese
 */
public class LessThan<T extends Comparable<T>> extends CompareTo<T> {

    private static final long serialVersionUID = 6004888476822043880L;

    public LessThan(Comparable<T> value) {
        super(value);
    }

    @Override
    protected String getName() {
        return "lt";
    }

    @Override
    protected boolean matchResult(int result) {
        return result < 0;
    }
}
