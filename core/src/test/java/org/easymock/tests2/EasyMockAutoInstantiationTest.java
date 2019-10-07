/*
 * Copyright 2001-2019 the original author or authors.
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

import org.easymock.*;
import org.easymock.tests.IMethods;
import org.easymock.tests.IVarArgs;
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
public class EasyMockAutoInstantiationTest extends EasyMockSupport {

    @Mock
    private IMethods standardMock;

    @Mock(type = MockType.NICE)
    private IMethods typedMock;

    @Mock(name = "name1")
    private IMethods namedMock;

    @Mock(name = "name2", type = MockType.NICE)
    private IMethods namedAndTypedMock;

    @Before
    public void setup() {
        assertNotNull(standardMock);
        assertNotNull(typedMock);
        assertNotNull(namedMock);
        assertNotNull(namedAndTypedMock);
    }

    public static class ToInjectWithoutConstructors {
        protected IMethods m1;

        protected IMethods m2;

        protected IVarArgs v;

        protected String a;

        protected final IVarArgs f = null;

        protected static IVarArgs s;
    }


    private static class DefaultConstructorInjectionTest {
        @Mock
        protected IMethods m;

        @Mock
        protected IVarArgs v;

        @TestSubject
        protected ToInjectWithoutConstructors toInject;
    }

    @Test
    public void shouldInjectMocksToTestSubjectWithDefaultConstructor() {
        DefaultConstructorInjectionTest test = new DefaultConstructorInjectionTest();
        EasyMockSupport.injectMocks(test);
        assertSame(test.m, test.toInject.m1);
        assertSame(test.m, test.toInject.m2);
        assertSame(test.v, test.toInject.v);
        assertNull(test.toInject.a);
        assertNull(test.toInject.f);
        assertNull(ToInjectWithoutConstructors.s);
    }

    public static class ToInjectWithDefinedDefaultConstructor {
        protected IMethods m1;

        protected IMethods m2;

        protected IVarArgs v;

        protected String a;

        protected final IVarArgs f = null;

        protected static IVarArgs s;

        ToInjectWithDefinedDefaultConstructor() {}
    }

    private static class DefinedDefaultConstructorInjectionTest {
        @Mock
        protected IMethods m;

        @Mock
        protected IVarArgs v;

        @TestSubject
        protected ToInjectWithDefinedDefaultConstructor toInject;
    }

    @Test
    public void shouldInjectMocksToTestTargetWithDefinedDefaultConstructor() {
        DefinedDefaultConstructorInjectionTest test = new DefinedDefaultConstructorInjectionTest();
        EasyMockSupport.injectMocks(test);
        assertSame(test.m, test.toInject.m1);
        assertSame(test.m, test.toInject.m2);
        assertSame(test.v, test.toInject.v);
        assertNull(test.toInject.a);
        assertNull(test.toInject.f);
        assertNull(ToInjectWithDefinedDefaultConstructor.s);
    }
}
