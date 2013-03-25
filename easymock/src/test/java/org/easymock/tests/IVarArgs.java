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
package org.easymock.tests;

/**
 * @author OFFIS, Tammo Freese
 */
public interface IVarArgs {
    public void withVarargsString(int value, String... s);

    public void withVarargsObject(int value, Object... o);

    public void withVarargsBoolean(int value, boolean... b);

    public void withVarargsByte(int value, byte... b);

    public void withVarargsChar(int value, char... c);

    public void withVarargsDouble(int value, double... d);

    public void withVarargsFloat(int value, float... f);

    public void withVarargsInt(int value, int... i);

    public void withVarargsLong(int value, long... l);

    public void withVarargsShort(int value, short... s);
}
