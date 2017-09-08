/**
 * Copyright 2001-2017 the original author or authors.
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

import org.easymock.IArgumentMatcher;
import org.easymock.internal.matchers.ArrayEquals;
import org.easymock.internal.matchers.Equals;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author OFFIS, Tammo Freese
 */
public class ExpectedInvocation implements Serializable {

    private static final long serialVersionUID = -5554816464613350531L;

    private final Invocation invocation;

    private final List<IArgumentMatcher> matchers;

    public ExpectedInvocation(Invocation invocation, List<IArgumentMatcher> matchers) {
        this.invocation = invocation;
        this.matchers = createMissingMatchers(invocation, matchers);
    }

    private List<IArgumentMatcher> createMissingMatchers(Invocation invocation,
            List<IArgumentMatcher> matchers) {
        if (matchers != null) {
            if (matchers.size() != invocation.getArguments().length) {
                throw new IllegalStateException(
                        ""
                                + invocation.getArguments().length
                                + " matchers expected, "
                                + matchers.size()
                                + " recorded.\n"
                                + "This exception usually occurs when matchers are mixed with raw values when recording a method:\n"
                                + "\tfoo(5, eq(6));\t// wrong\n"
                                + "You need to use no matcher at all or a matcher for every single param:\n"
                                + "\tfoo(eq(5), eq(6));\t// right\n" + "\tfoo(5, 6);\t// also right");
            }
            return matchers;
        }
        List<IArgumentMatcher> result = new ArrayList<IArgumentMatcher>(invocation.getArguments().length);
        for (Object argument : invocation.getArguments()) {
            if(argument != null && argument.getClass().isArray()) {
                result.add(new ArrayEquals(argument));
            }
            else {
                result.add(new Equals(argument));
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !this.getClass().equals(o.getClass())) {
            return false;
        }

        ExpectedInvocation other = (ExpectedInvocation) o;
        return this.invocation.equals(other.invocation)
                && ((this.matchers == null && other.matchers == null) || (this.matchers != null && this.matchers
                        .equals(other.matchers)));
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("hashCode() is not implemented");
    }

    public boolean matches(Invocation actual) {
        return this.invocation.getMock() == actual.getMock()
                && this.invocation.getMethod().equals(actual.getMethod()) && matches(actual.getArguments());
    }

    private boolean matches(Object[] arguments) {
        if (arguments.length != matchers.size()) {
            return false;
        }
        for (int i = 0; i < arguments.length; i++) {
            if (!matchers.get(i).matches(arguments[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(invocation.getMockAndMethodName());
        result.append("(");
        for (Iterator<IArgumentMatcher> it = matchers.iterator(); it.hasNext();) {
            it.next().appendTo(result);
            if (it.hasNext()) {
                result.append(", ");
            }
        }
        result.append(")");
        return result.toString();
    }

    public Method getMethod() {
        return invocation.getMethod();
    }
}
