/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.Method;

public interface ILegacyMatcherMethods {

    @SuppressWarnings("deprecation")
    void setDefaultMatcher(org.easymock.ArgumentsMatcher matcher);

    @SuppressWarnings("deprecation")
    void setMatcher(Method method, org.easymock.ArgumentsMatcher matcher);
}