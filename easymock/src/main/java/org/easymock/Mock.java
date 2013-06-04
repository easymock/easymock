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

import java.lang.annotation.*;

/**
 * Annotation to set on a field so that {@link EasyMockRunner} or {@link EasyMockSupport#injectMocks(Object)}
 * will inject a mock to it.
 * <p>
 * Doing<br>
 * <code>@Mock private MyClass mock;</code><br> 
 * is strictly identical to doing<br>
 * <code>private MyClass mock = createMock(MyClass.class);</code>
 * 
 * @author Henri Tremblay
 * @since 3.2
 */
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mock {

    /** Type of mock to create */
    MockType type() default MockType.DEFAULT;

    /** Name of the mock to be created */
    String name() default "";
}
