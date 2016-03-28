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
package org.easymock.internal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Container for mock injections and test subject injection targets.
 * 
 * @author Alistair Todd
 * @since 3.3
 */
public class InjectionPlan {

    private final List<Field> testSubjectFields = new ArrayList<Field>(1);

    private final List<Injection> qualifiedInjections = new ArrayList<Injection>(5);

    private final List<Injection> unqualifiedInjections = new ArrayList<Injection>(5);

    private final Set<String> qualifiers = new HashSet<String>();

    /**
     * Add an {@link Injection} to this container. It will be managed according to the presence
     * of a fieldName qualifier, and attempting to add an Injection with a duplicate fieldName
     * qualifier will cause an error.
     * 
     * @param injection Injection to manage as part of this plan
     */
    public void addInjection(Injection injection) {

        String qualifier = injection.getAnnotation().fieldName();

        if (qualifier.length() != 0) {
            blockDuplicateQualifiers(qualifier);
            qualifiedInjections.add(injection);
        } else {
            unqualifiedInjections.add(injection);
        }
    }

    private void blockDuplicateQualifiers(String qualifier) {
        if (!qualifiers.add(qualifier)) {
            throw new RuntimeException(
                    String.format("At least two mocks have fieldName qualifier '%s'", qualifier));
        }
    }

    /**
     * Add a field that should be treated as a test subject injection target.
     * @param f Field representing a test subject to which injection of mocks will be attempted
     */
    public void addTestSubjectField(Field f) {
        testSubjectFields.add(f);
    }

    /**
     * Get fields identified as test subjects to which injection of mocks should be attempted.
     * @return fields representing test subjects
     */
    public List<Field> getTestSubjectFields() {
        return testSubjectFields;
    }

    /**
     * Get all injections having fieldName qualifiers.
     * @return list of Injections having fieldName qualifiers
     */
    public List<Injection> getQualifiedInjections() {
        return qualifiedInjections;
    }

    /**
     * Get all injections that do not have fieldName qualifiers.
     * @return list of Injections that do not have fieldName qualifiers.
     */
    public List<Injection> getUnqualifiedInjections() {
        return unqualifiedInjections;
    }

}
