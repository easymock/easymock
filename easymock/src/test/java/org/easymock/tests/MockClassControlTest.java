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

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests of MockClassControl basic functionnalities
 * 
 * @author Henri Tremblay
 * @author OFFIS, Tammo Freese
 */
public class MockClassControlTest {

    /**
     * Class that will be mocked. The methods defined in it are there just to
     * make sure they are correctly overloaded by the mock.
     */
    public static class ClassToMock {

    }

    /**
     * Same as ClassToMock except that the methods with a standard behavior
     * provided by the mock are overloaded. We expect the standard behavior to
     * still be called.
     */
    public static class ClassToMockWithOverload {

        @Override
        public boolean equals(final Object o) {
            return false;
        }

        @Override
        public int hashCode() {
            return -1;
        }

        @Override
        public String toString() {
            return "super";
        }
    }

    public static class ClassWithAnotherOverload extends ClassToMockWithOverload {

        @Override
        public String toString() {
            return "super.super";
        }
    }

    public static class ClassWithFinalize {

        @Override
        public void finalize() {
        }
    }

    private Object mock;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        mock = null;
    }

    private void initMock(final Class<?> toMock) {
        mock = createMock(toMock);
    }

    @Test
    public void testEquals() {
        testEquals(ClassToMock.class);
    }

    @Test
    public void testEquals_WithOverload() {
        testEquals(ClassToMockWithOverload.class);
    }

    /**
     * Make sure that a mock is equals to itself
     */
    private void testEquals(final Class<?> toMock) {
        initMock(toMock);
        assertEquals(mock, mock);
        replay(mock);
        assertEquals(mock, mock);
    }

    @Test
    public void testHashCode() {
        testHashCode(ClassToMock.class);
    }

    @Test
    public void testHashCode_WithOverload() {
        testHashCode(ClassToMockWithOverload.class);
    }

    /**
     * Make sure the hashCode doesn't need to be recorded and that it stays the
     * same after the replay
     */
    private void testHashCode(final Class<?> toMock) {
        initMock(toMock);
        final int code = mock.hashCode();
        replay(mock);
        assertEquals(code, mock.hashCode());
    }

    @Test
    public void testToString() {
        testToString(ClassToMock.class);
    }

    @Test
    public void testToString_WithOverload() {
        testToString(ClassToMockWithOverload.class);
    }

    @Test
    public void testToString_WithTwoOverload() {
        testToString(ClassWithAnotherOverload.class);
    }

    /**
     * Check that the toString is the EasyMock one giving the mocked class
     */
    private void testToString(final Class<?> toMock) {
        initMock(toMock);
        final String expectedValue = "EasyMock for " + toMock.toString();
        assertEquals(expectedValue, mock.toString());
        replay(mock);
        assertEquals(expectedValue, mock.toString());
    }

    @Test
    public void testFinalize_AreIgnored() {
        final ClassWithFinalize mock = createMock(ClassWithFinalize.class);
        replay(mock);
        mock.finalize();
        verify(mock);
    }
}
