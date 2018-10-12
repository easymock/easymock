/*
 * Copyright 2001-2018 the original author or authors.
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

import org.easymock.EasyMock;

import java.io.*;
import java.lang.reflect.*;

/**
 * Default class instantiator that is pretty limited. It just hope that the
 * mocked class has a public empty constructor.
 *
 * @author Henri Tremblay
 */
public class DefaultClassInstantiator implements IClassInstantiator {

    private static final Class<?>[] EMPTY = new Class<?>[0];

    /**
     * Try to instantiate a class without using a special constructor. See
     * documentation for the algorithm.
     *
     * @param c
     *            Class to instantiate
     */
    public Object newInstance(Class<?> c) throws InstantiationException {

        if (isSerializable(c)) {
            try {
                return readObject(getSerializedBytes(c));
                // ///CLOVER:OFF
            } catch (IOException e) {
                throw new RuntimeException("Failed to instantiate " + c.getName() + "'s mock: ", e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Failed to instantiate " + c.getName() + "'s mock: ", e);
            }
            // ///CLOVER:ON
        }

        Constructor<?> constructor = getConstructorToUse(c);
        Object[] params = getArgsForTypes(constructor.getParameterTypes());
        try {
            return constructor.newInstance(params);
            // ///CLOVER:OFF
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to instantiate " + c.getName() + "'s mock: ", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to instantiate " + c.getName() + "'s mock: ", e);
            // ///CLOVER:ON
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate " + c.getName() + "'s mock: ", e);
        }
    }

    /**
     * Tells if the provided class is serializable
     *
     * @param clazz
     *            Class to check
     * @return If the class is serializable
     */
    private boolean isSerializable(Class<?> clazz) {
        return Serializable.class.isAssignableFrom(clazz);
    }

    /**
     * Return the constructor considered the best to use with this class.
     * Algorithm is: No args constructor and then first constructor defined in
     * the class
     *
     * @param clazz
     *            Class in which constructor is searched
     * @return Constructor to use
     */
    public Constructor<?> getConstructorToUse(Class<?> clazz) {
        // First try to use the empty constructor
        try {
            return clazz.getConstructor(EMPTY);
        } catch (NoSuchMethodException e) {
            // If it fails just use the first one
            if (clazz.getConstructors().length == 0) {
                throw new IllegalArgumentException("No visible constructors in class " + clazz.getName());
            }
            return clazz.getConstructors()[0];
        }
    }

    /**
     * Get some default instances of provided classes
     *
     * @param methodTypes
     *            Classes to instantiate
     * @return Instances of methodTypes in order
     * @throws InstantiationException Thrown if the class instantiation fails
     */
    public Object[] getArgsForTypes(Class<?>[] methodTypes) throws InstantiationException {
        Object[] methodArgs = new Object[methodTypes.length];

        for (int i = 0; i < methodTypes.length; i++) {

            if (methodTypes[i].isPrimitive()) {
                // Return a nice wrapped primitive type
                methodArgs[i] = RecordState.emptyReturnValueFor(methodTypes[i]);
            // ///CLOVER:OFF TODO: Remove when we manage to fix the ignored tests
            } else if (Modifier.isFinal(methodTypes[i].getModifiers())) {
                // Instantiate the class using the best constructor we can find
                // (because it's not
                // possible to mock a final class)
                methodArgs[i] = newInstance(methodTypes[i]);
                // ///CLOVER:ON
            } else {
                // For all classes and interfaces, just return a nice mock
                Object mock = EasyMock.createNiceMock(methodTypes[i]);
                EasyMock.replay(mock);
                methodArgs[i] = mock;
            }
        }
        return methodArgs;
    }

    private static byte[] getSerializedBytes(Class<?> clazz) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(baos);
        try {
            data.writeShort(ObjectStreamConstants.STREAM_MAGIC);
            data.writeShort(ObjectStreamConstants.STREAM_VERSION);
            data.writeByte(ObjectStreamConstants.TC_OBJECT);
            data.writeByte(ObjectStreamConstants.TC_CLASSDESC);
            data.writeUTF(clazz.getName());

            Long suid = getSerializableUID(clazz);

            data.writeLong(suid.longValue());

            data.writeByte(2); // classDescFlags (2 = Serializable)
            data.writeShort(0); // field count
            data.writeByte(ObjectStreamConstants.TC_ENDBLOCKDATA);
            data.writeByte(ObjectStreamConstants.TC_NULL);
        }
        finally {
            data.close();
        }
        return baos.toByteArray();
    }

    private static Long getSerializableUID(Class<?> clazz) {

        try {
            Field f = clazz.getDeclaredField("serialVersionUID");
            int mask = Modifier.STATIC | Modifier.FINAL;
            if ((f.getModifiers() & mask) == mask) {
                f.setAccessible(true);
                return Long.valueOf(f.getLong(null));
            }
        } catch (NoSuchFieldException e) {
            // It's not there, compute it then
        } catch (IllegalAccessException e) {
            // ///CLOVER:OFF
            throw new RuntimeException("Should have been able to get serialVersionUID since it's there");
            // ///CLOVER:ON
        }
        // ///CLOVER:OFF
        return callLongMethod(clazz,
                ClassInstantiatorFactory.is1_3Specifications() ? "computeSerialVersionUID"
                        : "computeDefaultSUID");
        // ///CLOVER:ON
    }

    private static Long callLongMethod(Class<?> clazz, String methodName) {

        Method method;
        // ///CLOVER:OFF
        try {
            method = ObjectStreamClass.class.getDeclaredMethod(methodName, Class.class);
        } catch (NoSuchMethodException e) {
            throw new InternalError("ObjectStreamClass." + methodName + " seems to have vanished");
        }
        boolean accessible = method.isAccessible();
        method.setAccessible(true);
        Long suid;
        try {
            suid = (Long) method.invoke(null, clazz);
        } catch (IllegalAccessException e) {
            throw new InternalError("ObjectStreamClass." + methodName + " should have been accessible");
        } catch (InvocationTargetException e) {
            throw new InternalError("ObjectStreamClass." + methodName + " failled to be called: "
                    + e.getMessage());
        }
        method.setAccessible(accessible);
        // ///CLOVER:ON
        return suid;
    }

    private static Object readObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
        try {
            return in.readObject();
        }
        finally {
            in.close();
        }
    }

}
