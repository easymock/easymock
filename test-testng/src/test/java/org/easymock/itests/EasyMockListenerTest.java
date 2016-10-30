package org.easymock.itests;

import org.easymock.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.easymock.EasyMock.expect;
import static org.testng.Assert.*;

@Listeners(EasyMockListener.class)
public class EasyMockListenerTest extends EasyMockSupport {

    @Mock
    private IMethods standardMock;

    @Mock(type = MockType.NICE)
    private IMethods typedMock;

    @Mock(name = "name1")
    private IMethods namedMock;

    @Mock(name = "name2", type = MockType.NICE)
    private IMethods namedAndTypedMock;

    @BeforeMethod
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
        assertEquals("EasyMock for interface org.easymock.itests.IMethods", standardMock.toString());
        assertEquals("name1", namedMock.toString());
        assertEquals("EasyMock for interface org.easymock.itests.IMethods", typedMock.toString());
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

    @Listeners(EasyMockListener.class)
    public static class ToInjectMocksTest {
        @Mock
        protected IMethods m;

        @Mock
        protected IVarArgs v;

        @TestSubject
        protected ToInject toInject = new ToInject();

        @Test
        public void shouldInjectMocksWhereTypeCompatible() {
            assertSame(m, toInject.m1);
            assertSame(m, toInject.m2);
            assertSame(v, toInject.v);
            assertNull(toInject.a);
            assertNull(toInject.f);
            assertNull(ToInject.s);
        }
    }
}
