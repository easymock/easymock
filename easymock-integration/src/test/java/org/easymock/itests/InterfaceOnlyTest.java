/**
 * Copyright 2009-2013 the original author or authors.
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
import java.util.ArrayList;
import java.util.jar.Manifest;

import org.easymock.MockType;
import org.easymock.internal.MocksControl;
import org.easymock.internal.matchers.Equals;
import org.osgi.framework.Constants;

/**
 * Test that we still can mock interfaces without cglib and objenesis as
 * dependencies
 * 
 * @author Henri Tremblay
 */
public class InterfaceOnlyTest extends OsgiBaseTest {

    @Override
    protected String[] getTestBundlesNames() {

        final String easymockVersion = getEasyMockVersion();

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

    public void testCanMock() throws IOException {
        final Appendable mock = createMock(Appendable.class);
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

    public void testCannotMock() throws Exception {
        try {
            createMock(ArrayList.class);
            fail("Should throw an exception due to a NoClassDefFoundError");
        } catch (final RuntimeException e) {
            assertEquals("Class mocking requires to have cglib and objenesis librairies in the classpath", e
                    .getMessage());
            assertTrue(e.getCause() instanceof NoClassDefFoundError);
        }
    }
}