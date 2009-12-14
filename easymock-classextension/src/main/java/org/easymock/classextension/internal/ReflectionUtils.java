/*
 * Copyright (c) 2003-2009 Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.classextension.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    public static Method findMethod(Class<?> clazz, String name) {
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
    public static Method findMethod(Class<?> clazz, String name,
            Class<?>... paramTypes) {
        Class<?> searchType = clazz;
        while (!Object.class.equals(searchType) && searchType != null) {
            Method[] methods = searchType.getDeclaredMethods();
            Method result = null;
            for (Method method : methods) {
                // Private methods can't be mocked so just skip them
                if (Modifier.isPrivate(method.getModifiers())) {
                    continue;
                }
                if (name.equals(method.getName())) {
                    if (paramTypes == null) {
                        if (result != null) {
                            throw new RuntimeException(
                                    "Ambiguous name: More than one method are named "
                                            + name);
                        }
                        result = method;
                    } else if (Arrays.equals(paramTypes, method
                            .getParameterTypes())) {
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
     * @param objs
     *            list of arguments of the constructor
     * @return a constructor with the arguments in {@code objs}
     * @throws NoSuchMethodException
     *             when the constructor with {@code args} does not exist or is
     *             ambiguous
     */
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> getConstructor(Class<T> clazz,
            Object... objs)
            throws NoSuchMethodException {
        Constructor<T> ret = null;
        for (Constructor<?> classConstructor : clazz.getDeclaredConstructors()) {
            if (isMatchingConstructor(classConstructor, objs)) {
                if (ret != null) {
                    // We had already found a constructor, so this is ambiguous
                    throw new IllegalArgumentException(
                            "Ambiguity in the constructors for "
                                    + clazz.getName() + ".");
                }

                // This unsafe cast is guaranteed to be correct by Class#getConstructors()
                ret = (Constructor<T>) classConstructor;
            }
        }

        if (ret != null) {
            return ret;
        }

        // Not found
        throw new NoSuchMethodException("Couldn't find constructor for class "
                + clazz.getName());
    }

    /**
     * Returns true if objects in {@code objs} are eligible to be passed to
     * {@code classConstructor}.
     */
    private static boolean isMatchingConstructor(
            Constructor<?> classConstructor, Object... objs) {
        
        if (Modifier.isPrivate(classConstructor.getModifiers())) {
            return false;
        }
        
        Class<?>[] parameterTypes = classConstructor.getParameterTypes();
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
    private static Class<?> wrapPrimitive(Class<?> parameterType) {
        return primitiveToWrapperType.get(parameterType);
    }

}
