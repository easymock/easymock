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

import java.io.IOException;

/**
 * @author OFFIS, Tammo Freese
 */
public interface IMethods {

    boolean booleanReturningMethod(int index);

    byte byteReturningMethod(int index);

    short shortReturningMethod(int index);

    char charReturningMethod(int index);

    int intReturningMethod(int index);

    long longReturningMethod(int index);

    float floatReturningMethod(int index);

    double doubleReturningMethod(int index);

    Object objectReturningMethod(int index);

    String oneArg(boolean value);

    String oneArg(byte value);

    String oneArg(short value);

    String oneArg(char value);

    String oneArg(int value);

    String oneArg(long value);

    String oneArg(float value);

    String oneArg(double value);

    String oneArg(Object value);

    String oneArg(String value);

    public String throwsNothing(boolean value);

    public String throwsIOException(int count) throws IOException;

    public String throwsError(int count) throws Error;

    void simpleMethod();

    void simpleMethodWithArgument(String argument);

    Object threeArgumentMethod(int valueOne, Object valueTwo, String valueThree);

    void twoArgumentMethod(int one, int two);

    void arrayMethod(String[] strings);

    String oneArray(boolean[] array);

    String oneArray(byte[] array);

    String oneArray(char[] array);

    String oneArray(double[] array);

    String oneArray(float[] array);

    String oneArray(int[] array);

    String oneArray(long[] array);

    String oneArray(short[] array);

    String oneArray(Object[] array);

    String oneArray(String[] array);

    void varargsString(int i, String... string);

    void varargsObject(int i, Object... object);
}
