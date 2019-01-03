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

import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
import org.easymock.internal.MocksControl;
import org.easymock.tests.IMethods;
import org.easymock.tests.IVarArgs;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * Tests annotation-driven mocking, requiring to be executed with either
 * EasyMockRunner or EasyMockRule.
 *
 * @author Henri Tremblay
 * @author Alistair Todd
 */
public abstract class EasyMockAnnotationsTest extends EasyMockSupport {

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

    @Test
    public void shouldCreateMocksUsingTestClassWhenExtendsEasyMockSupport() {
        expect(standardMock.oneArg(true)).andReturn("1");
        expect(namedMock.oneArg(true)).andReturn("2");
        replayAll(); // Relies on this test class having been used for createMock calls.
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

    private static class ToInjectMocksTest {
        @Mock
        protected IMethods m;

        @Mock
        protected IVarArgs v;

        @TestSubject
        protected ToInject toInject = new ToInject();
    }

    @Test
    public void shouldInjectMocksWhereTypeCompatible() {
        ToInjectMocksTest test = new ToInjectMocksTest();
        EasyMockSupport.injectMocks(test);
        assertSame(test.m, test.toInject.m1);
        assertSame(test.m, test.toInject.m2);
        assertSame(test.v, test.toInject.v);
        assertNull(test.toInject.a);
        assertNull(test.toInject.f);
        assertNull(ToInject.s);
    }

    private static class ToInjectDuplicateMocksTest {
        @Mock(name = "a")
        protected IMethods m;

        @Mock(name = "b")
        protected IMethods v;

        @TestSubject
        protected ToInject toInject = new ToInject();
    }

    @Test
    public void shouldErrorWhenDuplicateAssignmentPossible() {

        try {
            EasyMockSupport.injectMocks(new ToInjectDuplicateMocksTest());
        } catch (AssertionError e) {
            assertEquals(
                    "At least two mocks can be assigned to 'protected org.easymock.tests.IMethods org.easymock.tests2.EasyMockAnnotationsTest$ToInject.m1': a and b",
                    e.getMessage());
            return;
        }
        fail("Expected an exception for at least two mocks can be assigned");
    }

    private static class ToInjectQualifiedMocksTest {
        @Mock(name = "a", fieldName = "m1")
        protected IMethods a;

        @Mock(name = "b", fieldName = "m2")
        protected IMethods b;

        @TestSubject
        protected ToInject toInject = new ToInject();
    }

    @Test
    public void shouldInjectQualifiedMocksToNamedFields() {
        ToInjectQualifiedMocksTest test = new ToInjectQualifiedMocksTest();
        EasyMockSupport.injectMocks(test);
        assertSame(test.a, test.toInject.m1);
        assertSame(test.b, test.toInject.m2);
        assertNull(test.toInject.v);
    }

    private static class ToInjectExtension extends ToInject {
        // Expecting assignments in superclass
    }

    private static class ToInjectQualifiedMocksToSuperClassTest {

        @Mock(name = "a", fieldName = "m1")
        protected IMethods a;

        @Mock(name = "b", fieldName = "m2")
        protected IMethods b;

        @TestSubject
        protected ToInjectExtension toInject = new ToInjectExtension();
    }

    @Test
    public void shouldInjectQualifiedMocksToTestSubjectSuperClass() {
        ToInjectQualifiedMocksToSuperClassTest test = new ToInjectQualifiedMocksToSuperClassTest();
        EasyMockSupport.injectMocks(test);
        assertSame(test.a, test.toInject.m1);
        assertSame(test.b, test.toInject.m2);
        assertNull(test.toInject.v);
    }

    private static class ToInjectQualifiedMocksMultipleTestSubjectsTest {
        @Mock(name = "a", fieldName = "m1")
        protected IMethods a;

        @Mock(name = "b", fieldName = "m2")
        protected IMethods b;

        @TestSubject
        protected ToInject toInject = new ToInject();

        @TestSubject
        protected ToInject toInject2 = new ToInject();
    }

    @Test
    public void shouldInjectQualifiedMocksToAllMatchingTestSubjects() {
        ToInjectQualifiedMocksMultipleTestSubjectsTest test = new ToInjectQualifiedMocksMultipleTestSubjectsTest();
        EasyMockSupport.injectMocks(test);
        assertSame(test.a, test.toInject.m1);
        assertSame(test.b, test.toInject.m2);
        assertNull(test.toInject.v);
        assertSame(test.a, test.toInject2.m1);
        assertSame(test.b, test.toInject2.m2);
        assertNull(test.toInject2.v);
    }

    private static class ToInjectUnsatisfiedQualifierTest {
        @Mock(name = "a", fieldName = "m1")
        protected IMethods a;

        @Mock(name = "b", fieldName = "m2")
        protected IMethods b;

        @Mock(fieldName = "unmatched")
        protected IVarArgs v;

        @TestSubject
        protected ToInject toInject = new ToInject();
    }

    @Test
    public void shouldErrorWhenUnsatisfiedQualifier() {
        try {
            EasyMockSupport.injectMocks(new ToInjectUnsatisfiedQualifierTest());
        } catch (AssertionError e) {
            assertEquals("Unsatisfied qualifier: 'unmatched'",
                    e.getMessage());
            return;
        }
        fail("Expected an exception for unsatisfied fieldName qualifier");
    }

    private static class ToInjectTypeIncompatibleQualifierTest {
        @Mock(name = "a", fieldName = "m1")
        protected IMethods a;

        @Mock(fieldName = "m2")
        protected IVarArgs v;

        @TestSubject
        protected ToInject toInject = new ToInject();
    }

    @Test
    public void shouldErrorForUnmatchedQualifierWhenTypeIncompatibleQualifier() {
        try {
            EasyMockSupport.injectMocks(new ToInjectTypeIncompatibleQualifierTest());
        } catch (AssertionError e) {
            assertEquals("Unsatisfied qualifier: 'm2'",
                    e.getMessage());
            return;
        }
        fail("Expected an exception for unsatisfied fieldName qualifier");
    }

    private static class ToInjectUnassignableField extends ToInject {
        final IMethods finalField = null;

        static IMethods staticField;
    }

    private static class ToInjectUnassignableFinalFieldQualifierTest {
        @Mock(name = "a", fieldName = "finalField")
        protected IMethods a;

        @TestSubject
        protected ToInjectUnassignableField toInject = new ToInjectUnassignableField();
    }

    @Test
    public void shouldErrorForUnmatchedQualifierWhenUnassignableFinalField() {
        try {
            EasyMockSupport.injectMocks(new ToInjectUnassignableFinalFieldQualifierTest());
        } catch (AssertionError e) {
            assertEquals("Unsatisfied qualifier: 'finalField'",
                    e.getMessage());
            return;
        }
        fail("Expected an exception for unsatisfied fieldName qualifier");
    }

    private static class ToInjectUnassignableStaticFieldQualifierTest {
        @Mock(name = "a", fieldName = "staticField")
        protected IMethods a;

        @TestSubject
        protected ToInjectUnassignableField toInject = new ToInjectUnassignableField();
    }

    @Test
    public void shouldErrorForUnmatchedQualifierWhenUnassignableStaticField() {
        try {
            EasyMockSupport.injectMocks(new ToInjectUnassignableStaticFieldQualifierTest());
        } catch (AssertionError e) {
            assertEquals("Unsatisfied qualifier: 'staticField'",
                    e.getMessage());
            return;
        }
        fail("Expected an exception for unsatisfied fieldName qualifier");
    }

    private static class ToInjectDuplicateQualifierTest {
        @Mock(name = "a", fieldName = "m1")
        protected IMethods a;

        @Mock(name = "b", fieldName = "m1")
        protected IMethods b;

        @TestSubject
        protected ToInject toInject = new ToInject();
    }

    @Test
    public void shouldErrorWhenDuplicateQualifiers() {
        try {
            EasyMockSupport.injectMocks(new ToInjectDuplicateQualifierTest());
            fail("Expected an exception for duplicate fieldName qualifier");
        } catch (AssertionError e) {
            assertEquals("At least two mocks have fieldName qualifier 'm1'",
                    e.getMessage());
        }
    }

    private static class ToInjectOneTarget {
        protected IMethods m1;
    }

    private static class ToInjectQualifiedAndUnqualifiedTest {
        @Mock(name = "a")
        protected IMethods a;

        @Mock(name = "b", fieldName = "m1")
        protected IMethods b;

        @TestSubject
        protected ToInjectOneTarget toInjectOneTarget = new ToInjectOneTarget();
    }

    @Test
    public void shouldNotAssignUnqualifiedMockWhereQualifiedMockAssigned() {
        ToInjectQualifiedAndUnqualifiedTest test = new ToInjectQualifiedAndUnqualifiedTest();
        EasyMockSupport.injectMocks(test);
        assertSame(test.b, test.toInjectOneTarget.m1);
    }

    private static class TypeDefinedTwiceTest {
        @Mock(value=MockType.STRICT, type=MockType.STRICT)
        protected IMethods a;
    }

    @Test
    public void shouldNotDefineValueAndTypeAtTheSameTime() {
        TypeDefinedTwiceTest test = new TypeDefinedTwiceTest();
        try {
            EasyMockSupport.injectMocks(test);
            fail("Should not accept the redefinition");
        } catch(AssertionError e) {
            assertEquals("@Mock.value() and @Mock.type() are aliases, you can't specify both at the same time", e.getMessage());
        }
    }

    private static class TypeDefinedUsingValue {
        @Mock(MockType.STRICT)
        private IMethods standardMock;

        @TestSubject
        protected ToInjectOneTarget toInjectOneTarget = new ToInjectOneTarget();
    }

    @Test
    public void canUseValueToDefineType() {
        TypeDefinedUsingValue test = new TypeDefinedUsingValue();
        EasyMockSupport.injectMocks(test);
        assertSame(test.standardMock, test.toInjectOneTarget.m1);
        assertEquals(MocksControl.getControl(test.standardMock).getType(), MockType.STRICT);
    }
}
