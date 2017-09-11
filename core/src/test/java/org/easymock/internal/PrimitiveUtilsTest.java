/**
 * Copyright 2001-2017 the original author or authors.
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

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Henri Tremblay
 */
public class PrimitiveUtilsTest {
    @Test
    public void getEmptyValue() throws Exception {
        assertEquals(null, PrimitiveUtils.getEmptyValue(Void.TYPE));
        assertEquals(false, PrimitiveUtils.getEmptyValue(Boolean.TYPE));
        assertEquals(Byte.valueOf((byte) 0), PrimitiveUtils.getEmptyValue(Byte.TYPE));
        assertEquals(Short.valueOf((short) 0), PrimitiveUtils.getEmptyValue(Short.TYPE));
        assertEquals(Character.valueOf((char) 0), PrimitiveUtils.getEmptyValue(Character.TYPE));
        assertEquals(Integer.valueOf(0), PrimitiveUtils.getEmptyValue(Integer.TYPE));
        assertEquals(Long.valueOf(0), PrimitiveUtils.getEmptyValue(Long.TYPE));
        assertEquals(Float.valueOf(0), PrimitiveUtils.getEmptyValue(Float.TYPE));
        assertEquals(Double.valueOf(0), PrimitiveUtils.getEmptyValue(Double.TYPE));
    }

    @Test
    public void getWrapperType() throws Exception {
        assertEquals(Void.class, PrimitiveUtils.getWrapperType(Void.TYPE));
        assertEquals(Boolean.class, PrimitiveUtils.getWrapperType(Boolean.TYPE));
        assertEquals(Byte.class, PrimitiveUtils.getWrapperType(Byte.TYPE));
        assertEquals(Short.class, PrimitiveUtils.getWrapperType(Short.TYPE));
        assertEquals(Character.class, PrimitiveUtils.getWrapperType(Character.TYPE));
        assertEquals(Integer.class, PrimitiveUtils.getWrapperType(Integer.TYPE));
        assertEquals(Long.class, PrimitiveUtils.getWrapperType(Long.TYPE));
        assertEquals(Float.class, PrimitiveUtils.getWrapperType(Float.TYPE));
        assertEquals(Double.class, PrimitiveUtils.getWrapperType(Double.TYPE));
    }

    @Test
    public void isNumberWrapper() throws Exception {
        assertTrue(PrimitiveUtils.isPrimitiveWrapper(Byte.class));
        assertTrue(PrimitiveUtils.isPrimitiveWrapper(Short.class));
        assertTrue(PrimitiveUtils.isPrimitiveWrapper(Integer.class));
        assertTrue(PrimitiveUtils.isPrimitiveWrapper(Long.class));
        assertTrue(PrimitiveUtils.isPrimitiveWrapper(Float.class));
        assertTrue(PrimitiveUtils.isPrimitiveWrapper(Double.class));

        assertFalse(PrimitiveUtils.isPrimitiveWrapper(getClass()));
        assertFalse(PrimitiveUtils.isPrimitiveWrapper(Integer.TYPE));
    }

    @Test
    public void getPrimitiveTypeNameFromWrapper() throws Exception {
        assertPrimitiveName(Byte.TYPE, Byte.class);
        assertPrimitiveName(Short.TYPE, Short.class);
        assertPrimitiveName(Integer.TYPE, Integer.class);
        assertPrimitiveName(Long.TYPE, Long.class);
        assertPrimitiveName(Float.TYPE, Float.class);
        assertPrimitiveName(Double.TYPE, Double.class);

        assertNull(PrimitiveUtils.getPrimitiveTypeNameFromWrapper(getClass()));
        assertNull(PrimitiveUtils.getPrimitiveTypeNameFromWrapper(Long.TYPE));
    }

    private void assertPrimitiveName(Class<?> expected, Class<?> actual) {
        assertEquals(expected.getName(), PrimitiveUtils.getPrimitiveTypeNameFromWrapper(actual));
    }
}
