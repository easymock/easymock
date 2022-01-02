/*
 * Copyright 2001-2022 the original author or authors.
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
package org.easymock;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

/**
 * JUnit 5 replaced the previous {@link org.junit.runner.RunWith} annotation
 * (which made use of {@link EasyMockRunner}) with the new
 * {@link org.junit.jupiter.api.extension.ExtendWith} annotation. @ExtendWith
 * allows for multiple extensions to be mixed and used together. This is only
 * relevant for JUnit 5.
 *
 * This is retrieved from https://stackoverflow.com/a/47243856/2152081
 * StackOverflow answer provided by https://stackoverflow.com/users/4126968/eee
 */
public class EasyMockExtension implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        EasyMockSupport.injectMocks(testInstance);
    }
}
