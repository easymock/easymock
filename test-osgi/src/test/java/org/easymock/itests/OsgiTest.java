/**
 * Copyright 2009-2016 the original author or authors.
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

import org.easymock.MockType;
import org.easymock.internal.MocksControl;
import org.easymock.internal.matchers.Equals;
import org.objenesis.Objenesis;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.springframework.osgi.util.OsgiStringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.jar.Manifest;

import static org.easymock.EasyMock.*;

/**
 * Note: This is a JUnit 3 test because of the Spring OSGi test framework
 *
 * @author Henri Tremblay
 */
public class OsgiTest extends OsgiBaseTest {

    public static abstract class A {
        public abstract void foo();
    }

    @Override
    protected String[] getTestBundlesNames() {
        String objenesisVersion = getImplementationVersion(Objenesis.class);
        String easymockVersion = getEasyMockVersion();

        return new String[] {
            "org.easymock, easymock, " + easymockVersion,
            "org.objenesis, objenesis, " + objenesisVersion
        };
    }

    @Override
    protected Manifest getManifest() {
        Manifest mf = super.getManifest();

        String imports = mf.getMainAttributes().getValue(Constants.IMPORT_PACKAGE);
        imports = imports.replace("org.easymock.internal,", "org.easymock.internal;poweruser=true,");
        imports = imports.replace("org.easymock.internal.matchers,",
                "org.easymock.internal.matchers;poweruser=true,");

        imports += ",org.easymock.cglib.core,org.easymock.cglib.proxy,org.easymock.cglib.reflect,org.easymock.asm";

        mf.getMainAttributes().putValue(Constants.IMPORT_PACKAGE, imports);

        return mf;
    }

    @Override
    public void testOsgiPlatformStarts() throws Exception {
        System.out.println("Framework vendor: " + this.bundleContext.getProperty(Constants.FRAMEWORK_VENDOR));
        System.out.println("Framework version: " + bundleContext.getProperty(Constants.FRAMEWORK_VERSION));
        System.out.println("-----------------------------");

        Bundle[] bundles = bundleContext.getBundles();
        for (Bundle bundle : bundles) {
            System.out.println(OsgiStringUtils.nullSafeName(bundle) + ": "
                    + bundle.getHeaders().get(Constants.BUNDLE_VERSION));
        }
    }

    public void testCanMock() throws IOException {
        Appendable mock = mock(Appendable.class);
        expect(mock.append("test")).andReturn(mock);
        replayAll();
        assertSame(mock, mock.append("test"));
        verifyAll();
    }

    public void testCanUseMatchers() {
        new Equals(new Object());
    }

    public void testCanUseInternal() {
        new MocksControl(MockType.DEFAULT);
    }

    /**
     * Class loaded in the bootstrap class loader have a null class loader. In
     * this case, cglib creates the proxy in its own class loader. So I need to
     * test this case is working
     */
    public void testCanMock_BootstrapClassLoader() {
        ArrayList<?> mock = mock(ArrayList.class);
        expect(mock.size()).andReturn(5);
        replayAll();
        assertEquals(5, mock.size());
        verifyAll();
    }

    /**
     * Normal case of a class in this class loader
     */
    public void testCanMock_OtherClassLoader() {
        A mock = mock(A.class);
        mock.foo();
        replayAll();
        mock.foo();
        verifyAll();
    }

    public void  testCanPartialMock() throws Exception {
        A mock = partialMockBuilder(A.class).withConstructor().addMockedMethod("foo").createMock();

        mock.foo();
        replay(mock);
        mock.foo();
        verify(mock);
    }
}
