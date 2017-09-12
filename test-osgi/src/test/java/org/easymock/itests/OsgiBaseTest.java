/**
 * Copyright 2009-2017 the original author or authors.
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.MavenUtils;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import javax.inject.Inject;

import static org.ops4j.pax.exam.CoreOptions.*;


/**
 * @author Henri Tremblay
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public abstract class OsgiBaseTest {

    private final EasyMockSupport support = new EasyMockSupport();

    protected <T> T mock(Class<T> toMock) {
        return support.mock(toMock);
    }

    protected void replayAll() {
        support.replayAll();
    }

    protected void verifyAll() {
        support.verifyAll();
    }

    protected String getImplementationVersion(Class<?> c) {
        return c.getPackage().getImplementationVersion();
    }

    @Inject
    private BundleContext bundleContext;

    @Configuration
    public Option[] config() {
        String version = MavenUtils.getArtifactVersion("org.easymock", "easymock");
        return options(
            bundle("file:../core/target/easymock-" + version + ".jar"),
            mavenBundle().groupId("org.objenesis").artifactId("objenesis").versionAsInProject(),
            junitBundles()
        );
    }

    @Test
    public void testOsgiPlatformStarts() throws Exception {
        System.out.println("Framework vendor: " + this.bundleContext.getProperty(Constants.FRAMEWORK_VENDOR));
        System.out.println("Framework version: " + bundleContext.getProperty(Constants.FRAMEWORK_VERSION));
        System.out.println("Require capability: " + bundleContext.getProperty(Constants.REQUIRE_CAPABILITY));
        System.out.println("-----------------------------");

        Bundle[] bundles = bundleContext.getBundles();
        for (Bundle bundle : bundles) {
            System.out.println(bundle + ": " + bundle.getHeaders().get(Constants.BUNDLE_VERSION));
        }
    }
}
