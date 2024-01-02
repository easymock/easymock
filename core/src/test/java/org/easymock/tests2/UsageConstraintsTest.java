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

import org.easymock.EasyMock;
import org.easymock.LogicalOperator;
import org.easymock.internal.matchers.Equals;
import org.easymock.tests.IMethods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.easymock.EasyMock.*;

/**
 * @author OFFIS, Tammo Freese
 */
public class UsageConstraintsTest {
    private IMethods mock;

    @BeforeEach
    public void setUp() {
        mock = createMock(IMethods.class);
    }

    @Test
    public void equalsMissing() {
        mock.simpleMethodWithArgument(not(eq("asd")));
        try {
            mock.simpleMethodWithArgument(not("jkl"));
            Assertions.fail();
        } catch (IllegalStateException e) {
            Assertions.assertEquals("no matchers found.", e.getMessage());
        }
        try {
            mock.simpleMethodWithArgument(or(eq("jkl"), "asd"));
            Assertions.fail();
        } catch (IllegalStateException e) {
            Assertions.assertEquals("2 matchers expected, 1 recorded.", e.getMessage());
        }
        try {
            mock.threeArgumentMethod(1, "asd", eq("asd"));
            Assertions.fail();
        } catch (IllegalStateException e) {
            Assertions.assertEquals("3 matchers expected, 1 recorded.\n"
                    + "This exception usually occurs when matchers are mixed with raw values when recording a method:\n"
                    + "\tfoo(5, eq(6));\t// wrong\n"
                    + "You need to use no matcher at all or a matcher for every single param:\n"
                    + "\tfoo(eq(5), eq(6));\t// right\n" + "\tfoo(5, 6);\t// also right", e.getMessage());
        }

    }

