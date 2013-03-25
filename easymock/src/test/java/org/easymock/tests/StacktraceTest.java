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

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.easymock.IAnswer;
import org.easymock.internal.MockInvocationHandler;
import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class StacktraceTest {

    private IMethods mock;

    @Before
    public void setup() {
        mock = createMock(IMethods.class);
    }

    private static class ToStringThrowsException {
        @Override
        public String toString() {
            throw new NullPointerException();
        }
    }

    @Test
    public void assertRecordStateNoFillInStacktraceWhenExceptionNotFromEasyMock() {
        mock.oneArg(new ToStringThrowsException());
        try {
            mock.oneArg(new ToStringThrowsException());
        } catch (final NullPointerException expected) {
            assertTrue("stack trace must not be cut", Util.getStackTrace(expected).indexOf(
                    ToStringThrowsException.class.getName()) > 0);
        }
    }

    @Test
    public void assertReplayNoFillInStacktraceWhenExceptionNotFromEasyMock() {
        mock.oneArg(new ToStringThrowsException());
        try {
            replay(mock);
        } catch (final NullPointerException expected) {
            assertTrue("stack trace must not be cut", Util.getStackTrace(expected).indexOf(
                    ToStringThrowsException.class.getName()) > 0);
        }
    }

    @Test
    public void assertReplayStateNoFillInStacktraceWhenExceptionNotFromEasyMock() {
        replay(mock);
        try {
            mock.oneArg(new ToStringThrowsException());
        } catch (final NullPointerException expected) {
            assertTrue("stack trace must not be cut", Util.getStackTrace(expected).indexOf(
                    ToStringThrowsException.class.getName()) > 0);
        }
    }

    @Test
    public void assertVerifyNoFillInStacktraceWhenExceptionNotFromEasyMock() {
        expect(mock.oneArg(new ToStringThrowsException())).andReturn("");
        replay(mock);
        try {
            verify(mock);
            fail();
        } catch (final NullPointerException expected) {
            assertTrue("stack trace must not be cut", Util.getStackTrace(expected).indexOf(
                    ToStringThrowsException.class.getName()) > 0);
        }
    }

    @Test
    public void assertFillWhenThrowingAnswer() {
        expect(mock.oneArg("")).andThrow(new NullPointerException());
        replay(mock);
        try {
            mock.oneArg("");
        } catch (final NullPointerException expected) {
            assertTrue("stack trace should cut", Util.startWithClass(expected, MockInvocationHandler.class));
        }
    }

    @Test
    public void assertNoFillWhenDelegatingAnswer() {
        final IMethods answer = (IMethods) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[] { IMethods.class }, new InvocationHandler() {
                    public Object invoke(final Object proxy, final Method method, final Object[] args)
                            throws Throwable {
                        throw new NullPointerException();
                    }
                });
        expect(mock.oneArg("")).andDelegateTo(answer);
        replay(mock);
        try {
            mock.oneArg("");
        } catch (final NullPointerException expected) {
            assertTrue("stack trace must not be cut", Util.startWithClass(expected, Proxy
                    .getInvocationHandler(answer).getClass()));
        }
    }

    @Test
    public void assertNoFillWhenIAnswerAnswer() {
        final IAnswer<String> answer = new IAnswer<String>() {
            public String answer() throws Throwable {
                throw new NullPointerException();
            }
        };
        expect(mock.oneArg("")).andAnswer(answer);
        replay(mock);
        try {
            mock.oneArg("");
        } catch (final NullPointerException expected) {
            assertTrue("stack trace must not be cut", Util.startWithClass(expected, answer.getClass()));
        }
    }
}
