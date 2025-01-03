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
package org.easymock.internal.classinfoprovider;

/**
 * Provider to use when classes are not in the JDK. In this case, we create the mock in the package of the mocked class
 * and in it's class loader.
 */
public class DefaultClassInfoProvider implements ClassInfoProvider {
    @Override
    public String classPackage(Class<?> toMock) {
        return  toMock.getPackage().getName() + ".";
    }

    @Override
    public <T> ClassLoader classLoader(Class<T> toMock) {
        return toMock.getClassLoader();
    }

}
