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
import org.junit.Ignore;
import org.junit.Test;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.MavenUtils;
import org.ops4j.pax.exam.Option;

import java.io.IOException;
import java.util.ArrayList;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

/**
 * Note: This is a JUnit 3 test because of the Spring OSGi test framework
 *
 * @author Henri Tremblay
 */
public class OsgiTest extends OsgiBaseTest {

    public static abstract class A {
        public abstract void foo();
    }

    @Configuration
    public Option[] config() {
        String version = MavenUtils.getArtifactVersion("org.easymock", "easymock");
        return options(
            bundle("file:../core/target/easymock-" + version + ".jar"),
            mavenBundle().groupId("org.objenesis").artifactId("objenesis").versionAsInProject(),
            mavenBundle().groupId("net.bytebuddy").artifactId("byte-buddy").versionAsInProject(),
            mavenBundle().groupId("org.ow2.asm").artifactId("asm").versionAsInProject(),
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

    /**
     * Class loaded in the bootstrap class loader have a null class loader. In
     * this case, cglib creates the proxy in its own class loader. So I need to
     * test this case is working
     */
    @Ignore("Doesn't work above Java 8 yet")
    @Test
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
    @Ignore("Doesn't work above Java 8 yet")
    @Test
    public void testCanMock_OtherClassLoader() {
        A mock = mock(A.class);
        mock.foo();
        replayAll();
        mock.foo();
        verifyAll();
    }

    @Ignore("Doesn't work above Java 8 yet")
    @Test
    public void  testCanPartialMock() {
        A mock = partialMockBuilder(A.class).withConstructor().addMockedMethod("foo").createMock();

        mock.foo();
        replay(mock);
        mock.foo();
        verify(mock);
    }
}