    @Test
    public void differentConstraintsOnSameCall() {
        mock.simpleMethodWithArgument(isNull());
        mock.simpleMethodWithArgument(notNull());
        replay(mock);
        mock.simpleMethodWithArgument(null);
        mock.simpleMethodWithArgument("2");
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void equals() {
        Assertions.assertEquals(new Equals(null), new Equals(null));
        Assertions.assertEquals(new Equals(2), new Equals(2));
        Assertions.assertNotEquals(null, new Equals(null));
        Assertions.assertNotEquals("Test", new Equals(null));
        try {
            new Equals(null).hashCode();
            Assertions.fail();
        } catch (UnsupportedOperationException expected) {
        }
    }

    @Test
    public void constraints() {
        expect(mock.threeArgumentMethod(and(geq(7), leq(10)), isA(String.class), contains("123"))).andReturn(
                "456").atLeastOnce();
        replay(mock);
        boolean failed = false;
        try {
            mock.threeArgumentMethod(11, "", "01234");
        } catch (AssertionError expected) {
            failed = true;
        }
        if (!failed) {
            Assertions.fail();
        }
        failed = false;
        try {
            mock.threeArgumentMethod(8, new Object(), "01234");
        } catch (AssertionError expected) {
            failed = true;
        }
        if (!failed) {
            Assertions.fail();
        }
        failed = false;
        try {
            mock.threeArgumentMethod(8, "", "no match");
        } catch (AssertionError expected) {
            failed = true;
        }
        if (!failed) {
            Assertions.fail();
        }
        Assertions.assertEquals("456", mock.threeArgumentMethod(8, "", "01234"));
        verifyRecording(mock);
    }

    @Test
    public void andOverloaded() {
        expect(mock.oneArg(and(eq(false), eq(false)))).andReturn("0");
        expect(mock.oneArg(and(eq((byte) 1), eq((byte) 1)))).andReturn("1");
        expect(mock.oneArg(and(eq('a'), eq('a')))).andReturn("2");
        expect(mock.oneArg(and(eq((double) 1), eq((double) 1)))).andReturn("3");
        expect(mock.oneArg(and(eq((float) 1), eq((float) 1)))).andReturn("4");
        expect(mock.oneArg(and(eq(1), eq(1)))).andReturn("5");
        expect(mock.oneArg(and(eq((long) 1), eq((long) 1)))).andReturn("6");
        expect(mock.oneArg(and(eq((short) 1), eq((short) 1)))).andReturn("7");
        expect(mock.oneArg(and(contains("a"), contains("d")))).andReturn("8");
        expect(mock.oneArg(and(isA(Class.class), eq(Object.class)))).andReturn("9");
        replay(mock);
        Assertions.assertEquals("9", mock.oneArg(Object.class));
        Assertions.assertEquals("0", mock.oneArg(false));
        Assertions.assertEquals("1", mock.oneArg((byte) 1));
        Assertions.assertEquals("2", mock.oneArg('a'));
        Assertions.assertEquals("3", mock.oneArg((double) 1));
        Assertions.assertEquals("7", mock.oneArg((short) 1));
        Assertions.assertEquals("8", mock.oneArg("abcde"));
        Assertions.assertEquals("4", mock.oneArg((float) 1));
        Assertions.assertEquals("5", mock.oneArg(1));
        Assertions.assertEquals("6", mock.oneArg((long) 1));
        verify(mock);
    }

    @Test
    public void orOverloaded() {
        expect(mock.oneArg(or(eq(false), eq(true)))).andReturn("0");
        expect(mock.oneArg(or(eq((byte) 1), eq((byte) 2)))).andReturn("1");
        expect(mock.oneArg(or(eq((char) 1), eq((char) 2)))).andReturn("2");
        expect(mock.oneArg(or(eq((double) 1), eq((double) 2)))).andReturn("3");
        expect(mock.oneArg(or(eq((float) 1), eq((float) 2)))).andReturn("4");
        expect(mock.oneArg(or(eq(1), eq(2)))).andReturn("5");
        expect(mock.oneArg(or(eq((long) 1), eq((long) 2)))).andReturn("6");
        expect(mock.oneArg(or(eq((short) 1), eq((short) 2)))).andReturn("7");
        expect(mock.oneArg(or(eq("asd"), eq("jkl")))).andReturn("8");
        expect(mock.oneArg(or(eq(this.getClass()), eq(Object.class)))).andReturn("9");
        replay(mock);
        Assertions.assertEquals("9", mock.oneArg(Object.class));
        Assertions.assertEquals("0", mock.oneArg(true));
        Assertions.assertEquals("1", mock.oneArg((byte) 2));
        Assertions.assertEquals("2", mock.oneArg((char) 1));
        Assertions.assertEquals("3", mock.oneArg((double) 2));
        Assertions.assertEquals("7", mock.oneArg((short) 1));
        Assertions.assertEquals("8", mock.oneArg("jkl"));
        Assertions.assertEquals("4", mock.oneArg((float) 1));
        Assertions.assertEquals("5", mock.oneArg(2));
        Assertions.assertEquals("6", mock.oneArg((long) 1));
        verify(mock);
    }

    @Test
    public void notOverloaded() {
        expect(mock.oneArg(not(eq(false)))).andReturn("0");
        expect(mock.oneArg(not(eq((byte) 1)))).andReturn("1");
        expect(mock.oneArg(not(eq('a')))).andReturn("2");
        expect(mock.oneArg(not(eq((double) 1)))).andReturn("3");
        expect(mock.oneArg(not(eq((float) 1)))).andReturn("4");
        expect(mock.oneArg(not(eq(1)))).andReturn("5");
        expect(mock.oneArg(not(eq((long) 1)))).andReturn("6");
        expect(mock.oneArg(not(eq((short) 1)))).andReturn("7");
        expect(mock.oneArg(not(contains("a")))).andReturn("8");
        expect(mock.oneArg(not(isA(Class.class)))).andReturn("9");
        replay(mock);
        Assertions.assertEquals("9", mock.oneArg(new Object()));
        Assertions.assertEquals("0", mock.oneArg(true));
        Assertions.assertEquals("1", mock.oneArg((byte) 2));
        Assertions.assertEquals("2", mock.oneArg('b'));
        Assertions.assertEquals("3", mock.oneArg((double) 2));
        Assertions.assertEquals("7", mock.oneArg((short) 2));
        Assertions.assertEquals("8", mock.oneArg("bcde"));
        Assertions.assertEquals("4", mock.oneArg((float) 2));
        Assertions.assertEquals("5", mock.oneArg(2));
        Assertions.assertEquals("6", mock.oneArg((long) 2));
        verify(mock);
    }

    @Test
    public void lessOrEqualOverloaded() {
        expect(mock.oneArg(leq((byte) 1))).andReturn("1");
        expect(mock.oneArg(leq((double) 1))).andReturn("3");
        expect(mock.oneArg(leq((float) 1))).andReturn("4");
        expect(mock.oneArg(leq(1))).andReturn("5");
        expect(mock.oneArg(leq((long) 1))).andReturn("6");
        expect(mock.oneArg(leq((short) 1))).andReturn("7");
        expect(mock.oneArg(leq(new BigDecimal("1")))).andReturn("8");
        replay(mock);
        Assertions.assertEquals("1", mock.oneArg((byte) 1));
        Assertions.assertEquals("3", mock.oneArg((double) 1));
        Assertions.assertEquals("7", mock.oneArg((short) 0));
        Assertions.assertEquals("4", mock.oneArg((float) -5));
        Assertions.assertEquals("5", mock.oneArg(-2));
        Assertions.assertEquals("6", mock.oneArg((long) -3));
        Assertions.assertEquals("8", mock.oneArg(new BigDecimal("0.5")));
        verify(mock);
    }

    @Test
    public void lessThanOverloaded() {
        expect(mock.oneArg(lt((byte) 1))).andReturn("1");
        expect(mock.oneArg(lt((double) 1))).andReturn("3");
        expect(mock.oneArg(lt((float) 1))).andReturn("4");
        expect(mock.oneArg(lt(1))).andReturn("5");
        expect(mock.oneArg(lt((long) 1))).andReturn("6");
        expect(mock.oneArg(lt((short) 1))).andReturn("7");
        expect(mock.oneArg(lt(new BigDecimal("1")))).andReturn("8");
        replay(mock);
        Assertions.assertEquals("1", mock.oneArg((byte) 0));
        Assertions.assertEquals("3", mock.oneArg((double) 0));
        Assertions.assertEquals("7", mock.oneArg((short) 0));
        Assertions.assertEquals("4", mock.oneArg((float) -4));
        Assertions.assertEquals("5", mock.oneArg(-34));
        Assertions.assertEquals("6", mock.oneArg((long) -6));
        Assertions.assertEquals("8", mock.oneArg(new BigDecimal("0.5")));
        verify(mock);
    }

    @Test
    public void greaterOrEqualOverloaded() {
        expect(mock.oneArg(geq((byte) 1))).andReturn("1");
        expect(mock.oneArg(geq((double) 1))).andReturn("3");
        expect(mock.oneArg(geq((float) 1))).andReturn("4");
        expect(mock.oneArg(geq(1))).andReturn("5");
        expect(mock.oneArg(geq((long) 1))).andReturn("6");
        expect(mock.oneArg(geq((short) 1))).andReturn("7");
        expect(mock.oneArg(geq(new BigDecimal("1")))).andReturn("8");
        replay(mock);
        Assertions.assertEquals("1", mock.oneArg((byte) 2));
        Assertions.assertEquals("3", mock.oneArg((double) 1));
        Assertions.assertEquals("7", mock.oneArg((short) 2));
        Assertions.assertEquals("4", mock.oneArg((float) 3));
        Assertions.assertEquals("5", mock.oneArg(4));
        Assertions.assertEquals("6", mock.oneArg((long) 5));
        Assertions.assertEquals("8", mock.oneArg(new BigDecimal("1.5")));
        verify(mock);
    }

    @Test
    public void greaterThanOverloaded() {
        expect(mock.oneArg(gt((byte) 1))).andReturn("1");
        expect(mock.oneArg(gt((double) 1))).andReturn("3");
        expect(mock.oneArg(gt((float) 1))).andReturn("4");
        expect(mock.oneArg(gt(1))).andReturn("5");
        expect(mock.oneArg(gt((long) 1))).andReturn("6");
        expect(mock.oneArg(gt((short) 1))).andReturn("7");
        expect(mock.oneArg(gt(new BigDecimal("1")))).andReturn("8");
        replay(mock);
        Assertions.assertEquals("1", mock.oneArg((byte) 2));
        Assertions.assertEquals("3", mock.oneArg((double) 2));
        Assertions.assertEquals("7", mock.oneArg((short) 2));
        Assertions.assertEquals("4", mock.oneArg((float) 3));
        Assertions.assertEquals("5", mock.oneArg(2));
        Assertions.assertEquals("6", mock.oneArg((long) 5));
        Assertions.assertEquals("8", mock.oneArg(new BigDecimal("1.5")));
        verify(mock);
    }

    @Test
    public void cmpTo() {
        expect(mock.oneArg(cmpEq(new BigDecimal("1.5")))).andReturn("0");
        replay(mock);
        Assertions.assertEquals("0", mock.oneArg(new BigDecimal("1.50")));
        verify(mock);
    }

    public static class A {
        private final int value;

        public A(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @Test
    public void compareWithComparator() {
        // Undertype just to make sure the generic typing works
        Comparator<Object> comparator = new Comparator<Object>() {
            private int compare(A a1, A a2) {
                return a1.getValue() - a2.getValue();
            }

            @Override
            public int compare(Object o1, Object o2) {
                return compare((A) o1, (A) o2);
            }
        };

        // Check my comparator works
        Assertions.assertTrue(comparator.compare(new A(1), new A(2)) < 0);
        Assertions.assertTrue(comparator.compare(new A(2), new A(1)) > 0);
        Assertions.assertEquals(0, comparator.compare(new A(1), new A(1)));

        // Now test EasyMock.cmp
        checkOrder(mock, true);

        expect(mock.oneArg(cmp(new A(5), comparator, LogicalOperator.EQUAL))).andReturn("0");

        expect(mock.oneArg(cmp(new A(5), comparator, LogicalOperator.GREATER))).andReturn("1");

        expect(mock.oneArg(cmp(new A(5), comparator, LogicalOperator.GREATER_OR_EQUAL))).andReturn("2");
        expect(mock.oneArg(cmp(new A(5), comparator, LogicalOperator.GREATER_OR_EQUAL))).andReturn("2");

        expect(mock.oneArg(cmp(new A(5), comparator, LogicalOperator.LESS_OR_EQUAL))).andReturn("3");
        expect(mock.oneArg(cmp(new A(5), comparator, LogicalOperator.LESS_OR_EQUAL))).andReturn("3");

        expect(mock.oneArg(cmp(new A(5), comparator, LogicalOperator.LESS_THAN))).andReturn("4");

        replay(mock);

        checkItFails(null); // null is not comparable so always return false
        try {
            mock.oneArg("");
            Assertions.fail();
        } catch (AssertionError e) {
        } // different type isn't either

        checkItFails(new A(4));
        checkItFails(new A(6));
        Assertions.assertEquals("0", mock.oneArg(new A(5)));

        checkItFails(new A(4));
        checkItFails(new A(5));
        Assertions.assertEquals("1", mock.oneArg(new A(6)));

        checkItFails(new A(4));
        Assertions.assertEquals("2", mock.oneArg(new A(6)));
        Assertions.assertEquals("2", mock.oneArg(new A(5)));

        checkItFails(new A(6));
        Assertions.assertEquals("3", mock.oneArg(new A(4)));
        Assertions.assertEquals("3", mock.oneArg(new A(5)));

        checkItFails(new A(5));
        checkItFails(new A(6));
        Assertions.assertEquals("4", mock.oneArg(new A(4)));

        verifyRecording(mock);
    }

    private void checkItFails(A a) {
        try {
            mock.oneArg(a);
            Assertions.fail();
        } catch (AssertionError e) {
        }
    }

    @Test
    public void any() {
        expect(mock.oneArg(anyBoolean())).andReturn("0");
        expect(mock.oneArg(anyByte())).andReturn("1");
        expect(mock.oneArg(anyChar())).andReturn("2");
        expect(mock.oneArg(anyDouble())).andReturn("3");
        expect(mock.oneArg(anyFloat())).andReturn("4");
        expect(mock.oneArg(anyInt())).andReturn("5");
        expect(mock.oneArg(anyLong())).andReturn("6");
        expect(mock.oneArg(anyShort())).andReturn("7");
        expect(mock.oneArg(anyObject())).andReturn("8");
        expect(mock.oneArg(anyObject(String.class))).andReturn("9");
        expect(mock.oneArg(EasyMock.<List<String>> anyObject())).andReturn("9"); // make sure there's no warning on the cast
        expect(mock.oneArg(anyString())).andReturn("10");
        replay(mock);
        Assertions.assertEquals("9", mock.oneArg(Collections.emptyList()));
        Assertions.assertEquals("0", mock.oneArg(true));
        Assertions.assertEquals("1", mock.oneArg((byte) 1));
        Assertions.assertEquals("2", mock.oneArg((char) 1));
        Assertions.assertEquals("3", mock.oneArg((double) 1));
        Assertions.assertEquals("7", mock.oneArg((short) 1));
        Assertions.assertEquals("8", mock.oneArg("Test"));
        Assertions.assertEquals("4", mock.oneArg((float) 1));
        Assertions.assertEquals("5", mock.oneArg(1));
        Assertions.assertEquals("6", mock.oneArg((long) 1));
        Assertions.assertEquals("9", mock.oneArg("Other Test"));
        Assertions.assertEquals("10", mock.oneArg(""));
        verify(mock);
    }

    @Test
    public void arrayEquals() {
        expect(mock.oneArray(aryEq(new boolean[] { true }))).andReturn("0");
        expect(mock.oneArray(aryEq(new byte[] { 1 }))).andReturn("1");
        expect(mock.oneArray(aryEq(new char[] { 1 }))).andReturn("2");
        expect(mock.oneArray(aryEq(new double[] { 1 }))).andReturn("3");
        expect(mock.oneArray(aryEq(new float[] { 1 }))).andReturn("4");
        expect(mock.oneArray(aryEq(new int[] { 1 }))).andReturn("5");
        expect(mock.oneArray(aryEq(new long[] { 1 }))).andReturn("6");
        expect(mock.oneArray(aryEq(new short[] { 1 }))).andReturn("7");
        expect(mock.oneArray(aryEq(new String[] { "Test" }))).andReturn("8");
        expect(mock.oneArray(aryEq(new Object[] { "Test" }))).andReturn("9");
        replay(mock);
        Assertions.assertEquals("9", mock.oneArray(new Object[] { "Test" }));
        Assertions.assertEquals("0", mock.oneArray(new boolean[] { true }));
        Assertions.assertEquals("1", mock.oneArray(new byte[] { 1 }));
        Assertions.assertEquals("2", mock.oneArray(new char[] { 1 }));
        Assertions.assertEquals("3", mock.oneArray(new double[] { 1 }));
        Assertions.assertEquals("7", mock.oneArray(new short[] { 1 }));
        Assertions.assertEquals("8", mock.oneArray(new String[] { "Test" }));
        Assertions.assertEquals("4", mock.oneArray(new float[] { 1 }));
        Assertions.assertEquals("5", mock.oneArray(new int[] { 1 }));
        Assertions.assertEquals("6", mock.oneArray(new long[] { 1 }));
        verify(mock);
    }

    @Test
    public void greaterOrEqual() {
        expect(mock.oneArg(geq(7))).andReturn("1").times(3);
        expect(mock.oneArg(lt(7))).andStubReturn("2");

        replay(mock);

        Assertions.assertEquals("1", mock.oneArg(7));
        Assertions.assertEquals("2", mock.oneArg(6));
        Assertions.assertEquals("1", mock.oneArg(8));
        Assertions.assertEquals("2", mock.oneArg(6));
        Assertions.assertEquals("1", mock.oneArg(9));

        verify(mock);
    }

    @Test
    public void greaterThan() {
        expect(mock.oneArg(gt(7))).andReturn("1").times(3);
        expect(mock.oneArg(leq(7))).andStubReturn("2");

        replay(mock);

        Assertions.assertEquals("1", mock.oneArg(8));
        Assertions.assertEquals("2", mock.oneArg(7));
        Assertions.assertEquals("1", mock.oneArg(9));
        Assertions.assertEquals("2", mock.oneArg(6));
        Assertions.assertEquals("1", mock.oneArg(10));

        verify(mock);
    }

    @Test
    public void lessOrEqual() {
        expect(mock.oneArg(leq(7))).andReturn("1").times(3);
        expect(mock.oneArg(gt(7))).andStubReturn("2");

        replay(mock);

        Assertions.assertEquals("1", mock.oneArg(7));
        Assertions.assertEquals("2", mock.oneArg(8));
        Assertions.assertEquals("1", mock.oneArg(6));
        Assertions.assertEquals("2", mock.oneArg(9));
        Assertions.assertEquals("1", mock.oneArg(5));

        verify(mock);
    }

    @Test
    public void lessThan() {
        expect(mock.oneArg(lt(7))).andReturn("1").times(3);
        expect(mock.oneArg(geq(7))).andStubReturn("2");

        replay(mock);

        Assertions.assertEquals("1", mock.oneArg(5));
        Assertions.assertEquals("2", mock.oneArg(7));
        Assertions.assertEquals("1", mock.oneArg(6));
        Assertions.assertEquals("2", mock.oneArg(8));
        Assertions.assertEquals("1", mock.oneArg(4));

        verify(mock);
    }

    @Test
    public void testOr() {
        expect(mock.oneArg(or(eq(7), eq(9)))).andReturn("1").atLeastOnce();
        expect(mock.oneArg(anyInt())).andStubReturn("2");

        replay(mock);

        Assertions.assertEquals("1", mock.oneArg(7));
        Assertions.assertEquals("1", mock.oneArg(9));
        Assertions.assertEquals("2", mock.oneArg(10));

        verify(mock);
    }

    @Test
    public void testNull() {
        expect(mock.threeArgumentMethod(eq(1), isNull(), eq(""))).andReturn("1");
        expect(mock.threeArgumentMethod(eq(1), isNull(Object.class), eq(""))).andReturn("2");
        expect(mock.threeArgumentMethod(eq(1), not(isNull()), eq(""))).andStubReturn("3");

        replay(mock);

        Assertions.assertEquals("1", mock.threeArgumentMethod(1, null, ""));
        Assertions.assertEquals("2", mock.threeArgumentMethod(1, null, ""));
        Assertions.assertEquals("3", mock.threeArgumentMethod(1, new Object(), ""));

        verify(mock);
    }

    @Test
    public void testNotNull() {
        expect(mock.threeArgumentMethod(eq(1), notNull(), eq(""))).andReturn("1");
        expect(mock.threeArgumentMethod(eq(1), notNull(Object.class), eq(""))).andReturn("2");
        expect(mock.threeArgumentMethod(eq(1), not(notNull()), eq(""))).andStubReturn("3");

        replay(mock);

        Assertions.assertEquals("1", mock.threeArgumentMethod(1, new Object(), ""));
        Assertions.assertEquals("2", mock.threeArgumentMethod(1, new Object(), ""));
        Assertions.assertEquals("3", mock.threeArgumentMethod(1, null, ""));

        verify(mock);
    }

    @Test
    public void testFind() {
        expect(mock.oneArg(find("[a-z]+\\d"))).andReturn("1").atLeastOnce();
        expect(mock.oneArg(find("\\d\\d"))).andStubReturn("2");

        replay(mock);

        Assertions.assertEquals("1", mock.oneArg("1ab12"));
        Assertions.assertEquals("2", mock.oneArg("312xx"));

        verify(mock);
    }

    @Test
    public void testMatches() {
        expect(mock.oneArg(matches("[a-z]+\\d\\d"))).andReturn("1").atLeastOnce();
        expect(mock.oneArg(matches("\\d\\d\\d"))).andStubReturn("2");

        replay(mock);

        Assertions.assertEquals("1", mock.oneArg("a12"));
        Assertions.assertEquals("2", mock.oneArg("131"));

        verify(mock);
    }

    @Test
    public void testContains() {
        expect(mock.oneArg(contains("ab"))).andReturn("1").atLeastOnce();
        expect(mock.oneArg(contains("bc"))).andStubReturn("2");

        replay(mock);

        Assertions.assertEquals("1", mock.oneArg("xabcx"));
        Assertions.assertEquals("2", mock.oneArg("xdbcx"));

        verify(mock);
    }

    @Test
    public void testStartsWith() {
        expect(mock.oneArg(startsWith("ab"))).andReturn("1").atLeastOnce();
        expect(mock.oneArg(startsWith("bc"))).andStubReturn("2");

        replay(mock);

        Assertions.assertEquals("1", mock.oneArg("abcx"));
        Assertions.assertEquals("2", mock.oneArg("bcxe"));

        verify(mock);
    }

    @Test
    public void testEndsWith() {
        expect(mock.oneArg(endsWith("ab"))).andReturn("1").atLeastOnce();
        expect(mock.oneArg(endsWith("bc"))).andStubReturn("2");

        replay(mock);

        Assertions.assertEquals("1", mock.oneArg("xab"));
        Assertions.assertEquals("2", mock.oneArg("xbc"));

        verify(mock);
    }

    @Test
    public void equalsWithDelta() {
        expect(mock.oneArg(eq(1.0D, 0.1D))).andReturn("1").atLeastOnce();
        expect(mock.oneArg(eq(2.0D, 0.1D))).andStubReturn("2");
        expect(mock.oneArg(eq(1.0F, 0.1F))).andReturn("3").atLeastOnce();
        expect(mock.oneArg(eq(2.0F, 0.1F))).andStubReturn("4");
        expect(mock.oneArg(eq(2.0F, 0.1F))).andStubReturn("4");

        replay(mock);

        Assertions.assertEquals("1", mock.oneArg(1.0));
        Assertions.assertEquals("1", mock.oneArg(0.91));
        Assertions.assertEquals("1", mock.oneArg(1.09));
        Assertions.assertEquals("2", mock.oneArg(2.0));

        Assertions.assertEquals("3", mock.oneArg(1.0F));
        Assertions.assertEquals("3", mock.oneArg(0.91F));
        Assertions.assertEquals("3", mock.oneArg(1.09F));
        Assertions.assertEquals("4", mock.oneArg(2.0F));

        verify(mock);
    }

    @SuppressWarnings("RedundantStringConstructorCall")
    @Test
    public void testSame() {
        Object one = new String("1243");
        Object two = new String("1243");

        Assertions.assertNotSame(one, two);
        Assertions.assertEquals(one, two);

        expect(mock.oneArg(same(one))).andReturn("1").atLeastOnce();
        expect(mock.oneArg(same(two))).andStubReturn("2");

        replay(mock);

        Assertions.assertEquals("1", mock.oneArg(one));
        Assertions.assertEquals("2", mock.oneArg(two));

        verify(mock);
    }
}
