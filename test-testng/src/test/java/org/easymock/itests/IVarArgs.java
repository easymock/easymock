/*
 * Copyright 2010-2018 the original author or authors.
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
package org.easymock.itests;

/**
 * @author OFFIS, Tammo Freese
 */
public interface IVarArgs {
    void withVarargsString(int value, String... s);

    void withVarargsObject(int value, Object... o);

    void withVarargsBoolean(int value, boolean... b);

    void withVarargsByte(int value, byte... b);

    void withVarargsChar(int value, char... c);

    void withVarargsDouble(int value, double... d);

    void withVarargsFloat(int value, float... f);

    void withVarargsInt(int value, int... i);

    void withVarargsLong(int value, long... l);

    void withVarargsShort(int value, short... s);
}
