/*
 * Copyright 2001-2026 the original author or authors.
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

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.modifier.SyntheticState;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.easymock.internal.classinfoprovider.ClassInfoProvider;
import org.easymock.internal.classinfoprovider.JdkClassInfoProvider;
import org.easymock.mocks.MocksPackageLookup;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicInteger;

public class ByteBuddyMocker {
    private static final AtomicInteger id = new AtomicInteger(0);

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();
    private static final MethodHandle privateLookupIn = getPrivateLookupIn();

    private static MethodHandle getPrivateLookupIn() {
        // the method appeared in Java 9, so I call it by reflection to compile in Java 8
        MethodType type = MethodType.methodType(MethodHandles.Lookup.class, Class.class, MethodHandles.Lookup.class);
        try {
            return lookup.findStatic(MethodHandles.class, "privateLookupIn", type);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            // we are before Java 9, so we cannot use privateLookupIn
            return null;
        }
    }

    public static <T> Class<?> generateMockedClass(Class<T> toMock, ClassLoader classLoader, ClassInfoProvider provider) {
        ElementMatcher.Junction<MethodDescription> junction = ElementMatchers.any();
        try (DynamicType.Unloaded<T> unloaded = new ByteBuddy()
                .subclass(toMock)
                .name(provider.classPackage(toMock) + toMock.getSimpleName() + "$$$EasyMock$" + id.incrementAndGet())
                .defineField(ClassProxyFactory.CALLBACK_FIELD, ClassMockingData.class, SyntheticState.SYNTHETIC, Visibility.PUBLIC)
                .method(junction)
                .intercept(MethodDelegation.to(ClassProxyFactory.MockMethodInterceptor.class))
                .make()) {
            return unloaded
                    .load(classLoader, classLoadingStrategy(provider, toMock))
                    .getLoaded();
        }
    }

    @IgnoreAnimalSniffer // privateLookupIn is Java 9+
    private static ClassLoadingStrategy<ClassLoader> classLoadingStrategy(ClassInfoProvider provider, Class<?> toMock) {
        if (ClassInjector.UsingUnsafe.isAvailable()) {
            return new ClassLoadingStrategy.ForUnsafeInjection();
        }
        // Fallback for Java 26+: ByteBuddy disables Unsafe by default; use Lookup.defineClass instead.
        // Lookup.defineClass requires the lookup to reside in the same package as the class being defined.
        if (provider instanceof JdkClassInfoProvider) {
            // Mock will be placed in org.easymock.mocks — use a lookup rooted in that package.
            return ClassLoadingStrategy.UsingLookup.of(MocksPackageLookup.LOOKUP);
        }
        // Mock will be placed in toMock's own package — privateLookupIn grants the required access
        // as long as toMock's module opens the package (unnamed modules are always open).
        try {
            MethodHandles.Lookup privateLookup = (MethodHandles.Lookup) privateLookupIn.invoke(toMock, lookup);
            return ClassLoadingStrategy.UsingLookup.of(privateLookup);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("privateLookupIn not available (requires Java 9+)", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot acquire private lookup for " + toMock
                + ": the module does not open its package. "
                + "Add --add-opens <module>/<package>=org.easymock to the JVM arguments.", e.getCause());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot access privateLookupIn method", e);
        } catch (Error | RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
