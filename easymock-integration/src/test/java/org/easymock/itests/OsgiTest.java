/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.itests;

import static org.easymock.EasyMock.*;

import java.io.IOException;

import org.easymock.EasyMock;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;
import org.springframework.osgi.util.OsgiStringUtils;

/**
 * Note: This is a JUnit 3 test because of the Spring OSGi test framework
 */
public class OsgiTest extends AbstractConfigurableBundleCreatorTests {

     @Override
    protected String[] getTestBundlesNames() {
        final String version = EasyMock.class.getPackage()
                .getImplementationVersion();

        return new String[] { "org.easymock, easymock, " + version };
    }
    
    public void testOsgiPlatformStarts() throws Exception {
        System.out.println("Framework vendor: "
                + this.bundleContext
                .getProperty(Constants.FRAMEWORK_VENDOR));
        System.out.println("Framework version: "
                + bundleContext
                .getProperty(Constants.FRAMEWORK_VERSION));
        System.out.println("Execution environment: "
                + bundleContext
                .getProperty(Constants.FRAMEWORK_EXECUTIONENVIRONMENT));

        System.out.println("-----------------------------");
        
        Bundle[] bundles = bundleContext.getBundles();
        for (int i = 0; i < bundles.length; i++) {
            System.out.println(OsgiStringUtils.nullSafeName(bundles[i]) + ": "
                    + bundles[i].getHeaders().get(Constants.BUNDLE_VERSION));
        }
    }
    
    public void testCanMock() throws IOException {
        Appendable mock = createMock(Appendable.class);
        expect(mock.append("test")).andReturn(mock);
        replay(mock);
        assertSame(mock, mock.append("test"));
        verify(mock);
    }
}
