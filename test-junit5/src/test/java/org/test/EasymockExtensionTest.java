/*
 * Copyright 2019-2021 the original author or authors.
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
package org.test;

import static org.junit.jupiter.api.Assertions.*;

import org.easymock.EasyMock;
import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(EasyMockExtension.class)
public class EasymockExtensionTest extends EasyMockSupport {

	@Mock
	Object mockFromAnnotation;

	@Test
	public void mockInjection() {
		Object mockFromMethod = EasyMock.mock(Object.class);

		replayAll();

		assertEquals(mockFromMethod.getClass(), mockFromAnnotation.getClass(),
				"Expected annotation driven mock injection to work with JUnit 5");

		verifyAll();
	}
}
