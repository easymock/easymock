/*
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
package org.easymock.tests;

import org.easymock.internal.ExpectedInvocation;
import org.easymock.internal.Invocation;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * @author OFFIS, Tammo Freese
 */
public class MatchableArgumentsTest {

    private Object[] arguments;

    private Object[] arguments2;

    @Before
    public void setup() {
        arguments = new Object[] { "" };
        arguments2 = new Object[] { "", "" };
    }

    @Test
    public void testEquals() throws Exception {
        Method toPreventNullPointerException = Object.class.getMethod("toString");

        Object mock = new Object();

        ExpectedInvocation matchableArguments = new ExpectedInvocation(new Invocation(mock,
                toPreventNullPointerException, arguments), null);
        ExpectedInvocation nonEqualMatchableArguments = new ExpectedInvocation(new Invocation(mock,
                toPreventNullPointerException, arguments2), null);

        assertFalse(matchableArguments.equals(null));
        assertFalse(matchableArguments.equals(nonEqualMatchableArguments));
    }
}
