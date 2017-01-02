/**
 * Copyright 2001-2017 the original author or authors.
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
package org.easymock.internal;

import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.runners.model.Statement;

/**
 * JUnit Statement for use by JUnit Rule or JUnit Runner to process {@link Mock} and {@link TestSubject} annotations.
 *
 * @author Henri Tremblay
 * @since 3.3
 */
public class EasyMockStatement extends Statement {

    private final Statement originalStatement;

    private final Object test;

    public EasyMockStatement(Statement originalStatement, Object test) {
        this.originalStatement = originalStatement;
        this.test = test;
    }

    @Override
    public void evaluate() throws Throwable {
        EasyMockSupport.injectMocks(test);
        originalStatement.evaluate();
    }
}