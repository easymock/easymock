/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.easymock.IAnswer;
import org.easymock.MockControl;
import org.easymock.internal.MockInvocationHandler;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class StacktraceTest {

    private MockControl<IMethods> control;

    private IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createStrictControl(IMethods.class);
        mock = control.getMock();
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
            assertTrue("stack trace must not be cut",
                    Util.getStackTrace(expected).indexOf(
                            ToStringThrowsException.class.getName()) > 0);
        }
    }

    @Test
    public void assertReplayNoFillInStacktraceWhenExceptionNotFromEasyMock() {
        mock.oneArg(new ToStringThrowsException());
        try {
            control.replay();
        } catch (NullPointerException expected) {
            assertTrue("stack trace must not be cut",
                    Util.getStackTrace(expected).indexOf(
                            ToStringThrowsException.class.getName()) > 0);
        }
    }

    @Test
    public void assertReplayStateNoFillInStacktraceWhenExceptionNotFromEasyMock() {
        control.replay();
        try {
            mock.oneArg(new ToStringThrowsException());
        } catch (NullPointerException expected) {
            assertTrue("stack trace must not be cut",
                    Util.getStackTrace(expected).indexOf(
                            ToStringThrowsException.class.getName()) > 0);
        }
    }

    @Test
    public void assertVerifyNoFillInStacktraceWhenExceptionNotFromEasyMock() {
        mock.oneArg(new ToStringThrowsException());
        control.setReturnValue("");
        control.replay();
        try {
            control.verify();
            fail();
        } catch (NullPointerException expected) {
            assertTrue("stack trace must not be cut",
                    Util.getStackTrace(expected).indexOf(
                            ToStringThrowsException.class.getName()) > 0);
        }
    }

    @Test
    public void assertFillWhenThrowingAnswer() {
        expect(mock.oneArg("")).andThrow(new NullPointerException());
        replay(mock);
        try {
            mock.oneArg("");
        } catch (NullPointerException expected) {
            assertTrue("stack trace should cut", Util.startWithClass(expected, MockInvocationHandler.class));
        }
    }

    @Test
    public void assertNoFillWhenDelegatingAnswer() {
        IMethods answer = (IMethods) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[] { IMethods.class },
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method,
                            Object[] args) throws Throwable {
                        throw new NullPointerException();
                    }
                }); 
        expect(mock.oneArg("")).andDelegateTo(answer);
        replay(mock);
        try {
            mock.oneArg("");
        } catch (NullPointerException expected) {
            assertTrue("stack trace must not be cut",
                    Util.startWithClass(
                    expected, Proxy.getInvocationHandler(answer).getClass()));
        }
    }

    @Test
    public void assertNoFillWhenIAnswerAnswer() {
        IAnswer<String> answer = new IAnswer<String>() {
            public String answer() throws Throwable {
                throw new NullPointerException();
            }
        };
        expect(mock.oneArg("")).andAnswer(answer);
        replay(mock);
        try {
            mock.oneArg("");
        } catch (NullPointerException expected) {
            assertTrue("stack trace must not be cut",
                    Util.startWithClass(
                    expected, answer.getClass()));
        }
    }
}
