/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.classextension.internal;

/**
 * Factory returning a {@link IClassInstantiator}for the current JVM
 */
public class ClassInstantiatorFactory {

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
    public static void setInstantiator(IClassInstantiator i) {
        instantiator = i;
    }

    /**
     * Set back the default instantiator
     */
    public static void setDefaultInstantiator() {
        instantiator = new ObjenesisClassInstantiator();
    }
}
