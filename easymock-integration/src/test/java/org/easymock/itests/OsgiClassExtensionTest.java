/**
 * Copyright 2009-2011 the original author or authors.
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
package org.easymock.itests;

import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.jar.Manifest;

import org.easymock.EasyMockSupport;
import org.easymock.internal.ClassExtensionHelper;
import org.objenesis.Objenesis;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;
import org.springframework.osgi.util.OsgiStringUtils;

/**
 * Note: This is a JUnit 3 test because of the Spring OSGi test framework
 * 
 * @author Henri Tremblay
 */
public class OsgiClassExtensionTest extends AbstractConfigurableBundleCreatorTests {

    public static abstract class A {
        public abstract void foo();
    }

    private final EasyMockSupport support = new EasyMockSupport();

    public <T> T createMock(final Class<T> toMock) {
        return support.createMock(toMock);
    }

    public void replayAll() {
        support.replayAll();
    }

    public void verifyAll() {
        support.verifyAll();
    }

    private String getImplementationVersion(final Class<?> c) {
        return c.getPackage().getImplementationVersion();
    }

    @Override
    protected String[] getTestBundlesNames() {

        final ClassLoader cl = String.class.getClassLoader();
        final String cglibVersion = "2.2.0";
        final String objenesisVersion = getImplementationVersion(Objenesis.class);
        final String easymockVersion = getImplementationVersion(org.easymock.EasyMock.class);

        return new String[] { "net.sourceforge.cglib, com.springsource.net.sf.cglib, " + cglibVersion,
                "org.easymock, easymock, " + easymockVersion, "org.objenesis, objenesis, " + objenesisVersion };
    }

    @Override
    protected Manifest getManifest() {
        final Manifest mf = super.getManifest();

        String imports = mf.getMainAttributes().getValue(Constants.IMPORT_PACKAGE);

        imports = imports.replace("org.easymock.classextension.internal,",
                "org.easymock.classextension.internal;poweruser=true,");
        imports = imports.replace("org.easymock.internal,", "org.easymock.internal;poweruser=true,");

        imports += ",net.sf.cglib.reflect,net.sf.cglib.core,net.sf.cglib.proxy";

        mf.getMainAttributes().putValue(Constants.IMPORT_PACKAGE, imports);

        return mf;
    }

    public void testOsgiPlatformStarts() throws Exception {
        System.out.println("Framework vendor: " + this.bundleContext.getProperty(Constants.FRAMEWORK_VENDOR));
        System.out.println("Framework version: " + bundleContext.getProperty(Constants.FRAMEWORK_VERSION));
        System.out.println("Require capability: " + bundleContext.getProperty(Constants.REQUIRE_CAPABILITY));
        System.out.println("-----------------------------");

        final Bundle[] bundles = bundleContext.getBundles();
        for (final Bundle bundle : bundles) {
            System.out.println(OsgiStringUtils.nullSafeName(bundle) + ": "
                    + bundle.getHeaders().get(Constants.BUNDLE_VERSION));
        }
    }

    /**
     * Class loaded in the bootstrap class loader have a null class loader. In
     * this case, cglib creates the proxy in its own class loader. So I need to
     * test this case is working
     */
    public void testCanMock_BootstrapClassLoader() {
        final ArrayList<?> mock = createMock(ArrayList.class);
        expect(mock.size()).andReturn(5);
        replayAll();
        assertEquals(5, mock.size());
        verifyAll();
    }

    /**
     * Normal case of a class in this class loader
     */
    public void testCanMock_OtherClassLoader() {
        final A mock = createMock(A.class);
        mock.foo();
        replayAll();
        mock.foo();
        verifyAll();
    }

    public void testCanPartialMock() throws Exception {
        final A mock = createMockBuilder(A.class).withConstructor().addMockedMethod("foo").createMock();

        mock.foo();
        replay(mock);
        mock.foo();
        verify(mock);
    }

    public void testCanUseInternal() {
        final ArrayList<?> mock = createMock(ArrayList.class);
        ClassExtensionHelper.getControl(mock);
    }

}
