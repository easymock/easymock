/*
 * Copyright 2003-2009 OFFIS, Henri Tremblay
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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MethodSerializationWrapper implements Serializable {

    private static final long serialVersionUID = 1775475200823842126L;

    private static final Map<String, Class<?>> primitiveTypes = new HashMap<String, Class<?>>(
            10);

    static {
        primitiveTypes.put(Boolean.TYPE.getName(), Boolean.TYPE);
        primitiveTypes.put(Byte.TYPE.getName(), Byte.TYPE);
        primitiveTypes.put(Short.TYPE.getName(), Short.TYPE);
        primitiveTypes.put(Character.TYPE.getName(), Character.TYPE);
        primitiveTypes.put(Integer.TYPE.getName(), Integer.TYPE);
        primitiveTypes.put(Long.TYPE.getName(), Long.TYPE);
        primitiveTypes.put(Float.TYPE.getName(), Float.TYPE);
        primitiveTypes.put(Double.TYPE.getName(), Double.TYPE);
    }

    private String className;

    private String methodName;

    private String[] parameterTypeNames;

    public MethodSerializationWrapper(Method m) {
        className = m.getDeclaringClass().getName();
        methodName = m.getName();

        Class<?>[] parameterTypes = m.getParameterTypes();

        parameterTypeNames = new String[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            parameterTypeNames[i] = parameterTypes[i].getName();
        }
    }

    public Method getMethod() throws ClassNotFoundException,
            NoSuchMethodException {
        Class<?> clazz = Class.forName(className, true, Thread.currentThread()
                .getContextClassLoader());

        Class<?>[] parameterTypes = new Class[parameterTypeNames.length];

        for (int i = 0; i < parameterTypeNames.length; i++) {
            Class<?> primitiveType = primitiveTypes.get(parameterTypeNames[i]);
            if (primitiveType != null) {
                parameterTypes[i] = primitiveType;
            } else {
                parameterTypes[i] = Class.forName(parameterTypeNames[i], true,
                        Thread.currentThread().getContextClassLoader());
            }
        }

        Method m = clazz.getMethod(methodName, parameterTypes);

        return m;
    }
}
