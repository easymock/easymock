/*
 * Copyright 2001-2021 the original author or authors.
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

import java.lang.reflect.Field;

/**
 * Applies an {@link Injection} to a target field.
 * 
 * @author Henri Tremblay
 * @author Alistair Todd
 * @since 3.3
 */
public class InjectionTarget {

    private final Field targetField;

    /**
     * Create instance for injection to the given field.
     * @param f Field that will receive the {@link Injection}
     */
    public InjectionTarget(Field f) {
        this.targetField = f;
    }

    /**
     * Can the given Injection be applied to this InjectionTarget?
     * @param injection candidate Injection
     * @return true if injection represents a mock that can be applied to this InjectionTarget,
     * false if the mock is of a type that cannot be assigned
     */
    public boolean accepts(Injection injection) {
        return targetField.getType().isAssignableFrom(injection.getMock().getClass());
    }

    /**
     * Perform the injection against the given object set the "matched" status of the injection when successful.
     * @param obj Object instance on which to perform injection.
     * @param injection Injection containing mock to assign.
     */
    public void inject(Object obj, Injection injection) {

        targetField.setAccessible(true);

        try {
            targetField.set(obj, injection.getMock());
        } catch (IllegalAccessException e) {
            // ///CLOVER:OFF
            throw new RuntimeException(e);
            // ///CLOVER:ON
        }
        injection.setMatched();
    }

    /**
     * Get the field to which injections will be assigned.
     * @return target field for injection assignment.
     */
    public Field getTargetField() {
        return targetField;
    }

}
