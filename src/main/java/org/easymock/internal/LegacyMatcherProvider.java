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

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.easymock.ArgumentsMatcher;
import org.easymock.MockControl;

@SuppressWarnings("deprecation")
public class LegacyMatcherProvider implements Serializable {

    private static final long serialVersionUID = -4143082656571251917L;

    private ArgumentsMatcher defaultMatcher;

    private boolean defaultMatcherSet;

    private transient Map<Method, ArgumentsMatcher> matchers = new HashMap<Method, ArgumentsMatcher>();

    public ArgumentsMatcher getMatcher(Method method) {
        if (!matchers.containsKey(method)) {
            if (!defaultMatcherSet) {
                setDefaultMatcher(MockControl.EQUALS_MATCHER);
            }
            matchers.put(method, defaultMatcher);
        }
        return matchers.get(method);
    }

    public void setDefaultMatcher(ArgumentsMatcher matcher) {
        if (defaultMatcherSet) {
            throw new RuntimeExceptionWrapper(
                    new IllegalStateException(
                            "default matcher can only be set once directly after creation of the MockControl"));
        }
        defaultMatcher = matcher;
        defaultMatcherSet = true;
    }

    public void setMatcher(Method method, ArgumentsMatcher matcher) {
        if (matchers.containsKey(method) && matchers.get(method) != matcher) {
            throw new RuntimeExceptionWrapper(new IllegalStateException(
                    "for method "
                            + method.getName()
                            + "("
                            + (method.getParameterTypes().length == 0 ? ""
                                    : "...")
                            + "), a matcher has already been set"));
        }
        matchers.put(method, matcher);
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        Map<MethodSerializationWrapper, ArgumentsMatcher> map = (Map<MethodSerializationWrapper, ArgumentsMatcher>) stream
                .readObject();
        matchers = new HashMap<Method, ArgumentsMatcher>(map.size());
        for (Map.Entry<MethodSerializationWrapper, ArgumentsMatcher> entry : map
                .entrySet()) {
            try {
                Method method = entry.getKey().getMethod();
                matchers.put(method, entry.getValue());
            } catch (NoSuchMethodException e) {
                // ///CLOVER:OFF
                throw new IOException(e.toString());
                // ///CLOVER:ON
            }
        }
    }

    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
        stream.defaultWriteObject();
        Map<MethodSerializationWrapper, ArgumentsMatcher> map = new HashMap<MethodSerializationWrapper, ArgumentsMatcher>(
                matchers.size());
        for (Map.Entry<Method, ArgumentsMatcher> matcher : matchers.entrySet()) {
            map.put(new MethodSerializationWrapper(matcher.getKey()), matcher
                    .getValue());
        }
        stream.writeObject(map);
    }
}
