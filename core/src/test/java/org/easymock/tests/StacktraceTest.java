/*
 * Copyright 2001-2025 the original author or authors.
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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.easymock.IAnswer;
import org.easymock.internal.MockInvocationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class StacktraceTest {

    private IMethods mock;

    @BeforeEach
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
        } catch (NullPointerException expected) {
            Assertions.assertTrue(Util.getStackTrace(expected).indexOf(
                    ToStringThrowsException.class.getName()) > 0, "stack trace must not be cut");
        }
    }

    @Test
    public void assertReplayNoFillInStacktraceWhenExceptionNotFromEasyMock() {
        mock.oneArg(new ToStringThrowsException());
        try {
            replay(mock);
        } catch (NullPointerException expected) {
            Assertions.assertTrue(Util.getStackTrace(expected).indexOf(
                    ToStringThrowsException.class.getName()) > 0, "stack trace must not be cut");
        }
    }

    @Test
    public void assertReplayStateNoFillInStacktraceWhenExceptionNotFromEasyMock() {
        replay(mock);
        try {
            mock.oneArg(new ToStringThrowsException());
        } catch (NullPointerException expected) {
            Assertions.assertTrue(Util.getStackTrace(expected).indexOf(
                    ToStringThrowsException.class.getName()) > 0, "stack trace must not be cut");
        }
    }

    @Test
    public void assertVerifyNoFillInStacktraceWhenExceptionNotFromEasyMock() {
        expect(mock.oneArg(new ToStringThrowsException())).andReturn("");
        replay(mock);
        try {
            verify(mock);
            Assertions.fail();
        } catch (NullPointerException expected) {
            Assertions.assertTrue(Util.getStackTrace(expected).indexOf(
                    ToStringThrowsException.class.getName()) > 0, "stack trace must not be cut");
        }
    }

    @Test
    public void assertFillWhenThrowingAnswer() {
        expect(mock.oneArg("")).andThrow(new NullPointerException());
        replay(mock);
        try {
            mock.oneArg("");
        } catch (NullPointerException expected) {
            Assertions.assertTrue(Util.startWithClass(expected, MockInvocationHandler.class), "stack trace should cut");
        }
    }

    @Test
    public void assertNoFillWhenDelegatingAnswer() {
        @SuppressWarnings("Convert2Lambda") // otherwise the assertion below won't work
        IMethods answer = (IMethods) Proxy.newProxyInstance(getClass().getClassLoader(),
            new Class<?>[] { IMethods.class }, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) {
                    throw new NullPointerException();
                }
        });
        expect(mock.oneArg("")).andDelegateTo(answer);
        replay(mock);
        try {
            mock.oneArg("");
        } catch (NullPointerException expected) {
            Class<? extends InvocationHandler> proxyClass = Proxy.getInvocationHandler(answer).getClass();
            Assertions.assertTrue(Util.startWithClass(expected, proxyClass), "stack trace must not be cut but was\nstarting with " +
                       expected.getStackTrace()[0] + "\ninstead of " + proxyClass);
        }
    }

    @Test
    public void assertNoFillWhenIAnswerAnswer() {
        @SuppressWarnings("Convert2Lambda") // otherwise the assertion below won't work
        IAnswer<String> answer = new IAnswer<String>() {
            @Override
            public String answer() {
                throw new NullPointerException();
            }
        };
        expect(mock.oneArg("")).andAnswer(answer);
        replay(mock);
        try {
            mock.oneArg("");
        } catch (NullPointerException expected) {
            Assertions.assertTrue(Util.startWithClass(expected, answer.getClass()), "stack trace must not be cut but was\nstarting with " +
                       expected.getStackTrace()[0] + "\ninstead of " + answer.getClass());
        }
    }
}
