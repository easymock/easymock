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

import java.lang.reflect.*;
import java.util.*;

/**
 * Code taken from the <a href="http://www.springframework.org">Spring
 * framework</a>.
 * 
 * Helper for resolving synthetic {@link Method#isBridge bridge Methods} to the
 * {@link Method} being bridged.
 * 
 * <p>
 * Given a synthetic {@link Method#isBridge bridge Method} returns the
 * {@link Method} being bridged. A bridge method may be created by the compiler
 * when extending a parameterized type whose methods have parameterized
 * arguments. During runtime invocation the bridge {@link Method} may be invoked
 * and/or used via reflection. When attempting to locate annotations on
 * {@link Method Methods}, it is wise to check for bridge {@link Method Methods}
 * as appropriate and find the bridged {@link Method}.
 * 
 * <p>
 * See <a href="http://java.sun.com/docs/books/jls/third_edition/html/expressions.html#15.12.4.5"
 * > The Java Language Specification</a> for more details on the use of bridge
 * methods.
 * 
 * @author Rob Harrop
 * @author Juergen Hoeller
 */
public final class BridgeMethodResolver {

    // Hard to test all cases since bridges varies from JVM implementation
    // Plus, the code is taken from Spring so we consider it is working
    // So, don't check coverage over the class

    // ///CLOVER:OFF
    private BridgeMethodResolver() {
    }

    /**
     * Find the original method for the supplied {@link Method bridge Method}.
     * <p>
     * It is safe to call this method passing in a non-bridge {@link Method}
     * instance. In such a case, the supplied {@link Method} instance is
     * returned directly to the caller. Callers are <strong>not</strong>
     * required to check for bridging before calling this method.
     * 
     * @param bridgeMethod
     *            the bridge method
     * @return the original method for the bridge
     * @throws IllegalStateException
     *             if no bridged {@link Method} can be found
     */
    public static Method findBridgedMethod(final Method bridgeMethod) {
        assert bridgeMethod != null : "Method must not be null";

        if (!bridgeMethod.isBridge()) {
            return bridgeMethod;
        }

        // Gather all methods with matching name and parameter size.
        final List<Method> candidateMethods = new ArrayList<Method>();
        final Method[] methods = getAllDeclaredMethods(bridgeMethod.getDeclaringClass());
        for (final Method candidateMethod : methods) {
            if (isBridgedCandidateFor(candidateMethod, bridgeMethod)) {
                candidateMethods.add(candidateMethod);
            }
        }

        Method result;
        // Now perform simple quick checks.
        if (candidateMethods.size() == 1) {
            result = candidateMethods.get(0);
        } else {
            result = searchCandidates(candidateMethods, bridgeMethod);
        }

        if (result == null) {
            throw new IllegalStateException("Unable to locate bridged method for bridge method '"
                    + bridgeMethod + "'");
        }

        return result;
    }

    /**
     * Search for the bridged method in the given candidates.
     * 
     * @param candidateMethods
     *            the List of candidate Methods
     * @param bridgeMethod
     *            the bridge method
     * @return the bridged method, or <code>null</code> if none found
     */
    private static Method searchCandidates(final List<Method> candidateMethods, final Method bridgeMethod) {
        final Map<TypeVariable<?>, Type> typeParameterMap = createTypeVariableMap(bridgeMethod
                .getDeclaringClass());
        for (int i = 0; i < candidateMethods.size(); i++) {
            final Method candidateMethod = candidateMethods.get(i);
            if (isBridgeMethodFor(bridgeMethod, candidateMethod, typeParameterMap)) {
                return candidateMethod;
            }
        }
        return null;
    }

