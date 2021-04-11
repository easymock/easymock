/*
 * Copyright 2001-2021 the original author or authors.
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
import java.util.function.Predicate;

/**
 * @author Henri Tremblay
 */
public final class ReflectionUtils {

    public static final Predicate<Method> NOT_PRIVATE = method -> !Modifier.isPrivate(method.getModifiers());

    private static final Map<Class<?>, Class<?>> primitiveToWrapperType = new HashMap<>(8);

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
        Object.class);

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
     * {@code Object}. The filter is used to ignore some kind of methods the caller doesn't want to see returned. In this case
     * they are totally ignored and can't clash with a non-ignored one to cause ambiguity.
     * <p>
     * Returns {@code null} if no {@link Method} can be found.
     *
     * @param clazz
     *            the class to introspect
     * @param name
     *            the name of the method
     * @param filter
     *            tells what methods to ignore in the research
     * @return the Method object, or {@code null} if none found
     */
    public static Method findMethod(Class<?> clazz, String name, Predicate<Method> filter) {
        return findMethod(clazz, name, filter, (Class<?>[]) null);
    }

    /**
     * Attempt to find a {@link Method} on the supplied class with the supplied
     * name and parameter types. Searches all superclasses up to
     * {@code Object}. The filter is used to ignore some kind of methods the caller doesn't want to see returned. In this case
     * they are totally ignored and can't clash with a non-ignored one to cause ambiguity.
     * <p>
     * Returns {@code null} if no {@link Method} can be found.
     *
     * @param clazz
     *            the class to introspect
     * @param name
     *            the name of the method
     * @param filter
     *            tells what methods to ignore in the research
     * @param paramTypes
     *            the parameter types of the method (may be {@code null} to
     *            indicate any signature)
     * @return the Method object, or {@code null} if none found
     */
    public static Method findMethod(Class<?> clazz, String name, Predicate<Method> filter, Class<?>... paramTypes) {
        Class<?> searchType = clazz;
        while (searchType != null) {
            Method[] methods = searchType.getDeclaredMethods();
            Method result = null;
            for (Method method : methods) {
                // Private methods can't be mocked so just skip them
                if (!filter.test(method)) {
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
        // Nothing found, our last hope is a default method
        searchType = clazz;
        while (searchType != Object.class) {
            Method method = findDefaultMethod(searchType, name, paramTypes);
            if(method != null) {
                return method;
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    private static Method findDefaultMethod(Class<?> searchedClass, String name, Class<?>[] paramTypes) {
        Class<?>[] interfaces = searchedClass.getInterfaces();
        Method result = null;
        for(Class<?> i : interfaces) {
            Method[] methods = i.getDeclaredMethods();
            for (Method method : methods) {
                if(!method.isDefault()) {
                    continue;
                }
                if (name.equals(method.getName())) {
                    if (paramTypes == null) {
                        if (result != null) {
                            throw new RuntimeException("Ambiguous name: More than one method are named "
                                + name);
                        }
                        result = method; // match, remember it to see if it's ambiguous
                    } else if (Arrays.equals(paramTypes, method.getParameterTypes())) {
                        return method; // perfect match, get out now
                    }
                }
            }
        }
        return result;
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
    public static <T> Constructor<T> getConstructor(Class<T> clazz, Object... objs)
            throws NoSuchMethodException {
        Constructor<T> ret = null;
        for (Constructor<?> classConstructor : clazz.getDeclaredConstructors()) {
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
    private static boolean isMatchingConstructor(Constructor<?> classConstructor, Object... objs) {

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
    public static Method getDeclaredMethod(Class<?> clazz, String name,
            Class<?>... paramTypes) {
        try {
            return clazz.getDeclaredMethod(name, paramTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tells if a class is available in the classpath
     *
     * @param className full class name
     * @return true if the class was found
     */
    public static boolean isClassAvailable(String className) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

}
