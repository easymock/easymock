/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public final class MockInvocationHandler implements InvocationHandler, Serializable {

    private static final long serialVersionUID = -7799769066534714634L;
    
    private final MocksControl control;

    public MockInvocationHandler(MocksControl control) {
        this.control = control;
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        try {
            if (control.getState() instanceof RecordState) {
                LastControl.reportLastControl(control);
            }
            return control.getState().invoke(
                    new Invocation(proxy, method, args));
        } catch (RuntimeExceptionWrapper e) {
            throw e.getRuntimeException().fillInStackTrace();
        } catch (AssertionErrorWrapper e) {
            throw e.getAssertionError().fillInStackTrace();
        } catch (ThrowableWrapper t) {
            throw t.getThrowable().fillInStackTrace();
        } catch (Throwable t) {
            throw t; // let all unwrapped pass unmodified
        }
    }

    public MocksControl getControl() {
        return control;
    }
}