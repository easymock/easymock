/**
 * Copyright 2001-2013 the original author or authors.
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
 * JUnit runner used to process {@link Mock} and {@link TestSubject} annotations
 * 
 * @author Henri Tremblay
 * @since 3.2
 */
public class EasyMockRunner extends BlockJUnit4ClassRunner {

    public EasyMockRunner(final Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Statement methodInvoker(final FrameworkMethod method, final Object test) {
        return new EasyMockStatement(super.methodInvoker(method, test), test);
    }

}

class EasyMockStatement extends Statement {

    private final Statement originalStatement;

    private final Object test;

    public EasyMockStatement(final Statement originalStatement, final Object test) {
        this.originalStatement = originalStatement;
        this.test = test;
    }

    @Override
    public void evaluate() throws Throwable {
        EasyMockSupport.injectMocks(test);
        originalStatement.evaluate();
    }
}
