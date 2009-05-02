/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests2;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.IAnswer;
import org.easymock.tests.IMethods;
import org.junit.Before;
import org.junit.Test;

public class AnswerTest {

    private IMethods mock;

    @Before
    public void setUp() {
        mock = createMock(IMethods.class);
    }

    @Test
    public void answer() {
        IAnswer<Object> firstAnswer = new IAnswer<Object>() {
            public Object answer() {
                assertArrayEquals(new Object[] { 1, "2", "3" },
                        getCurrentArguments());
                return "Call answered";
            }
        };

        IAnswer<Object> secondAnswer = new IAnswer<Object>() {
            public Object answer() {
                assertArrayEquals(new Object[] { 1, "2", "3" },
                        getCurrentArguments());
                throw new IllegalStateException("Call answered");
            }
        };

        expect(mock.threeArgumentMethod(1, "2", "3")).andAnswer(firstAnswer)
                .andReturn("Second call").andAnswer(secondAnswer).andReturn(
                        "Fourth call");

        replay(mock);

        assertEquals("Call answered", mock.threeArgumentMethod(1, "2", "3"));
        assertEquals("Second call", mock.threeArgumentMethod(1, "2", "3"));
        try {
            mock.threeArgumentMethod(1, "2", "3");
            fail();
        } catch (IllegalStateException expected) {
            assertEquals("Call answered", expected.getMessage());
        }
        assertEquals("Fourth call", mock.threeArgumentMethod(1, "2", "3"));

        verify(mock);
    }

    @Test
    public void stubAnswer() {
        IAnswer<Object> firstAnswer = new IAnswer<Object>() {
            public Object answer() {
                assertArrayEquals(new Object[] { 1, "2", "3" },
                        getCurrentArguments());
                return "Call answered";
            }
        };

        IAnswer<Object> secondAnswer = new IAnswer<Object>() {
            public Object answer() {
                assertArrayEquals(new Object[] { 1, "2", "4" },
                        getCurrentArguments());
                return "Call answered";
            }
        };

        expect(mock.threeArgumentMethod(1, "2", "3")).andReturn(42)
                .andStubAnswer(firstAnswer);
        expect(mock.threeArgumentMethod(1, "2", "4")).andStubAnswer(
                secondAnswer);

        replay(mock);

        assertEquals(42, mock.threeArgumentMethod(1, "2", "3"));
        assertEquals("Call answered", mock.threeArgumentMethod(1, "2", "3"));
        assertEquals("Call answered", mock.threeArgumentMethod(1, "2", "4"));
        assertEquals("Call answered", mock.threeArgumentMethod(1, "2", "3"));
        assertEquals("Call answered", mock.threeArgumentMethod(1, "2", "3"));

        verify(mock);
    }

    @Test
    public void nullAnswerNotAllowed() {
        try {
            expect(mock.threeArgumentMethod(1, "2", "3")).andAnswer(null);
            fail();
        } catch (NullPointerException expected) {
            assertEquals("answer object must not be null", expected
                    .getMessage());
        }
    }

    @Test
    public void nullStubAnswerNotAllowed() {
        try {
            expect(mock.threeArgumentMethod(1, "2", "3")).andStubAnswer(null);
            fail();
        } catch (NullPointerException expected) {
            assertEquals("answer object must not be null", expected
                    .getMessage());
        }
    }

    public static class A {}
    public static class B extends A{}
    
    public static interface C {
        A foo();
    }
    
    @Test
    public void testGenericityFlexibility() {
        
        final C c = createMock(C.class);
        final B b = new B();
        
        IAnswer<B> answer = new IAnswer<B>() {

            public B answer() throws Throwable {
                return b;
            }
            
        };

        expect(c.foo()).andAnswer(answer);
        expect(c.foo()).andStubAnswer(answer);
        
        replay(c);
        assertSame(b, c.foo());
        assertSame(b, c.foo());
        verify(c);
    }
    
    @Test
    public void answerOnVoidMethod() {
        String[] array = new String[] { "a" };
        mock.arrayMethod(array);
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                String[] s = (String[]) getCurrentArguments()[0];
                s[0] = "b";
                return null;
            }
        });
        replay(mock);
        mock.arrayMethod(array);
        verify(mock);
        
        assertEquals("b", array[0]);
    }
}
