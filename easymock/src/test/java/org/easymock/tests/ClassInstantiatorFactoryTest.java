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
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.internal.ClassInstantiatorFactory;
import org.easymock.internal.DefaultClassInstantiator;
import org.easymock.internal.IClassInstantiator;
import org.easymock.internal.ObjenesisClassInstantiator;
import org.junit.After;
import org.junit.Test;

/**
 * @author Henri Tremblay
 */
public class ClassInstantiatorFactoryTest {

    @After
    public void tearDown() throws Exception {
        // put back the default to prevent side effects on other tests
        ClassInstantiatorFactory.setDefaultInstantiator();
    }

    @Test
    public void getInstantiator_Default() {
        final IClassInstantiator instantiator = ClassInstantiatorFactory.getInstantiator();
        assertTrue(instantiator instanceof ObjenesisClassInstantiator);
    }

    @Test
    public void getInstantiator_Overriden() {
        ClassInstantiatorFactory.setInstantiator(new DefaultClassInstantiator());
        final IClassInstantiator instantiator = ClassInstantiatorFactory.getInstantiator();
        assertTrue(instantiator instanceof DefaultClassInstantiator);
    }

    @Test
    public void getInstantiator_BackToDefault() {
        ClassInstantiatorFactory.setInstantiator(new DefaultClassInstantiator());
        ClassInstantiatorFactory.setDefaultInstantiator();
        final IClassInstantiator instantiator = ClassInstantiatorFactory.getInstantiator();
        assertTrue(instantiator instanceof ObjenesisClassInstantiator);
    }

    @Test
    public void getJVM() {
        assertEquals(System.getProperty("java.vm.vendor"), ClassInstantiatorFactory.getJVM());
    }
}
