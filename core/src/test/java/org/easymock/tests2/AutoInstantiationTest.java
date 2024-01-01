/*
 * Copyright 2001-2024 the original author or authors.
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
package org.easymock.tests2;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.easymock.tests.IMethods;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Tests automated instantiations of {@link org.easymock.TestSubject}
 *
 * @author Tatsuya Iwanari
 */
@RunWith(EasyMockRunner.class)
public class AutoInstantiationTest {

    @TestSubject
    private String toTest;

    @Before
    public void before() {
        assertNotNull(toTest);
    }

    public static class ToInjectWithoutConstructors {
        private IMethods m;
    }

    private static class DefaultConstructorInjectionTest {
        @Mock
        private IMethods m;

        @TestSubject
        private ToInjectWithoutConstructors toInject;
    }

    @Test
    public void shouldInjectMocksToTestSubjectWithDefaultConstructor() {
        DefaultConstructorInjectionTest test = new DefaultConstructorInjectionTest();
        EasyMockSupport.injectMocks(test);
        assertSame(test.m, test.toInject.m);
    }

}
