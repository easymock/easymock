/**
 * Copyright 2003-2011 the original author or authors.
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
package org.easymock.classextension.tests;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.classextension.IMocksControl;
import org.easymock.classextension.internal.MocksClassControl;
import org.easymock.internal.ClassExtensionHelper;
import org.easymock.internal.MocksControl.MockType;
import org.junit.Test;

/**
 * Test all kind of control creation to make sure that the correct behavior is
 * given.
 * 
 * @author Henri Tremblay
 */
@SuppressWarnings("deprecation")
public class MockingTest {

    private IMocksControl ctrl;

    public static class ClassToMock {
        public int foo() {
            return 10;
        }

        public int method() {
            return 20;
        }
    }

    /**
     * Make sure one mock is not interacting with another
     */
    @Test
    public void testTwoMocks() {
        ClassToMock transition1 = createMock(ClassToMock.class);
        ClassToMock transition2 = createMock(ClassToMock.class);

        // Should have two different callbacks
        assertNotSame(ClassExtensionHelper.getInterceptor(transition2),
                ClassExtensionHelper.getInterceptor(transition1));

        transition2.foo();
        transition1.foo();
    }

    @Test
    public void testInterfaceMocking() {
        IMocksControl ctrl = createControl();
        checkBehavior(ctrl, MockType.DEFAULT);
    }

    @Test
    public void testNiceInterfaceMocking() {
        IMocksControl ctrl = createNiceControl();
        checkBehavior(ctrl, MockType.NICE);
    }

    @Test
    public void testStrictInterfaceMocking() {
        IMocksControl ctrl = createStrictControl();
        checkBehavior(ctrl, MockType.STRICT);
    }

    private void checkBehavior(IMocksControl ctrl, MockType behavior) {
        assertEquals(behavior, extractBehavior(ctrl));
    }

    private MockType extractBehavior(IMocksControl ctrl) {
        return ((MocksClassControl) ctrl).getType();
    }
}
