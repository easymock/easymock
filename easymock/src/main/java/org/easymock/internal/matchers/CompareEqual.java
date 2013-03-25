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

/**
 * @param <T>
 *            Type of the values compared
 * 
 * @author Henri Tremblay
 */
public class CompareEqual<T extends Comparable<T>> extends CompareTo<T> {

    private static final long serialVersionUID = 7616033998227799268L;

    public CompareEqual(final Comparable<T> value) {
        super(value);
    }

    @Override
    protected String getName() {
        return "cmpEq";
    }

    @Override
    protected boolean matchResult(final int result) {
        return result == 0;
    }
}
