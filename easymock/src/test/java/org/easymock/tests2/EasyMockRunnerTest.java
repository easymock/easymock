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
package org.easymock.tests2;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.TestSubject;
import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.tests.BaseEasyMockRunnerTest;
import org.easymock.tests.IMethods;
import org.easymock.tests.IVarArgs;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

@RunWith(EasyMockRunner.class)
public class EasyMockRunnerTest extends BaseEasyMockRunnerTest {

    @Mock
    private IMethods standardMock;

    @Mock(type = MockType.NICE)
    private IMethods typedMock;

    @Mock(name = "name1")
    private IMethods namedMock;

    @Mock(name = "name2", type = MockType.NICE)
    private IMethods namedAndTypedMock;

    @Test
    public void testMocksWithSupport() {
        expect(standardMock.oneArg(true)).andReturn("1");
        expect(namedMock.oneArg(true)).andReturn("2");
        replayAll();
        assertNull(typedMock.oneArg("0"));
        assertNull(namedAndTypedMock.oneArg("0"));
        assertEquals("1", standardMock.oneArg(true));
        assertEquals("2", namedMock.oneArg(true));
        verifyAll();
        assertEquals("EasyMock for interface org.easymock.tests.IMethods", standardMock.toString());
        assertEquals("name1", namedMock.toString());
        assertEquals("EasyMock for interface org.easymock.tests.IMethods", typedMock.toString());
        assertEquals("name2", namedAndTypedMock.toString());
    }

    private static class ToInject {
        protected IMethods m1;
        protected IMethods m2;
        protected IVarArgs v;
        protected String a;
        protected final IVarArgs f = null;
        protected static IVarArgs s;
    }

    private static class ToInjectTest {
        @Mock
        protected IMethods m;
        @Mock
        protected IVarArgs v;
        @TestSubject
        protected ToInject toInject = new ToInject();
    }

    @Test
    public void testInjectMocks() {
        ToInjectTest test = new ToInjectTest();
        EasyMockSupport.injectMocks(test);
        assertSame(test.m, test.toInject.m1);
        assertSame(test.m, test.toInject.m2);
        assertSame(test.v, test.toInject.v);
        assertNull(test.toInject.a);
        assertNull(test.toInject.f);
        assertNull(ToInject.s);
    }

    private static class ToInjectDuplicateTest {
        @Mock(name="a")
        protected IMethods m;
        @Mock(name="b")
        protected IMethods v;
        @TestSubject
        protected ToInject toInject = new ToInject();
    }

    @Test
    public void testInjectDuplicate() {
        ToInjectDuplicateTest test = new ToInjectDuplicateTest();
        try {
            EasyMockSupport.injectMocks(test);
        }
        catch (RuntimeException e) {
            assertEquals("At least two mocks can be assigned to protected org.easymock.tests.IMethods org.easymock.tests2.EasyMockRunnerTest$ToInject.m1: a and b", e.getMessage());
        }
    }
}