    /**
     * Return <code>true</code> if the supplied '<code>candidateMethod</code>'
     * can be consider a validate candidate for the {@link Method} that is
     * {@link Method#isBridge() bridged} by the supplied {@link Method bridge
     * Method}. This method performs inexpensive checks and can be used quickly
     * filter for a set of possible matches.
     */
    private static boolean isBridgedCandidateFor(final Method candidateMethod, final Method bridgeMethod) {
        return (!candidateMethod.isBridge() && !candidateMethod.equals(bridgeMethod)
                && candidateMethod.getName().equals(bridgeMethod.getName()) && candidateMethod
                .getParameterTypes().length == bridgeMethod.getParameterTypes().length);
    }

    /**
     * Determine whether or not the bridge {@link Method} is the bridge for the
     * supplied candidate {@link Method}.
     */
    private static boolean isBridgeMethodFor(final Method bridgeMethod, final Method candidateMethod,
            final Map<TypeVariable<?>, Type> typeVariableMap) {
        if (isResolvedTypeMatch(candidateMethod, bridgeMethod, typeVariableMap)) {
            return true;
        }
        final Method method = findGenericDeclaration(bridgeMethod);
        return (method != null ? isResolvedTypeMatch(method, candidateMethod, typeVariableMap) : false);
    }

    /**
     * Search for the generic {@link Method} declaration whose erased signature
     * matches that of the supplied bridge method.
     * 
     * @throws IllegalStateException
     *             if the generic declaration cannot be found
     */
    private static Method findGenericDeclaration(final Method bridgeMethod) {
        // Search parent types for method that has same signature as bridge.
        Class<?> superclass = bridgeMethod.getDeclaringClass().getSuperclass();
        while (!Object.class.equals(superclass)) {
            final Method method = searchForMatch(superclass, bridgeMethod);
            if (method != null && !method.isBridge()) {
                return method;
            }
            superclass = superclass.getSuperclass();
        }

        // Search interfaces.
        final Class<?>[] interfaces = getAllInterfacesForClass(bridgeMethod.getDeclaringClass());
        for (final Class<?> anInterface : interfaces) {
            final Method method = searchForMatch(anInterface, bridgeMethod);
            if (method != null && !method.isBridge()) {
                return method;
            }
        }

        return null;
    }

