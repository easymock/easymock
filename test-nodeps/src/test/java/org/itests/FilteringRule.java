/*
 * Copyright 2015-2024 the original author or authors.
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
package org.itests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author Henri Tremblay
 */
public class FilteringRule implements TestRule {

    private final String[] filteredPackages;

    public FilteringRule(String... filteredPackages) {
        this.filteredPackages = filteredPackages;
    }

    public Statement apply(Statement base, Description description) {
        return new FilteringStatement(base, description, filteredPackages);
    }

}

class FilteringClassLoader extends ClassLoader {

    private static final String[] packagesToBeDeferred = new String[] { "org.hamcrest.", "java.", "jdk.", "sun.",
            "org.junit." };

    private final Collection<String> ignoredPackages;

    private final Map<String, Class<?>> classes = new HashMap<>();

    public FilteringClassLoader(Collection<String> ignoredPackages) {
        this.ignoredPackages = ignoredPackages;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (isIgnored(name)) {
            throw new ClassNotFoundException(name);
        }
        Class<?> clazz = classes.get(name);
        if (clazz != null) {
            return clazz;
        }

        if (shouldBeDeferred(name)) {
            return super.loadClass(name, resolve);
        }

        try {
            clazz = loadClass0(name);
        } catch (IOException e) {
            throw new ClassNotFoundException("Can't load " + name, e);
        }

        if (resolve) {
            resolveClass(clazz);
        }

        classes.put(name, clazz);
        return clazz;
    }

    private boolean shouldBeDeferred(String name) {
        for (String pack : packagesToBeDeferred) {
            if (name.startsWith(pack)) {
                return true;
            }
        }
        return false;
    }

    private Class<?> loadClass0(String name) throws IOException, ClassNotFoundException {
        String path = name.replace('.', '/') + ".class";
        InputStream in = null;
        ByteArrayOutputStream out = null;

        try {
            in = getResourceAsStream(path);
            if (in == null) {
                throw new ClassNotFoundException(name);
            }
            out = new ByteArrayOutputStream();

            int one;
            while ((one = in.read()) != -1) {
                out.write((byte) one);
            }

            out.flush();

            byte[] bytes = out.toByteArray();
            return defineClass(name, bytes, 0, bytes.length);
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    private boolean isIgnored(String name) {
        for (String s : ignoredPackages) {
            if (name.startsWith(s)) {
                return true;
            }
        }
        return false;
    }
}

class FilteringStatement extends Statement {

    private final Description description; // Description of the tested method

    private final String[] filteredPackages;

    public FilteringStatement(Statement base, Description description,
            String[] filteredPackages) {
        this.description = description;
        this.filteredPackages = filteredPackages;
    }

    @Override
    public void evaluate() throws Throwable {
        FilteringClassLoader cl = new FilteringClassLoader(Arrays.asList(filteredPackages));
        Class<?> c = cl.loadClass(description.getTestClass().getName());
        Constructor constructor = c.getConstructor();
        Object test = constructor.newInstance();
        Method m = c.getMethod(description.getMethodName());
        try {
            m.invoke(test);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

}
