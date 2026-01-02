/*
 * Copyright 2001-2026 the original author or authors.
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
 * Matches any argument.
 *
 * @author OFFIS, Tammo Freese
 */
public final class Any implements IArgumentMatcher, Serializable {

    private static final long serialVersionUID = -3743894206806704049L;

    public static final Any ANY = new Any();

    private Any() {

    }

    @Override
    public boolean matches(Object actual) {
        return true;
    }

    @Override
    public void appendTo(StringBuffer buffer) {
        buffer.append("<any>");
    }
}
