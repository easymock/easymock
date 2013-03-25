/**
 * Copyright 2001-2013 the original author or authors.
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
package org.easymock.internal.matchers;

import java.util.Arrays;

import org.easymock.internal.ArgumentToString;

/**
 * @author OFFIS, Tammo Freese
 */
public class ArrayEquals extends Equals {

    private static final long serialVersionUID = 1L;

    public ArrayEquals(final Object expected) {
        super(expected);
    }

    @Override
    public boolean matches(final Object actual) {
        final Object expected = getExpected();
        if (expected instanceof boolean[] && (actual == null || actual instanceof boolean[])) {
            return Arrays.equals((boolean[]) expected, (boolean[]) actual);
        } else if (expected instanceof byte[] && (actual == null || actual instanceof byte[])) {
            return Arrays.equals((byte[]) expected, (byte[]) actual);
        } else if (expected instanceof char[] && (actual == null || actual instanceof char[])) {
            return Arrays.equals((char[]) expected, (char[]) actual);
        } else if (expected instanceof double[] && (actual == null || actual instanceof double[])) {
            return Arrays.equals((double[]) expected, (double[]) actual);
        } else if (expected instanceof float[] && (actual == null || actual instanceof float[])) {
            return Arrays.equals((float[]) expected, (float[]) actual);
        } else if (expected instanceof int[] && (actual == null || actual instanceof int[])) {
            return Arrays.equals((int[]) expected, (int[]) actual);
        } else if (expected instanceof long[] && (actual == null || actual instanceof long[])) {
            return Arrays.equals((long[]) expected, (long[]) actual);
        } else if (expected instanceof short[] && (actual == null || actual instanceof short[])) {
            return Arrays.equals((short[]) expected, (short[]) actual);
        } else if (expected instanceof Object[] && (actual == null || actual instanceof Object[])) {
            return Arrays.equals((Object[]) expected, (Object[]) actual);
        } else {
            return super.matches(actual);
        }
    }

    @Override
    public void appendTo(final StringBuffer buffer) {
        ArgumentToString.appendArgument(getExpected(), buffer);
    }
}
