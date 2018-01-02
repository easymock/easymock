/**
 * Copyright 2001-2018 the original author or authors.
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

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * JUnit runner used to process {@link Mock} and {@link TestSubject} annotations. Note
 * that this runner only works with JUnit 4.5 or higher
 *
 * @author Henri Tremblay
 * @since 3.2
 */
public class EasyMockRunner extends BlockJUnit4ClassRunner {

    public EasyMockRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    /**
     * We are required to override a deprecated method because it's the only way the perform
     * the mock injection before the {@code @Before} of our class being called. Using a statement
     * wouldn't work.
     *
     * @param method test method class
     * @param target test class instance
     * @param statement current statement
     * @return a statement to return to the caller
     */
    @Override
    protected Statement withBefores(FrameworkMethod method, Object target,
            Statement statement) {
        EasyMockSupport.injectMocks(target);
        return super.withBefores(method, target, statement);
    }
}
