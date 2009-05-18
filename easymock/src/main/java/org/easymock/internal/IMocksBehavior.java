/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

public interface IMocksBehavior extends ILegacyMatcherMethods {

    // record
    void addExpected(ExpectedInvocation expected, Result result, Range count);

    void addStub(ExpectedInvocation expected, Result result);

    void checkOrder(boolean value);

    void makeThreadSafe(boolean isThreadSafe);
    
    void shouldBeUsedInOneThread(boolean shouldBeUsedInOneThread);
    
    // replay
    Result addActual(Invocation invocation);    
    
    boolean isThreadSafe();

    void checkThreadSafety();

    // verify
    void verify();
}
