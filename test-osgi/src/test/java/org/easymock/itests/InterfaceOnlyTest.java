/*
 * Copyright 2009-2025 the original author or authors.
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
import org.junit.Test;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.MavenUtils;
import org.ops4j.pax.exam.Option;

import java.io.IOException;
import java.util.ArrayList;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.options;

/**
 * Test that we still can mock interfaces without ByteBuddy and Objenesis as
 * dependencies
 *
 * @author Henri Tremblay
 */
public class InterfaceOnlyTest extends OsgiBaseTest {

    @Configuration
    public Option[] config() {
        String version = MavenUtils.getArtifactVersion("org.easymock", "easymock");
        return options(
            bundle("file:../core/target/easymock-" + version + ".jar"),
            junitBundles()
        );
    }

    @Test
    public void testCanMock() throws IOException {
        Appendable mock = mock(Appendable.class);
        expect(mock.append("test")).andReturn(mock);
        replayAll();
        assertSame(mock, mock.append("test"));
        verifyAll();
    }

    @Test
    public void testCanUseMatchers() {
        new Equals(new Object());
    }

    @Test
    public void testCanUseInternal() {
        new MocksControl(MockType.DEFAULT);
    }

    @Test
    public void testCannotMock() {
        // Do not use assertThrows, Pax-exam doesn't like it
        try {
            mock(ArrayList.class);
            fail("Should throw an exception due to a NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            // a class from bytebuddy will be missing, no need to check which one
        }
    }
}
