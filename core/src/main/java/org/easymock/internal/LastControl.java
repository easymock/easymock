/*
 * Copyright 2001-2023 the original author or authors.
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.easymock.IArgumentMatcher;
import org.easymock.internal.matchers.And;
import org.easymock.internal.matchers.Not;
import org.easymock.internal.matchers.Or;

/**
 * @author OFFIS, Tammo Freese
 */
public final class LastControl {

    private static final String NO_MATCHERS_FOUND = "no matchers found.";

    private static final ThreadLocal<MocksControl> threadToControl = new ThreadLocal<>();

    private static final ThreadLocal<List<Invocation>> threadToCurrentInvocation = new ThreadLocal<>();

    private static final ThreadLocal<List<IArgumentMatcher>> threadToArgumentMatcherStack = new ThreadLocal<>();

    // ///CLOVER:OFF
    private LastControl() {
    }

    // ///CLOVER:ON

    public static void reportLastControl(MocksControl control) {
        if (control != null) {
            threadToControl.set(control);
        } else {
            threadToControl.remove();
        }
    }

    public static MocksControl lastControl() {
        return threadToControl.get();
    }

    public static void reportMatcher(IArgumentMatcher matcher) {
        List<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        if (stack == null) {
            stack = new ArrayList<>(5); // methods of more than 5 parameters are quite rare
            threadToArgumentMatcherStack.set(stack);
        }
        stack.add(matcher);
    }

    public static List<IArgumentMatcher> pullMatchers() {
        List<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        if (stack == null) {
            return null;
        }
        threadToArgumentMatcherStack.remove();
        return new ArrayList<>(stack);
    }

    public static void reportAnd(int count) {
        List<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        assertState(stack != null, NO_MATCHERS_FOUND);
        stack.add(new And(popLastArgumentMatchers(count)));
    }

    public static void reportNot() {
        List<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        assertState(stack != null, NO_MATCHERS_FOUND);
        stack.add(new Not(popLastArgumentMatchers(1).get(0)));
    }

    private static List<IArgumentMatcher> popLastArgumentMatchers(int count) {
        List<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        assertState(stack != null, NO_MATCHERS_FOUND);
        assertState(stack.size() >= count, "" + count + " matchers expected, " + stack.size() + " recorded.");
        List<IArgumentMatcher> result = new LinkedList<>(stack.subList(stack.size() - count, stack.size()));
        for (int i = 0; i < count; i++) {
            stack.remove(stack.size()-1);
        }
        return result;
    }

    private static void assertState(boolean toAssert, String message) {
        if (!toAssert) {
            threadToArgumentMatcherStack.remove();
            throw new IllegalStateException(message);
        }
    }

    public static void reportOr(int count) {
        List<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        assertState(stack != null, NO_MATCHERS_FOUND);
        stack.add(new Or(popLastArgumentMatchers(count)));
    }

    public static Invocation getCurrentInvocation() {
        List<Invocation> stack = threadToCurrentInvocation.get();
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        return stack.get(stack.size()-1);
    }

    public static void pushCurrentInvocation(Invocation invocation) {
        List<Invocation> stack = threadToCurrentInvocation.get();
        if (stack == null) {
            stack = new ArrayList<>(2); // we will rarely have more than 1 recursion. So almost never over 2
            threadToCurrentInvocation.set(stack);
        }
        stack.add(invocation);
    }

    public static void popCurrentInvocation() {
        List<Invocation> stack = threadToCurrentInvocation.get();
        stack.remove(stack.size()-1);
    }
}
