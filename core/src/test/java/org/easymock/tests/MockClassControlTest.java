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
package org.easymock.tests;

import static org.easymock.EasyMock.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests of MockClassControl basic functionalities
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
        public boolean equals(Object o) {
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

    @SuppressWarnings("deprecation")
    public static class ClassWithFinalize {

        @Override
        public void finalize() {
        }
    }

    private Object mock;

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
        mock = null;
    }

    private void initMock(Class<?> toMock) {
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
    private void testEquals(Class<?> toMock) {
        initMock(toMock);
        Assertions.assertEquals(mock, mock);
        replay(mock);
        Assertions.assertEquals(mock, mock);
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
    private void testHashCode(Class<?> toMock) {
        initMock(toMock);
        int code = mock.hashCode();
        replay(mock);
        Assertions.assertEquals(code, mock.hashCode());
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
    private void testToString(Class<?> toMock) {
        initMock(toMock);
        String expectedValue = "EasyMock for " + toMock.toString();
        Assertions.assertEquals(expectedValue, mock.toString());
        replay(mock);
        Assertions.assertEquals(expectedValue, mock.toString());
    }

    @Test
    public void testFinalize_AreIgnored() {
        ClassWithFinalize mock = createMock(ClassWithFinalize.class);
        replay(mock);
        mock.finalize();
        verify(mock);
    }
}
