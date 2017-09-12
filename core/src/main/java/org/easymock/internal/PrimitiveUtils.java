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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Henri Tremblay
 */
public final class PrimitiveUtils {

    private static class PrimitiveEntry {
        final Object emptyValue;
        final Class<?> wrapperType;

        private PrimitiveEntry(Object emptyValue, Class<?> wrapperType) {
            this.emptyValue = emptyValue;
            this.wrapperType = wrapperType;
        }
    }

    private static final Map<Class<?>, PrimitiveEntry> primitiveTypes = new HashMap<Class<?>, PrimitiveEntry>(9);
    private static final Map<Class<?>, String> wrapperTypes = new HashMap<Class<?>, String>(6);

    static {
        primitiveTypes.put(Void.TYPE, new PrimitiveEntry(null, Void.class));
        primitiveTypes.put(Boolean.TYPE, new PrimitiveEntry(Boolean.FALSE, Boolean.class));
        primitiveTypes.put(Byte.TYPE, new PrimitiveEntry(Byte.valueOf((byte) 0), Byte.class));
        primitiveTypes.put(Short.TYPE, new PrimitiveEntry(Short.valueOf((short) 0), Short.class));
        primitiveTypes.put(Character.TYPE, new PrimitiveEntry(Character.valueOf((char) 0), Character.class));
        primitiveTypes.put(Integer.TYPE, new PrimitiveEntry(Integer.valueOf(0), Integer.class));
        primitiveTypes.put(Long.TYPE, new PrimitiveEntry(Long.valueOf(0), Long.class));
        primitiveTypes.put(Float.TYPE, new PrimitiveEntry(Float.valueOf(0), Float.class));
        primitiveTypes.put(Double.TYPE, new PrimitiveEntry(Double.valueOf(0), Double.class));

        wrapperTypes.put(Byte.class, "byte");
        wrapperTypes.put(Short.class, "short");
        wrapperTypes.put(Integer.class, "int");
        wrapperTypes.put(Long.class, "long");
        wrapperTypes.put(Float.class, "float");
        wrapperTypes.put(Double.class, "double");
    }

    private PrimitiveUtils() {}

    public static Object getEmptyValue(Class<?> type) {
        return primitiveTypes.get(type).emptyValue;
    }

    public static Class<?> getWrapperType(Class<?> type) {
        return primitiveTypes.get(type).wrapperType;
    }

    public static boolean isPrimitiveWrapper(Class<?> type) {
        return wrapperTypes.containsKey(type);
    }

    public static String getPrimitiveTypeNameFromWrapper(Class<?> type) {
        return wrapperTypes.get(type);
    }
}
