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
package org.easymock;

import org.easymock.internal.EasyMockStatement;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * JUnit Rule used to process {@link Mock} and {@link TestSubject} annotations.
 * 
 * @author Alistair Todd
 * @since 3.3
 */
public class EasyMockRule implements TestRule {

    private final Object test;

    public EasyMockRule(Object test) {
        this.test = test;
    }

    public Statement apply(Statement base, Description description) {
        return new EasyMockStatement(base, test);
    }

}
