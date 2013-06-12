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
package org.easymock.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Henri Tremblay
 */
public final class ReflectionUtils {

    private static Map<Class<?>, Class<?>> primitiveToWrapperType = new HashMap<Class<?>, Class<?>>();

    static {
        primitiveToWrapperType.put(boolean.class, Boolean.class);
        primitiveToWrapperType.put(byte.class, Byte.class);
        primitiveToWrapperType.put(short.class, Short.class);
        primitiveToWrapperType.put(char.class, Character.class);
        primitiveToWrapperType.put(int.class, Integer.class);
        primitiveToWrapperType.put(long.class, Long.class);
        primitiveToWrapperType.put(float.class, Float.class);
        primitiveToWrapperType.put(double.class, Double.class);
    }

    public static final Method OBJECT_EQUALS = getDeclaredMethod(Object.class, "equals",
            new Class[] { Object.class });

    public static final Method OBJECT_HASHCODE = getDeclaredMethod(Object.class, "hashCode", (Class[]) null);

    public static final Method OBJECT_TOSTRING = getDeclaredMethod(Object.class, "toString", (Class[]) null);

    public static final Method OBJECT_FINALIZE = getDeclaredMethod(Object.class, "finalize", (Class[]) null);

    // ///CLOVER:OFF
    private ReflectionUtils() {
    }

    // ///CLOVER:ON

    /**
     * Attempt to find a {@link Method} on the supplied class with the supplied
     * name and no parameters. Searches all superclasses up to
     * <code>Object</code>.
     * <p>
     * Returns <code>null</code> if no {@link Method} can be found.
     * 
     * @param clazz
     *            the class to introspect
     * @param name
     *            the name of the method
     * @return the Method object, or <code>null</code> if none found
     */
    public static Method findMethod(final Class<?> clazz, final String name) {
        return findMethod(clazz, name, (Class<?>[]) null);
    }

    /**
     * Attempt to find a {@link Method} on the supplied class with the supplied
     * name and parameter types. Searches all superclasses up to
     * <code>Object</code>.
     * <p>
     * Returns <code>null</code> if no {@link Method} can be found.
     * 
     * @param clazz
     *            the class to introspect
     * @param name
     *            the name of the method
     * @param paramTypes
     *            the parameter types of the method (may be <code>null</code> to
     *            indicate any signature)
     * @return the Method object, or <code>null</code> if none found
     */
    public static Method findMethod(final Class<?> clazz, final String name, final Class<?>... paramTypes) {
        Class<?> searchType = clazz;
        while (searchType != null) {
            final Method[] methods = searchType.getDeclaredMethods();
            Method result = null;
            for (final Method method : methods) {
                // Private methods can't be mocked so just skip them
                if (Modifier.isPrivate(method.getModifiers())) {
                    continue;
                }
                // Skip bridges because we never mock them. We mock the method underneath
                if (method.isBridge()) {
                    continue;
                }
                if (name.equals(method.getName())) {
                    if (paramTypes == null) {
                        if (result != null) {
                            throw new RuntimeException("Ambiguous name: More than one method are named "
                                    + name);
                        }
                        result = method;
                    } else if (Arrays.equals(paramTypes, method.getParameterTypes())) {
                        return method;
                    }
                }
            }
            if (result != null) {
                return result;
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    /**
     * Returns a constructor that contains {@code objs} as arguments.
     * 
     * <p>
     * We could not do something like {@code clazz.getConstructor(objs.class())}
     * because that would require casting all the passed arguments to the exact
     * parameter types of the desired constructor.
     * 
     * @param clazz
     *            class on which we are searching the constructor
     * @param <T>
     *            type of the class searched
     * @param objs
     *            list of arguments of the constructor
     * @return a constructor with the arguments in {@code objs}
     * @throws NoSuchMethodException
     *             when the constructor with {@code args} does not exist or is
     *             ambiguous
     */
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> getConstructor(final Class<T> clazz, final Object... objs)
            throws NoSuchMethodException {
        Constructor<T> ret = null;
        for (final Constructor<?> classConstructor : clazz.getDeclaredConstructors()) {
            if (isMatchingConstructor(classConstructor, objs)) {
                if (ret != null) {
                    // We had already found a constructor, so this is ambiguous
                    throw new IllegalArgumentException("Ambiguity in the constructors for " + clazz.getName()
                            + ".");
                }

                // This unsafe cast is guaranteed to be correct by Class#getConstructors()
                ret = (Constructor<T>) classConstructor;
            }
        }

        if (ret != null) {
            return ret;
        }

        // Not found
        throw new NoSuchMethodException("Couldn't find constructor for class " + clazz.getName());
    }

    /**
     * Returns true if objects in {@code objs} are eligible to be passed to
     * {@code classConstructor}.
     */
    private static boolean isMatchingConstructor(final Constructor<?> classConstructor, final Object... objs) {

        if (Modifier.isPrivate(classConstructor.getModifiers())) {
            return false;
        }

        final Class<?>[] parameterTypes = classConstructor.getParameterTypes();
        if (parameterTypes.length != objs.length) {
            return false;
        }

        for (int i = 0; i < objs.length; ++i) {
            Class<?> parameterType = parameterTypes[i];

            // If the parameter is a primitive, we need to get the boxed equivalent type
            if (parameterType.isPrimitive()) {
                parameterType = wrapPrimitive(parameterType);
            }

            // Check if parameter type matches
            if (!parameterType.isInstance(objs[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Given a primitive type, returns its boxed equivalent. For instance, given
     * {@code int}, returns {@code Integer}.
     * 
     * @param parameterType
     *            the primitive type
     * @return the boxed equivalent
     */
    private static Class<?> wrapPrimitive(final Class<?> parameterType) {
        return primitiveToWrapperType.get(parameterType);
    }

    /**
     * Basically calls getDeclaredMethod on a Class but wraps the
     * NoSuchMethodException into a Runtime.
     * 
     * @param clazz
     *            class on which the getDeclaredMethod is called
     * @param name
     *            method name
     * @param paramTypes
     *            method parameters
     * @return the method searched
     */
    public static Method getDeclaredMethod(final Class<?> clazz, final String name,
            final Class<?>[] paramTypes) {
        try {
            return clazz.getDeclaredMethod(name, paramTypes);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
