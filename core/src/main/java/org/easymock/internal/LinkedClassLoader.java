/**
 * Copyright 2001-2016 the original author or authors.
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

import java.util.Arrays;
import java.util.List;

/**
 * ClassLoader looking in multiple {@code ClassLoader}s in order to find the wanted class. Useful
 * when in an OSGi context where class loaders are organized as a graph.
 */
class LinkedClassLoader extends ClassLoader {

    private final List<ClassLoader> classLoaders;

    public LinkedClassLoader(ClassLoader... classLoaders) {
        this.classLoaders = Arrays.asList(classLoaders);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> clazz = null;
        for (ClassLoader classLoader : classLoaders) {
            try {
                clazz = classLoader.loadClass(name);
            } catch (ClassNotFoundException e) {
                // Retry with next ClassLoader in List
            }
        }
        if (clazz == null) {
            throw new ClassNotFoundException(name);
        }
        return clazz;
    }
}
