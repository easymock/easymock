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
package org.easymock.tests;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.easymock.internal.ExpectedInvocation;
import org.easymock.internal.Invocation;
import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class ExpectedMethodCallTest {

    private ExpectedInvocation call;

    @Before
    public void setup() throws SecurityException, NoSuchMethodException {
        final Object[] arguments1 = new Object[] { "" };
        final Method m = Object.class.getMethod("equals", new Class[] { Object.class });
        call = new ExpectedInvocation(new Invocation(null, m, arguments1), null);
    }

    @Test
    public void testHashCode() {
        try {
            call.hashCode();
            fail();
        } catch (final UnsupportedOperationException expected) {
            assertEquals("hashCode() is not implemented", expected.getMessage());
        }
    }
}