/*
 * Copyright 2001-2009 OFFIS, Tammo Freese
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