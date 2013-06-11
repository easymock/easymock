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
package org.easymock.tests2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import org.easymock.EasyMock;
import org.easymock.internal.EasyMockProperties;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Henri Tremblay
 */
public class EasyMockPropertiesTest {

    private static final String NORMAL_PATH = "target" + File.separatorChar + "test-classes";
    private static final String CLOVER_PATH = "target" + File.separatorChar + "clover" + File.separatorChar + "test-classes";
    private static final File PROPERTIES_FILE = new File(isCloverBuild() ? CLOVER_PATH : NORMAL_PATH, "easymock.properties");

    /**
     * Check that the test execution isn't ran by Clover. Because it it's the case, we need to generate the easymock.properties
     * file in a different directory
     */
    private static boolean isCloverBuild() {
        String surefireClasspath = System.getProperty("surefire.test.class.path");
        if(surefireClasspath != null) {
            return surefireClasspath.contains(CLOVER_PATH);
        }
        return false;
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        // Create an EasyMock property file for the test
        final BufferedWriter out = new BufferedWriter(new FileWriter(PROPERTIES_FILE));
        out.write(EasyMock.ENABLE_THREAD_SAFETY_CHECK_BY_DEFAULT + "=" + true);
        out.newLine();
        out.write("easymock.a=1");
        out.newLine();
        out.write("easymock.b=2");
        out.newLine();
        out.write(EasyMock.NOT_THREAD_SAFE_BY_DEFAULT + "=" + true);
        out.newLine();
        out.close();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        // Cleanup the mess
        PROPERTIES_FILE.delete();

        // Make sure we reset all our mess at the end
        resetInstance();
    }

    @Before
    public void setup() throws Exception {
        // Make sure to reset to prevent getting an already initialized
        // EasyMockProperties
        resetInstance();

        // Set manually a new one
        setEasyMockProperty("easymock.e", "7");

        // Set manually an old one
        setEasyMockProperty("easymock.c", "8");

        // Overload after (will be ignored)
        System.setProperty("easymock.h", "4");
    }

    @Test
    public void testGetInstance() throws Exception {
        assertExpectedValue("1", "easymock.a");
        assertExpectedValue("8", "easymock.c");
        assertExpectedValue("7", "easymock.e");
        assertExpectedValue(null, "easymock.g");
        assertExpectedValue(null, "easymock.h");
        assertExpectedValue(null, "xxx.yyy");

        assertExpectedValue(Boolean.TRUE.toString(), EasyMock.NOT_THREAD_SAFE_BY_DEFAULT);
        assertExpectedValue(null, EasyMock.DISABLE_CLASS_MOCKING);
    }

    @Test
    public void testGetProperty() {
        final EasyMockProperties instance = EasyMockProperties.getInstance();

        // use the default
        assertEquals("1", instance.getProperty("easymock.a", "10"));
        // don't use the default
        assertEquals("10", instance.getProperty("easymock.z", "10"));
        // null default
        assertNull(instance.getProperty("easymock.z", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProperty() {
        final EasyMockProperties instance = EasyMockProperties.getInstance();

        instance.setProperty("tralala.a", null);
    }

    @Test
    public void testNoThreadContextClassLoader() throws Exception {
        final ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            resetInstance();

            // Remove the context class loader
            Thread.currentThread().setContextClassLoader(null);

            // This instance will load easymock.properties from the
            // EasyMockProperties class loader
            EasyMockProperties.getInstance();

            // And so "easymock.a" should be there
            assertExpectedValue("1", "easymock.a");

        } finally {
            // Whatever happens, set the initial class loader back or it'll get
            // messy
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Test
    public void testBadPropertiesFile() throws Exception {

        final Boolean[] close = new Boolean[1];

        // A ClassLoader that returns no easymock.properties
        final ClassLoader cl = new ClassLoader(getClass().getClassLoader()) {

            @Override
            public InputStream getResourceAsStream(final String name) {
                if ("easymock.properties".equals(name)) {
                    return new InputStream() {
                        @Override
                        public void close() throws IOException {
                            close[0] = Boolean.TRUE;
                        }

                        @Override
                        public int read(final byte[] b, final int off, final int len) throws IOException {
                            throw new IOException("Failed!");
                        }

                        @Override
                        public int read(final byte[] b) throws IOException {
                            throw new IOException("Failed!");
                        }

                        @Override
                        public int read() throws IOException {
                            throw new IOException("Failed!");
                        }
                    };
                }
                return super.getResourceAsStream(name);
            }

        };
        final ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            resetInstance();

            // Remove the context class loader
            Thread.currentThread().setContextClassLoader(cl);

            try {
                EasyMockProperties.getInstance();
                fail("Should have an issue loading the easymock.properties file");
            } catch (final RuntimeException e) {
                assertEquals("Failed to read easymock.properties file", e.getMessage());
                // Make sure the thread was closed
                assertSame(Boolean.TRUE, close[0]);
            }

        } finally {
            // Whatever happens, set the initial class loader back or it'll get
            // messy
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Test
    public void testNoEasymockPropertiesFile() throws Exception {
        // A ClassLoader that returns no easymock.properties
        final ClassLoader cl = new ClassLoader(getClass().getClassLoader()) {

            @Override
            public InputStream getResourceAsStream(final String name) {
                if ("easymock.properties".equals(name)) {
                    return null;
                }
                return super.getResourceAsStream(name);
            }

        };
        final ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            resetInstance();

            // Set our class loader
            Thread.currentThread().setContextClassLoader(cl);

            // This instance will try to load easymock.properties with our
            // custom class loader and so won't find it
            EasyMockProperties.getInstance();

            // And so it shouldn't find "easymock.a"
            assertExpectedValue(null, "easymock.a");

        } finally {
            // Whatever happens, set the initial class loader back or it'll get
            // messy
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    private static void resetInstance() throws NoSuchFieldException, IllegalAccessException {
        // Cheat and make the singleton uninitialized
        final Field field = EasyMockProperties.class.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, null);
    }

    private static void assertExpectedValue(final String expected, final String key) {
        assertEquals(expected, getEasyMockProperty(key));
    }
}
