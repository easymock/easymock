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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Contains properties used by EasyMock to change its default behavior. The
 * loading order is (any step being able to overload the properties of the
 * previous step):
 * <ul>
 * <li>easymock.properties in classpath default package</li>
 * <li>explicit call to setProperty</li>
 * </ul>
 * 
 * @author Henri Tremblay
 */
public final class EasyMockProperties {

    private static final String PREFIX = "easymock.";

    // volatile for double-checked locking
    private static volatile EasyMockProperties instance;

    private final Properties properties = new Properties();

    public static EasyMockProperties getInstance() {
        if (instance == null) {
            synchronized (EasyMockProperties.class) {
                // ///CLOVER:OFF
                if (instance == null) {
                    // ///CLOVER:ON
                    instance = new EasyMockProperties();
                }
            }
        }
        return instance;
    }

    private EasyMockProperties() {
        loadEasyMockProperties("easymock.properties");
    }

    private void loadEasyMockProperties(final String propertyFileName) {
        InputStream in = getClassLoader().getResourceAsStream(propertyFileName);
        if (in != null) {
            in = new BufferedInputStream(in);
            try {
                properties.load(in);
            } catch (final IOException e) {
                throw new RuntimeException("Failed to read " + propertyFileName + " file");
            } finally {
                try {
                    in.close();
                } catch (final IOException e) {
                    // Doesn't matter
                }
            }
        }
    }

    /**
     * Searches for the property with the specified key. If the key is not
     * found, return the default value.
     * 
     * @param key
     *            key leading to the property
     * @param defaultValue
     *            the value to be returned if the key isn't found
     * @return the value found for the key or the default value
     */
    public String getProperty(final String key, final String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Searches for the property with the specified key. Return null if the key
     * is not found.
     * 
     * @param key
     *            key leading to the property
     * @return the value found for the key or null
     */
    public String getProperty(final String key) {
        return properties.getProperty(key);
    }

    /**
     * Add a value referenced by the provided key. A null value will remove the
     * key
     * 
     * @param key
     *            the key of the new property
     * @param value
     *            the value corresponding to <tt>key</tt>.
     * @return the property previous value
     */
    public String setProperty(final String key, final String value) {
        if (!key.startsWith(PREFIX)) {
            throw new IllegalArgumentException("Invalid key (" + key
                    + "), an easymock property starts with \"" + PREFIX + "\"");
        }
        if (value == null) {
            return (String) properties.remove(key);
        }
        return (String) properties.setProperty(key, value);
    }

    private ClassLoader getClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (final Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system
            // class loader
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = getClass().getClassLoader();
        }
        return cl;
    }
}
