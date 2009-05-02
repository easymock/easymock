/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.easymock.IArgumentMatcher;
import org.easymock.internal.matchers.And;
import org.easymock.internal.matchers.Not;
import org.easymock.internal.matchers.Or;

public class LastControl {
    private static final ThreadLocal<MocksControl> threadToControl = new ThreadLocal<MocksControl>();

    private static final ThreadLocal<Stack<Invocation>> threadToCurrentInvocation = new ThreadLocal<Stack<Invocation>>();

    private static final ThreadLocal<Stack<IArgumentMatcher>> threadToArgumentMatcherStack = new ThreadLocal<Stack<IArgumentMatcher>>();

    public static synchronized void reportLastControl(MocksControl control) {
        if (control != null) {
            threadToControl.set(control);
        } else {
            threadToControl.remove();
        }
    }

    public static synchronized MocksControl lastControl() {
        return threadToControl.get();
    }

    public static synchronized void reportMatcher(IArgumentMatcher matcher) {
        Stack<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        if (stack == null) {
            stack = new Stack<IArgumentMatcher>();
            threadToArgumentMatcherStack.set(stack);
        }
        stack.push(matcher);
    }

    public static synchronized List<IArgumentMatcher> pullMatchers() {
        Stack<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        if (stack == null) {
            return null;
        }
        threadToArgumentMatcherStack.remove();
        return new ArrayList<IArgumentMatcher>(stack);
    }

    public static synchronized void reportAnd(int count) {
        Stack<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        assertState(stack != null, "no matchers found.");
        stack.push(new And(popLastArgumentMatchers(count)));
    }

    public static synchronized void reportNot() {
        Stack<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        assertState(stack != null, "no matchers found.");
        stack.push(new Not(popLastArgumentMatchers(1).get(0)));
    }

    private static List<IArgumentMatcher> popLastArgumentMatchers(int count) {
        Stack<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        assertState(stack != null, "no matchers found.");
        assertState(stack.size() >= count, "" + count + " matchers expected, "
                + stack.size() + " recorded.");
        List<IArgumentMatcher> result = new LinkedList<IArgumentMatcher>();
        result.addAll(stack.subList(stack.size() - count, stack.size()));
        for (int i = 0; i < count; i++) {
            stack.pop();
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
        Stack<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        assertState(stack != null, "no matchers found.");
        stack.push(new Or(popLastArgumentMatchers(count)));
    }

    public static Invocation getCurrentInvocation() {
        Stack<Invocation> stack = threadToCurrentInvocation.get();
        if (stack == null || stack.empty()) {
            return null;
        }
        return stack.lastElement();
    }

    public static void pushCurrentInvocation(Invocation invocation) {
        Stack<Invocation> stack = threadToCurrentInvocation.get();
        if (stack == null) {
            stack = new Stack<Invocation>();
            threadToCurrentInvocation.set(stack);
        }
        stack.push(invocation);
    }

    public static void popCurrentInvocation() {
        Stack<Invocation> stack = threadToCurrentInvocation.get();
        stack.pop();
    }
}
