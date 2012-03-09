/**
 * Copyright 2009-2012 the original author or authors.
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

import org.easymock.EasyMockSupport;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;
import org.springframework.osgi.util.OsgiStringUtils;

/**
 * Note: This is a JUnit 3 test because of the Spring OSGi test framework
 * 
 * @author Henri Tremblay
 */
public abstract class OsgiBaseTest extends AbstractConfigurableBundleCreatorTests {

    private final EasyMockSupport support = new EasyMockSupport();

    protected <T> T createMock(final Class<T> toMock) {
        return support.createMock(toMock);
    }

    protected void replayAll() {
        support.replayAll();
    }

    protected void verifyAll() {
        support.verifyAll();
    }

    protected String getImplementationVersion(final Class<?> c) {
        return c.getPackage().getImplementationVersion();
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
}
