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

    private Object toAssign = null;

    private boolean qualified = false;

    /**
     * Create instance for injection to the given field.
     * @param f Field that will receive the {@link Injection}
     */
    public InjectionTarget(final Field f) {
        this.targetField = f;
    }

    /**
     * Can the given Injection be applied to this InjectionTarget?
     * @param injection candidate Injection
     * @return true if injection is of a type that can b applied to this InjectionTarget
     */
    public boolean accepts(final Injection injection) {
        return targetField.getType().isAssignableFrom(injection.getMock().getClass());
    }

    /**
     * Perform the injection against the given object.
     * @param obj Object instance on which to perform injection.
     * @return true if injection succeeds, otherwise false.
     */
    public boolean inject(final Object obj) {

        if (toAssign == null) {
            return false;
        }

        targetField.setAccessible(true);

        try {
            targetField.set(obj, toAssign);
        } catch (final IllegalAccessException e) {
            // ///CLOVER:OFF
            throw new RuntimeException(e);
            // ///CLOVER:ON
        }

        return true;
    }

    /**
     * Set the Injection that will be applied to this target, if it is unqualified or if the qualifier
     * matches the target field name. Throws RuntimeException if the injection is already set, unless
     * the new injection has a qualifier matching the fieldname and the previous setting was unqualified.
     * @param injection injection to apply.
     */
    public void setInjection(final Injection injection) {

        final String qualifier = injection.getAnnotation().qualifier();

        if (qualifier.length() > 0) {
            if (qualifier.equals(targetField.getName())) {
                assignQualified(injection);
            }
            return;
        }

        assignUnqualified(injection);
    }

    private void assignQualified(final Injection injection) {
        if (qualified) {
            throw new RuntimeException(
                    "At least two mocks with qualifier '" + injection.getAnnotation().qualifier()
                            + "' can be assigned to " + targetField + ": "
                            + toAssign + " and " + injection.getMock());
        }
        toAssign = injection.getMock();
        qualified = true;
    }

    private void assignUnqualified(final Injection injection) {
        if (toAssign != null) {
            throw new RuntimeException("At least two mocks can be assigned to " + targetField + ": "
                    + toAssign + " and " + injection.getMock());
        }

        toAssign = injection.getMock();
    }

}
