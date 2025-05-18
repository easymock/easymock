/*
 * Copyright 2001-2025 the original author or authors.
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
package org.easymock.internal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Henri Tremblay
 */
class PrimitiveUtilsTest {
    @Test
    void getEmptyValue() {
        Assertions.assertNull(PrimitiveUtils.getEmptyValue(Void.TYPE));
        Assertions.assertEquals(false, PrimitiveUtils.getEmptyValue(Boolean.TYPE));
        Assertions.assertEquals((byte) 0, PrimitiveUtils.getEmptyValue(Byte.TYPE));
        Assertions.assertEquals((short) 0, PrimitiveUtils.getEmptyValue(Short.TYPE));
        Assertions.assertEquals((char) 0, PrimitiveUtils.getEmptyValue(Character.TYPE));
        Assertions.assertEquals(0, PrimitiveUtils.getEmptyValue(Integer.TYPE));
        Assertions.assertEquals(0L, PrimitiveUtils.getEmptyValue(Long.TYPE));
        Assertions.assertEquals((float) 0, PrimitiveUtils.getEmptyValue(Float.TYPE));
        Assertions.assertEquals((double) 0, PrimitiveUtils.getEmptyValue(Double.TYPE));
    }

    @Test
    void getWrapperType() {
        Assertions.assertEquals(Void.class, PrimitiveUtils.getWrapperType(Void.TYPE));
        Assertions.assertEquals(Boolean.class, PrimitiveUtils.getWrapperType(Boolean.TYPE));
        Assertions.assertEquals(Byte.class, PrimitiveUtils.getWrapperType(Byte.TYPE));
        Assertions.assertEquals(Short.class, PrimitiveUtils.getWrapperType(Short.TYPE));
        Assertions.assertEquals(Character.class, PrimitiveUtils.getWrapperType(Character.TYPE));
        Assertions.assertEquals(Integer.class, PrimitiveUtils.getWrapperType(Integer.TYPE));
        Assertions.assertEquals(Long.class, PrimitiveUtils.getWrapperType(Long.TYPE));
        Assertions.assertEquals(Float.class, PrimitiveUtils.getWrapperType(Float.TYPE));
        Assertions.assertEquals(Double.class, PrimitiveUtils.getWrapperType(Double.TYPE));
    }

    @Test
    void isNumberWrapper() {
        Assertions.assertTrue(PrimitiveUtils.isPrimitiveWrapper(Byte.class));
        Assertions.assertTrue(PrimitiveUtils.isPrimitiveWrapper(Short.class));
        Assertions.assertTrue(PrimitiveUtils.isPrimitiveWrapper(Integer.class));
        Assertions.assertTrue(PrimitiveUtils.isPrimitiveWrapper(Long.class));
        Assertions.assertTrue(PrimitiveUtils.isPrimitiveWrapper(Float.class));
        Assertions.assertTrue(PrimitiveUtils.isPrimitiveWrapper(Double.class));

        Assertions.assertFalse(PrimitiveUtils.isPrimitiveWrapper(getClass()));
        Assertions.assertFalse(PrimitiveUtils.isPrimitiveWrapper(Integer.TYPE));
    }

    @Test
    void getPrimitiveTypeNameFromWrapper() {
        assertPrimitiveName(Byte.TYPE, Byte.class);
        assertPrimitiveName(Short.TYPE, Short.class);
        assertPrimitiveName(Integer.TYPE, Integer.class);
        assertPrimitiveName(Long.TYPE, Long.class);
        assertPrimitiveName(Float.TYPE, Float.class);
        assertPrimitiveName(Double.TYPE, Double.class);

        Assertions.assertNull(PrimitiveUtils.getPrimitiveTypeNameFromWrapper(getClass()));
        Assertions.assertNull(PrimitiveUtils.getPrimitiveTypeNameFromWrapper(Long.TYPE));
    }

    private void assertPrimitiveName(Class<?> expected, Class<?> actual) {
        Assertions.assertEquals(expected.getName(), PrimitiveUtils.getPrimitiveTypeNameFromWrapper(actual));
    }
}
