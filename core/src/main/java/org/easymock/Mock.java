/**
 * Copyright 2001-2015 the original author or authors.
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
 * Annotation to set on a field so that {@link EasyMockRunner}, {@link EasyMockRule} or {@link EasyMockSupport#injectMocks(Object)}
 * will inject a mock to it.
 * <p>
 * Doing<br>
 * {@code @Mock private MyClass mock;}<br>
 * is strictly identical to doing<br>
 * {@code private MyClass mock = createMock(MyClass.class);}
 *
 * @author Henri Tremblay
 * @since 3.2
 */
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mock {

    /** @return type of mock to create */
    MockType type() default MockType.DEFAULT;

    /** @return name of the mock to be created */
    String name() default "";

    /**
     * Name of the test subject field to which this mock will be assigned. Use to disambiguate the case where a mock may be assigned to multiple fields of the same type.
     * When set, this mock will be assigned to the given field name in any test subject with a matching field name.
     * If not set, injection is to all type-compatible fields in all test subjects.
     * A given field name may only be used once, and there must be a matching field in at least one test subject.
     *
     * @return name of the field to inject to
     **/
    String fieldName() default "";
}
