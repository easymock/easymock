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

import java.io.File;
import java.lang.reflect.Method;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;
import org.springframework.osgi.test.AbstractOsgiTests;
import org.springframework.osgi.util.OsgiStringUtils;
import org.w3c.dom.Document;

/**
 * Note: This is a JUnit 3 test because of the Spring OSGi test framework
 * 
 * @author Henri Tremblay
 */
public abstract class OsgiBaseTest extends AbstractConfigurableBundleCreatorTests {

    private final XPathFactory xPathFactory = XPathFactory.newInstance();

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

    @Override
    public void runBare() throws Throwable {
        // Since we are changing the bundles between the tests and that Spring is keeping a cache
        // of the OSGi platform once it is initialized, I'm using a secret method to shutdown
        // the platform between each test (this however slows down the tests so adding a wiser
        // cache is a good idea
        final Method m = AbstractOsgiTests.class.getDeclaredMethod("shutdownTest");
        m.setAccessible(true);
        m.invoke(this);
        super.runBare();
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

    protected String getEasyMockVersion() {
        String version = getImplementationVersion(EasyMock.class);
        // Null means we are an IDE, not in Maven. So we have an IDE project dependency instead
        // of a Maven dependency with the jar. Which explains why the version is null (no Manifest
        // since there's no jar. In that case we get the version from the pom.xml and hope the Maven
        // jar is up-to-date in the local repository
        if (version == null) {
            try {
                final XPath xPath = xPathFactory.newXPath();
                XPathExpression xPathExpression;
                try {
                    xPathExpression = xPath.compile("/project/parent/version");
                } catch (final XPathExpressionException e) {
                    throw new RuntimeException(e);
                }

                final DocumentBuilderFactory xmlFact = DocumentBuilderFactory.newInstance();
                xmlFact.setNamespaceAware(false);
                final DocumentBuilder builder = xmlFact.newDocumentBuilder();
                final Document doc = builder.parse(new File("pom.xml"));
                version = xPathExpression.evaluate(doc);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
        return version;
    }
}
