/**
 * Copyright 2009-2010 the original author or authors.
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

import java.io.IOException;
import java.util.jar.Manifest;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.internal.MocksControl;
import org.easymock.internal.MocksControl.MockType;
import org.easymock.internal.matchers.Equals;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;
import org.springframework.osgi.util.OsgiStringUtils;

/**
 * Test that we still can mock interfaces without cglib and objenesis as
 * dependencies
 * 
 * @author Henri Tremblay
 */
public class InterfaceOnlyTest extends AbstractConfigurableBundleCreatorTests {

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

    @Override
    protected String[] getTestBundlesNames() {

        final String easymockVersion = EasyMock.class.getPackage().getImplementationVersion();

        return new String[] { "org.easymock, easymock, " + easymockVersion };
    }

    @Override
    protected Manifest getManifest() {
        final Manifest mf = super.getManifest();

        String imports = mf.getMainAttributes().getValue(Constants.IMPORT_PACKAGE);
        imports = imports.replace("org.easymock.internal,", "org.easymock.internal;poweruser=true,");
        imports = imports.replace("org.easymock.internal.matchers,",
                "org.easymock.internal.matchers;poweruser=true,");

        mf.getMainAttributes().putValue(Constants.IMPORT_PACKAGE, imports);

        return mf;
    }

    public void testOsgiPlatformStarts() throws Exception {
        System.out.println("Framework vendor: " + this.bundleContext.getProperty(Constants.FRAMEWORK_VENDOR));
        System.out.println("Framework version: " + bundleContext.getProperty(Constants.FRAMEWORK_VERSION));
        System.out.println("Execution environment: "
                + bundleContext.getProperty(Constants.FRAMEWORK_EXECUTIONENVIRONMENT));

        System.out.println("-----------------------------");

        final Bundle[] bundles = bundleContext.getBundles();
        for (final Bundle bundle : bundles) {
            System.out.println(OsgiStringUtils.nullSafeName(bundle) + ": "
                    + bundle.getHeaders().get(Constants.BUNDLE_VERSION));
        }
    }

    public void testCanMock() throws IOException {
        final Appendable mock = createMock(Appendable.class);
        expect(mock.append("test")).andReturn(mock);
        replayAll();
        assertSame(mock, mock.append("test"));
        verifyAll();
    }

    public void testCanUseMatchers() {
        final Equals equals = new Equals(new Object());
    }

    public void testCanUseInternal() {
        final MocksControl ctrl = new MocksControl(MockType.DEFAULT);
    }
}