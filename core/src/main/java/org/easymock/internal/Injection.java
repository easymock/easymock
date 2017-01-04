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
package org.easymock.internal;

import org.easymock.Mock;

/**
 * Described mock instance for injection.
 * 
 * @author Alistair Todd
 * @since 3.3
 */
public class Injection {

    private final Object mock;

    private final Mock annotation;

    private boolean matched = false;

    /**
     * Create instance containing the given mock and annotation.
     * @param mock a mock object instance
     * @param annotation Mock annotation describing the mock
     */
    public Injection(Object mock, Mock annotation) {
        this.mock = mock;
        this.annotation = annotation;
    }

    /**
     * Gets the mock instance for this injection.
     * 
     * @return a mock object instance
     */
    public Object getMock() {
        return mock;
    }

    /**
     * Gets the annotation describing this mock instance.
     * 
     * @return annotation describing the mock instance
     */
    public Mock getAnnotation() {
        return annotation;
    }

    /**
     * Get the field name qualifier for this injection.
     * @return the field name qualifier for this injection which may be empty string where not set.
     */
    public String getQualifier() {
        return annotation.fieldName();
    }

    /**
     * Change the status to indicate that this injection was matched to some target.
     */
    public void setMatched() {
        matched = true;
    }

    /**
     * Is this injection matched by some injection target?
     * @return true if setMatched was called, indicating that a matching injection target was found
     */
    public boolean isMatched() {
        return matched;
    }
}
