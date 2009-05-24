/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.classextension.internal;

/**
 * Used to instantiate a given class.
 */
public interface IClassInstantiator {

    /**
     * Return a new instance of the specified class. The recommended way is
     * without calling any constructor. This is usually done by doing like
     * <code>ObjectInputStream.readObject()</code> which is JVM specific.
     * 
     * @param c
     *            Class to instantiate
     * @return new instance of clazz
     */
    Object newInstance(Class<?> clazz) throws InstantiationException;
}
