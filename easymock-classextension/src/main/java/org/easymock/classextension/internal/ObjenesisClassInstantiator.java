/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.classextension.internal;

import org.objenesis.ObjenesisHelper;

public class ObjenesisClassInstantiator implements IClassInstantiator {

    public Object newInstance(Class<?> clazz) throws InstantiationException {
        return ObjenesisHelper.newInstance(clazz);
    }

}
