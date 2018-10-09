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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * @author Henri Tremblay
 */
public class LinkedClassLoaderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void findClass() throws Exception {
        OneClassLoader stringLoader = new OneClassLoader(String.class.getName(), getClass().getClassLoader());
        OneClassLoader testLoader = new OneClassLoader(getClass().getName(), getClass().getClassLoader());
        LinkedClassLoader classLoader = new LinkedClassLoader(stringLoader, testLoader);
        assertSame(String.class, classLoader.findClass(String.class.getName()));
        assertSame(getClass(), classLoader.findClass(getClass().getName()));

        expectedException.expect(ClassNotFoundException.class);
        classLoader.findClass(Test.class.getName());
    }

}

class OneClassLoader extends ClassLoader {

    private final String className;

    public OneClassLoader(String className, ClassLoader parent) {
        super(parent);
        this.className = className;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (className.equals(name)) {
            return super.loadClass(name, resolve);
        }
        throw new ClassNotFoundException(name);
    }
}
