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

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.IAnswer;
import org.easymock.tests.IMethods;
import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class AnswerTest {

    private IMethods mock;

    @Before
    public void setUp() {
        mock = createMock(IMethods.class);
    }

    @Test
    public void answer() {
        final IAnswer<Object> firstAnswer = new IAnswer<Object>() {
            public Object answer() {
                assertArrayEquals(new Object[] { 1, "2", "3" }, getCurrentArguments());
                return "Call answered";
            }
        };

        final IAnswer<Object> secondAnswer = new IAnswer<Object>() {
            public Object answer() {
                assertArrayEquals(new Object[] { 1, "2", "3" }, getCurrentArguments());
                throw new IllegalStateException("Call answered");
            }
        };

        expect(mock.threeArgumentMethod(1, "2", "3")).andAnswer(firstAnswer).andReturn("Second call")
                .andAnswer(secondAnswer).andReturn("Fourth call");

        replay(mock);

        assertEquals("Call answered", mock.threeArgumentMethod(1, "2", "3"));
        assertEquals("Second call", mock.threeArgumentMethod(1, "2", "3"));
        try {
            mock.threeArgumentMethod(1, "2", "3");
            fail();
        } catch (final IllegalStateException expected) {
            assertEquals("Call answered", expected.getMessage());
        }
        assertEquals("Fourth call", mock.threeArgumentMethod(1, "2", "3"));

        verify(mock);
    }

    @Test
    public void stubAnswer() {
        final IAnswer<Object> firstAnswer = new IAnswer<Object>() {
            public Object answer() {
                assertArrayEquals(new Object[] { 1, "2", "3" }, getCurrentArguments());
                return "Call answered";
            }
        };

        final IAnswer<Object> secondAnswer = new IAnswer<Object>() {
            public Object answer() {
                assertArrayEquals(new Object[] { 1, "2", "4" }, getCurrentArguments());
                return "Call answered";
            }
        };

        expect(mock.threeArgumentMethod(1, "2", "3")).andReturn(42).andStubAnswer(firstAnswer);
        expect(mock.threeArgumentMethod(1, "2", "4")).andStubAnswer(secondAnswer);

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
        } catch (final NullPointerException expected) {
            assertEquals("answer object must not be null", expected.getMessage());
        }
    }

    @Test
    public void nullStubAnswerNotAllowed() {
        try {
            expect(mock.threeArgumentMethod(1, "2", "3")).andStubAnswer(null);
            fail();
        } catch (final NullPointerException expected) {
            assertEquals("answer object must not be null", expected.getMessage());
        }
    }

    public static class A {
    }

    public static class B extends A {
    }

    public static interface C {
        A foo();
    }

    @Test
    public void testGenericityFlexibility() {

        final C c = createMock(C.class);
        final B b = new B();

        final IAnswer<B> answer = new IAnswer<B>() {

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
        final String[] array = new String[] { "a" };
        mock.arrayMethod(array);
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                final String[] s = (String[]) getCurrentArguments()[0];
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
