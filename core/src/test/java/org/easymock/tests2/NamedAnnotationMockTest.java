/*
 * Copyright 2001-2026 the original author or authors.
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
package org.easymock.tests2;

import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(EasyMockExtension.class)
class NamedAnnotationMockTest extends EasyMockSupport {

    @Mock(name = "my first runnable")
    private Runnable myRunner1;

    @Mock
    private Runnable myRunner2;

    @Test
    void testUnexpected() {
        replayAll();
        AssertionError error = assertThrows(AssertionError.class, () -> myRunner1.run());
        assertEquals("\n  Unexpected method call Mock named my first runnable -> Runnable.run()", error.getMessage());
    }

    @Test
    void testFailsVerify() {
        myRunner2.run();
        replayAll();
        AssertionError error = assertThrows(AssertionError.class, () -> verifyAll());
        assertEquals("\n  Expectation failure on verify:\n" +
            "    EasyMock for field org.easymock.tests2.NamedAnnotationMockTest.myRunner2 -> Runnable.run(): expected: 1, actual: 0", error.getMessage());
    }
}
