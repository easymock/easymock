/**
 * Copyright 2001-2016 the original author or authors.
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

import org.easymock.IArgumentMatcher;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * @author OFFIS, Tammo Freese
 */
public class Find implements IArgumentMatcher, Serializable {

    private static final long serialVersionUID = -7104607303959381785L;

    private final Pattern regex;

    public Find(String regex) {
        this.regex = Pattern.compile(regex);
    }

    public boolean matches(Object actual) {
        return (actual instanceof String) && regex.matcher((String) actual).find();
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("find(\"" + regex.pattern().replaceAll("\\\\", "\\\\\\\\") + "\")");
    }
}
