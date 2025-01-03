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
package org.easymock;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Class wrapping arguments to create a partial class mock that gets
 * instantiated by calling one of its constructors.
 *
 * @author Henri Tremblay
 */
public final class ConstructorArgs {

    private final Constructor<?> constructor;

    private final Object[] initArgs;

    /**
     * @param constructor
     *            Constructor to be called when creating the mock
     * @param initArgs
     *            Arguments passed to the constructor
     */
    public ConstructorArgs(Constructor<?> constructor, Object... initArgs) {
        this.constructor = Objects.requireNonNull(constructor);
        this.initArgs = validateArgs(constructor, initArgs);
    }

    private static Object[] validateArgs(Constructor<?> constructor, Object[] initArgs) {

        Class<?>[] paramTypes = constructor.getParameterTypes();

        if (initArgs.length != paramTypes.length) {
            throw new IllegalArgumentException("Number of provided arguments doesn't match constructor ones");
        }

        for (int i = 0; i < initArgs.length; i++) {

            Class<?> paramType = paramTypes[i];
            Object arg = initArgs[i];

            if (paramType.isPrimitive()) {
                if (arg == null) {
                    throw new IllegalArgumentException("Null argument for primitive param " + i);
                }

                try {
                    Field field = arg.getClass().getDeclaredField("TYPE");
                    Class<?> argType = (Class<?>) field.get(null);

                    if (paramType.equals(argType)) {
                        continue;
                    }
                } catch (Exception e) {
                    throw throwException(paramType, arg);
                }

                throw throwException(paramType, arg);
            }
            if (arg == null) {
                continue;
            }
            if (!paramType.isAssignableFrom(arg.getClass())) {
                throw throwException(paramType, arg);
            }
        }

        return initArgs;
    }

    private static IllegalArgumentException throwException(Class<?> paramType, Object arg) {
        return new IllegalArgumentException(arg + " isn't of type " + paramType);
    }

    /**
     * @return arguments to be passed to the constructor
     */
    public Object[] getInitArgs() {
        return initArgs;
    }

    /**
     * @return constructor to be called
     */
    public Constructor<?> getConstructor() {
        return constructor;
    }
}
