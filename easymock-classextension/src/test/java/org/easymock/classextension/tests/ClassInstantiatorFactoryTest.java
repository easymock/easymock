/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.classextension.tests;

import static org.junit.Assert.*;

import org.easymock.classextension.internal.ClassInstantiatorFactory;
import org.easymock.classextension.internal.DefaultClassInstantiator;
import org.easymock.classextension.internal.IClassInstantiator;
import org.easymock.classextension.internal.ObjenesisClassInstantiator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Henri Tremblay
 */
public class ClassInstantiatorFactoryTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getInstantiator_Default() {
        IClassInstantiator instantiator = ClassInstantiatorFactory
                .getInstantiator();
        assertTrue(instantiator instanceof ObjenesisClassInstantiator);
    }

    @Test
    public void getInstantiator_Overriden() {
        ClassInstantiatorFactory
                .setInstantiator(new DefaultClassInstantiator());
        IClassInstantiator instantiator = ClassInstantiatorFactory
                .getInstantiator();
        assertTrue(instantiator instanceof DefaultClassInstantiator);
    }

    @Test
    public void getInstantiator_BackToDefault() {
        ClassInstantiatorFactory
                .setInstantiator(new DefaultClassInstantiator());
        ClassInstantiatorFactory.setDefaultInstantiator();
        IClassInstantiator instantiator = ClassInstantiatorFactory
                .getInstantiator();
        assertTrue(instantiator instanceof ObjenesisClassInstantiator);
    }

    @Test
    public void getJVM() {
        assertEquals(System.getProperty("java.vm.vendor"),
                ClassInstantiatorFactory.getJVM());
    }
}