    /**
     * Return <code>true</code> if the {@link Type} signature of both the
     * supplied {@link Method#getGenericParameterTypes() generic Method} and
     * concrete {@link Method} are equal after resolving all
     * {@link TypeVariable TypeVariables} using the supplied
     * {@link #createTypeVariableMap TypeVariable Map}, otherwise returns
     * <code>false</code>.
     */
    private static boolean isResolvedTypeMatch(final Method genericMethod, final Method candidateMethod,
            final Map<TypeVariable<?>, Type> typeVariableMap) {
        final Type[] genericParameters = genericMethod.getGenericParameterTypes();
        final Class<?>[] candidateParameters = candidateMethod.getParameterTypes();
        if (genericParameters.length != candidateParameters.length) {
            return false;
        }
        for (int i = 0; i < genericParameters.length; i++) {
            final Type genericParameter = genericParameters[i];
            final Class<?> candidateParameter = candidateParameters[i];
            if (candidateParameter.isArray()) {
                // An array type: compare the component type.
                final Type rawType = getRawType(genericParameter, typeVariableMap);
                if (rawType instanceof GenericArrayType) {
                    if (!candidateParameter.getComponentType().equals(
                            getRawType(((GenericArrayType) rawType).getGenericComponentType(),
                                    typeVariableMap))) {
                        return false;
                    }
                    break;
                }
            }
            // A non-array type: compare the type itself.
            if (!candidateParameter.equals(getRawType(genericParameter, typeVariableMap))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determine the raw type for the given generic parameter type.
     */
    private static Type getRawType(final Type genericType, final Map<TypeVariable<?>, Type> typeVariableMap) {
        if (genericType instanceof TypeVariable<?>) {
            final TypeVariable<?> tv = (TypeVariable<?>) genericType;
            final Type result = typeVariableMap.get(tv);
            return (result != null ? result : Object.class);
        } else if (genericType instanceof ParameterizedType) {
            return ((ParameterizedType) genericType).getRawType();
        } else {
            return genericType;
        }
    }

    /**
     * If the supplied {@link Class} has a declared {@link Method} whose
     * signature matches that of the supplied {@link Method}, then this matching
     * {@link Method} is returned, otherwise <code>null</code> is returned.
     */
    private static Method searchForMatch(final Class<?> type, final Method bridgeMethod) {
        return findMethod(type, bridgeMethod.getName(), bridgeMethod.getParameterTypes());
    }

    /**
     * Build a mapping of {@link TypeVariable#getName TypeVariable names} to
     * concrete {@link Class} for the specified {@link Class}. Searches all
     * super types, enclosing types and interfaces.
     */
    private static Map<TypeVariable<?>, Type> createTypeVariableMap(final Class<?> cls) {
        final Map<TypeVariable<?>, Type> typeVariableMap = new HashMap<TypeVariable<?>, Type>();

        // interfaces
        extractTypeVariablesFromGenericInterfaces(cls.getGenericInterfaces(), typeVariableMap);

        // super class
        Type genericType = cls.getGenericSuperclass();
        Class<?> type = cls.getSuperclass();
        while (!Object.class.equals(type)) {
            if (genericType instanceof ParameterizedType) {
                final ParameterizedType pt = (ParameterizedType) genericType;
                populateTypeMapFromParameterizedType(pt, typeVariableMap);
            }
            extractTypeVariablesFromGenericInterfaces(type.getGenericInterfaces(), typeVariableMap);
            genericType = type.getGenericSuperclass();
            type = type.getSuperclass();
        }

        // enclosing class
        type = cls;
        while (type.isMemberClass()) {
            genericType = type.getGenericSuperclass();
            if (genericType instanceof ParameterizedType) {
                final ParameterizedType pt = (ParameterizedType) genericType;
                populateTypeMapFromParameterizedType(pt, typeVariableMap);
            }
            type = type.getEnclosingClass();
        }

        return typeVariableMap;
    }

    private static void extractTypeVariablesFromGenericInterfaces(final Type[] genericInterfaces,
            final Map<TypeVariable<?>, Type> typeVariableMap) {
        for (final Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType) {
                final ParameterizedType pt = (ParameterizedType) genericInterface;
                populateTypeMapFromParameterizedType(pt, typeVariableMap);
                if (pt.getRawType() instanceof Class<?>) {
                    extractTypeVariablesFromGenericInterfaces(((Class<?>) pt.getRawType())
                            .getGenericInterfaces(), typeVariableMap);
                }
            } else if (genericInterface instanceof Class<?>) {
                extractTypeVariablesFromGenericInterfaces(((Class<?>) genericInterface)
                        .getGenericInterfaces(), typeVariableMap);
            }
        }
    }

    /**
     * Read the {@link TypeVariable TypeVariables} from the supplied
     * {@link ParameterizedType} and add mappings corresponding to the
     * {@link TypeVariable#getName TypeVariable name} -> concrete type to the
     * supplied {@link Map}.
     * <p>
     * Consider this case:
     * 
     * <pre class="code>
     * public interface Foo&lt;S, T&gt; {
     *  ..
     * }
     * 
     * public class FooImpl implements Foo&lt;String, Integer&gt; {
     *  ..
     * }
     * </pre>
     * 
     * For '<code>FooImpl</code>' the following mappings would be added to the
     * {@link Map}: {S=java.lang.String, T=java.lang.Integer}.
     */
    private static void populateTypeMapFromParameterizedType(final ParameterizedType type,
            final Map<TypeVariable<?>, Type> typeVariableMap) {
        if (type.getRawType() instanceof Class<?>) {
            final Type[] actualTypeArguments = type.getActualTypeArguments();
            final TypeVariable<?>[] typeVariables = ((Class<?>) type.getRawType()).getTypeParameters();
            for (int i = 0; i < actualTypeArguments.length; i++) {
                final Type actualTypeArgument = actualTypeArguments[i];
                final TypeVariable<?> variable = typeVariables[i];
                if (actualTypeArgument instanceof Class<?>) {
                    typeVariableMap.put(variable, actualTypeArgument);
                } else if (actualTypeArgument instanceof GenericArrayType) {
                    typeVariableMap.put(variable, actualTypeArgument);
                } else if (actualTypeArgument instanceof ParameterizedType) {
                    typeVariableMap.put(variable, ((ParameterizedType) actualTypeArgument).getRawType());
                } else if (actualTypeArgument instanceof TypeVariable<?>) {
                    // We have a type that is parameterized at instantiation time
                    // the nearest match on the bridge method will be the bounded type.
                    final TypeVariable<?> typeVariableArgument = (TypeVariable<?>) actualTypeArgument;
                    Type resolvedType = typeVariableMap.get(typeVariableArgument);
                    if (resolvedType == null) {
                        resolvedType = extractClassForTypeVariable(typeVariableArgument);
                    }
                    if (resolvedType != null) {
                        typeVariableMap.put(variable, resolvedType);
                    }
                }
            }
        }
    }

    /**
     * Extracts the bound '<code>Class</code>' for a give {@link TypeVariable}.
     */
    private static Class<?> extractClassForTypeVariable(final TypeVariable<?> typeVariable) {
        final Type[] bounds = typeVariable.getBounds();
        Type result = null;
        if (bounds.length > 0) {
            final Type bound = bounds[0];
            if (bound instanceof ParameterizedType) {
                result = ((ParameterizedType) bound).getRawType();
            } else if (bound instanceof Class<?>) {
                result = bound;
            } else if (bound instanceof TypeVariable<?>) {
                result = extractClassForTypeVariable((TypeVariable<?>) bound);
            }
        }
        return (result instanceof Class<?> ? (Class<?>) result : null);
    }

    /**
     * Return all interfaces that the given class implements as array, including
     * ones implemented by superclasses.
     * <p>
     * If the class itself is an interface, it gets returned as sole interface.
     * 
     * @param clazz
     *            the class to analyse for interfaces
     * @return all interfaces that the given object implements as array
     */
    private static Class<?>[] getAllInterfacesForClass(Class<?> clazz) {
        assert clazz != null : "Class must not be null";
        if (clazz.isInterface()) {
            return new Class[] { clazz };
        }
        final List<Class<?>> interfaces = new ArrayList<Class<?>>();
        while (clazz != null) {
            for (int i = 0; i < clazz.getInterfaces().length; i++) {
                final Class<?> ifc = clazz.getInterfaces()[i];
                if (!interfaces.contains(ifc)) {
                    interfaces.add(ifc);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return interfaces.toArray(new Class[interfaces.size()]);
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
     *            the parameter types of the method
     * @return the Method object, or <code>null</code> if none found
     */
    private static Method findMethod(final Class<?> clazz, final String name, final Class<?>[] paramTypes) {
        assert clazz != null : "Class must not be null";
        assert name != null : "Method name must not be null";
        Class<?> searchType = clazz;
        while (!Object.class.equals(searchType) && searchType != null) {
            final Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType
                    .getDeclaredMethods());
            for (final Method method : methods) {
                if (name.equals(method.getName()) && Arrays.equals(paramTypes, method.getParameterTypes())) {
                    return method;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    /**
     * Get all declared methods on the leaf class and all superclasses. Leaf
     * class methods are included first.
     */
    private static Method[] getAllDeclaredMethods(Class<?> leafClass) {
        final List<Method> list = new LinkedList<Method>();

        // Keep backing up the inheritance hierarchy.
        do {
            final Method[] methods = leafClass.getDeclaredMethods();
            for (final Method method : methods) {
                list.add(method);
            }
            leafClass = leafClass.getSuperclass();
        } while (leafClass != null);

        return list.toArray(new Method[list.size()]);
    }
    // ///CLOVER:ON
}
