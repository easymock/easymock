/**
 * Copyright 2001-2017 the original author or authors.
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
public class Null implements IArgumentMatcher, Serializable {

    private static final long serialVersionUID = 6077244839421122011L;

    public static final Null NULL = new Null();

    private Null() {
    }

    public boolean matches(Object actual) {
        return actual == null;
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("isNull()");
    }
}
