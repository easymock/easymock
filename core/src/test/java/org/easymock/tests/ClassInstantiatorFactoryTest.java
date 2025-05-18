/*
 * Copyright 2001-2025 the original author or authors.
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

import org.easymock.internal.ClassInstantiatorFactory;
import org.easymock.internal.DefaultClassInstantiator;
import org.easymock.internal.IClassInstantiator;
import org.easymock.internal.ObjenesisClassInstantiator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Henri Tremblay
 */
class ClassInstantiatorFactoryTest {

    @AfterEach
    void tearDown() {
        // put back the default to prevent side effects on other tests
        ClassInstantiatorFactory.setDefaultInstantiator();
    }

    @Test
    void getInstantiator_Default() {
        IClassInstantiator instantiator = ClassInstantiatorFactory.getInstantiator();
        assertTrue(instantiator instanceof ObjenesisClassInstantiator);
    }

    @Test
    void getInstantiator_Overridden() {
        ClassInstantiatorFactory.setInstantiator(new DefaultClassInstantiator());
        IClassInstantiator instantiator = ClassInstantiatorFactory.getInstantiator();
        assertTrue(instantiator instanceof DefaultClassInstantiator);
    }

    @Test
    void getInstantiator_BackToDefault() {
        ClassInstantiatorFactory.setInstantiator(new DefaultClassInstantiator());
        ClassInstantiatorFactory.setDefaultInstantiator();
        IClassInstantiator instantiator = ClassInstantiatorFactory.getInstantiator();
        assertTrue(instantiator instanceof ObjenesisClassInstantiator);
    }

    @Test
    void getJVM() {
        assertEquals(System.getProperty("java.vm.vendor"), ClassInstantiatorFactory.getJVM());
    }
}
