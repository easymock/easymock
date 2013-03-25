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

/**
 * Factory returning a {@link IClassInstantiator}for the current JVM
 * 
 * @author Henri Tremblay
 */
public final class ClassInstantiatorFactory {

    private static IClassInstantiator instantiator = new ObjenesisClassInstantiator();

    // ///CLOVER:OFF
    private ClassInstantiatorFactory() {
    }

    // ///CLOVER:ON

    /**
     * Returns the current JVM as specified in the System properties
     * 
     * @return current JVM
     */
    public static String getJVM() {
        return System.getProperty("java.vm.vendor");
    }

    /**
     * Returns the current JVM specification version (1.5, 1.4, 1.3)
     * 
     * @return current JVM specification version
     */
    public static String getJVMSpecificationVersion() {
        return System.getProperty("java.specification.version");
    }

    public static boolean is1_3Specifications() {
        return getJVMSpecificationVersion().equals("1.3");
    }

    /**
     * Returns a class instantiator suitable for the current JVM
     * 
     * @return a class instantiator usable on the current JVM
     */
    public static IClassInstantiator getInstantiator() {
        return instantiator;
    }

    /**
     * Allow to override the default instantiator. Useful when the default one
     * isn't able to create mocks in a given environment.
     * 
     * @param i
     *            New instantiator
     */
    public static void setInstantiator(final IClassInstantiator i) {
        instantiator = i;
    }

    /**
     * Set back the default instantiator
     */
    public static void setDefaultInstantiator() {
        instantiator = new ObjenesisClassInstantiator();
    }
}
