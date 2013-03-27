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
package org.easymock;

import java.lang.reflect.Method;
import java.util.Comparator;

import org.easymock.internal.*;
import org.easymock.internal.matchers.*;

/**
 * Main EasyMock class. Contains methods to create, replay and verify mocks and
 * a list of standard matchers.
 * 
 * @author OFFIS, Tammo Freese
 * @author Henri Tremblay
 */
public class EasyMock {

    /**
     * Since EasyMock 2.4, by default, a mock wasn't allowed to be called in
     * multiple threads unless it was made thread-safe (See
     * {@link #makeThreadSafe(Object, boolean)} method). Since EasyMock 2.5,
     * this isn't the default anymore. For backward compatibility, this property
     * can bring EasyMock 2.4 behavior back.
     */
    public static final String ENABLE_THREAD_SAFETY_CHECK_BY_DEFAULT = "easymock.enableThreadSafetyCheckByDefault";

    /**
     * Since EasyMock 2.5, by default a mock is thread-safe. For backward
     * compatibility, this property can change the default. A given mock still
     * can be made thread-safe by calling
     * {@link #makeThreadSafe(Object, boolean)}.
     */
    public static final String NOT_THREAD_SAFE_BY_DEFAULT = "easymock.notThreadSafeByDefault";

    /**
     * Since EasyMock 3.0, EasyMock can perform class mocking directly without
     * using the class extension. If you want to disable any class mocking, turn
     * this to true.
     */
    public static final String DISABLE_CLASS_MOCKING = "easymock.disableClassMocking";

    /**
     * Creates a mock object, of the requested type, that implements the given interface
     * or extends the given class
     * 
     * @param <T>
     *            the interface that the mock object should implement.
     * @param type 
     *            the type of the mock to be created.
     * @param toMock
     *            the class or interface that should be mocked.
     * @return the mock object.
     * @since 3.2
     */
    public static <T> T createMock(final MockType type, final Class<T> toMock) {
        return createControl(type).createMock(toMock);
    }

    /**
     * Creates a mock object, of the requested type and name, that implements the given interface
     * or extends the given class
     * 
     * @param <T> 
     *            the class or interface that should be mocked.
     * @param name 
     *            the name of the mock object.
     * @param type 
     *            the type of the mock to be created.
     * @param toMock
     *            the class or interface that should be mocked.
     * @return the mock object.
     * @since 3.2
     */
    public static <T> T createMock(final String name, final MockType type, final Class<T> toMock) {
        return createControl(type).createMock(name, toMock);
    }

    /**
     * Creates a mock object that implements the given interface, order checking
     * is enabled by default.
     * 
     * @param <T>
     *            the interface that the mock object should implement.
     * @param toMock
     *            the class or interface that should be mocked.
     * @return the mock object.
     */
    public static <T> T createStrictMock(final Class<T> toMock) {
        return createStrictControl().createMock(toMock);
    }

    /**
     * Creates a mock object that implements the given interface, order checking
     * is enabled by default.
     * 
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class or interface that should be mocked.
     * @param <T>
     *            the interface that the mock object should implement.
     * @return the mock object.
     * @throws IllegalArgumentException
     *             if the name is not a valid Java identifier.
     */
    public static <T> T createStrictMock(final String name, final Class<T> toMock) {
        return createStrictControl().createMock(name, toMock);
    }

    /**
     * Creates a mock object that implements the given interface, order checking
     * is disabled by default.
     * 
     * @param <T>
     *            the interface that the mock object should implement.
     * @param toMock
     *            the class or interface that should be mocked.
     * @return the mock object.
     */
    public static <T> T createMock(final Class<T> toMock) {
        return createControl().createMock(toMock);
    }

    /**
     * Creates a mock object that implements the given interface, order checking
     * is disabled by default.
     * 
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class or interface that should be mocked.
     * 
     * @param <T>
     *            the interface that the mock object should implement.
     * @return the mock object.
     * @throws IllegalArgumentException
     *             if the name is not a valid Java identifier.
     */
    public static <T> T createMock(final String name, final Class<T> toMock) {
        return createControl().createMock(name, toMock);
    }

    /**
     * Creates a mock object that implements the given interface, order checking
     * is disabled by default, and the mock object will return <code>0</code>,
     * <code>null</code> or <code>false</code> for unexpected invocations.
     * 
     * @param <T>
     *            the interface that the mock object should implement.
     * @param toMock
     *            the class or interface that should be mocked.
     * @return the mock object.
     */
    public static <T> T createNiceMock(final Class<T> toMock) {
        return createNiceControl().createMock(toMock);
    }

    /**
     * Creates a mock object that implements the given interface, order checking
     * is disabled by default, and the mock object will return <code>0</code>,
     * <code>null</code> or <code>false</code> for unexpected invocations.
     * 
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class or interface that should be mocked.
     * 
     * @param <T>
     *            the interface that the mock object should implement.
     * @return the mock object.
     * @throws IllegalArgumentException
     *             if the name is not a valid Java identifier.
     */
    public static <T> T createNiceMock(final String name, final Class<T> toMock) {
        return createNiceControl().createMock(name, toMock);
    }

    /**
     * Creates a mock object that extends the given class, order checking is
     * enabled by default.
     * 
     * @param <T>
     *            the class or interface that should be mocked.
     * @param toMock
     *            the class or interface that should be mocked.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public static <T> T createStrictMock(final Class<T> toMock, final Method... mockedMethods) {
        return createStrictControl().createMock(toMock, mockedMethods);
    }

    /**
     * Creates a mock object that extends the given class, order checking is
     * enabled by default.
     * 
     * @param <T>
     *            the class or interface that should be mocked.
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class or interface that should be mocked.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public static <T> T createStrictMock(final String name, final Class<T> toMock,
            final Method... mockedMethods) {
        return createStrictControl().createMock(name, toMock, mockedMethods);
    }

    /**
     * Creates a mock object that extends the given class, order checking is
     * enabled by default.
     * 
     * @param <T>
     *            the class or interface that should be mocked.
     * @param toMock
     *            the class or interface that should be mocked.
     * @param constructorArgs
     *            constructor and parameters used to instantiate the mock.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public static <T> T createStrictMock(final Class<T> toMock, final ConstructorArgs constructorArgs,
            final Method... mockedMethods) {
        return createStrictControl().createMock(toMock, constructorArgs, mockedMethods);
    }

    /**
     * Creates a mock object that extends the given class, order checking is
     * enabled by default.
     * 
     * @param <T>
     *            the class or interface that should be mocked.
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class or interface that should be mocked.
     * @param constructorArgs
     *            constructor and parameters used to instantiate the mock.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public static <T> T createStrictMock(final String name, final Class<T> toMock,
            final ConstructorArgs constructorArgs, final Method... mockedMethods) {
        return createStrictControl().createMock(name, toMock, constructorArgs, mockedMethods);
    }

    /**
     * Creates a mock object that extends the given class, order checking is
     * disabled by default.
     * 
     * @param <T>
     *            the class or interface that should be mocked.
     * @param toMock
     *            the class or interface that should be mocked.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public static <T> T createMock(final Class<T> toMock, final Method... mockedMethods) {
        return createControl().createMock(toMock, mockedMethods);
    }

    /**
     * Creates a mock object that extends the given class, order checking is
     * disabled by default.
     * 
     * @param <T>
     *            the class or interface that should be mocked.
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class or interface that should be mocked.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public static <T> T createMock(final String name, final Class<T> toMock, final Method... mockedMethods) {
        return createControl().createMock(name, toMock, mockedMethods);
    }

    /**
     * Creates a mock object that extends the given class, order checking is
     * disabled by default.
     * 
     * @param <T>
     *            the class or interface that should be mocked.
     * @param toMock
     *            the class or interface that should be mocked.
     * @param constructorArgs
     *            constructor and parameters used to instantiate the mock.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public static <T> T createMock(final Class<T> toMock, final ConstructorArgs constructorArgs,
            final Method... mockedMethods) {
        return createControl().createMock(toMock, constructorArgs, mockedMethods);
    }

    /**
     * Creates a mock object that extends the given class, order checking is
     * disabled by default.
     * 
     * @param <T>
     *            the class or interface that should be mocked.
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class or interface that should be mocked.
     * @param constructorArgs
     *            constructor and parameters used to instantiate the mock.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public static <T> T createMock(final String name, final Class<T> toMock,
            final ConstructorArgs constructorArgs, final Method... mockedMethods) {
        return createControl().createMock(name, toMock, constructorArgs, mockedMethods);
    }

    /**
     * Creates a mock object that extends the given class, order checking is
     * disabled by default, and the mock object will return <code>0</code>,
     * <code>null</code> or <code>false</code> for unexpected invocations.
     * 
     * @param <T>
     *            the class or interface that should be mocked.
     * @param toMock
     *            the class or interface that should be mocked.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public static <T> T createNiceMock(final Class<T> toMock, final Method... mockedMethods) {
        return createNiceControl().createMock(toMock, mockedMethods);
    }

    /**
     * Creates a mock object that extends the given class, order checking is
     * disabled by default, and the mock object will return <code>0</code>,
     * <code>null</code> or <code>false</code> for unexpected invocations.
     * 
     * @param <T>
     *            the class or interface that should be mocked.
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class or interface that should be mocked.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public static <T> T createNiceMock(final String name, final Class<T> toMock,
            final Method... mockedMethods) {
        return createNiceControl().createMock(name, toMock, mockedMethods);
    }

    /**
     * Creates a mock object that extends the given class, order checking is
     * disabled by default, and the mock object will return <code>0</code>,
     * <code>null</code> or <code>false</code> for unexpected invocations.
     * 
     * @param <T>
     *            the class or interface that should be mocked.
     * @param toMock
     *            the class or interface that should be mocked.
     * @param constructorArgs
     *            constructor and parameters used to instantiate the mock.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public static <T> T createNiceMock(final Class<T> toMock, final ConstructorArgs constructorArgs,
            final Method... mockedMethods) {
        return createNiceControl().createMock(toMock, constructorArgs, mockedMethods);
    }

    /**
     * Creates a mock object that extends the given class, order checking is
     * disabled by default, and the mock object will return <code>0</code>,
     * <code>null</code> or <code>false</code> for unexpected invocations.
     * 
     * @param <T>
     *            the class or interface that should be mocked.
     * @param name
     *            the name of the mock object.
     * @param toMock
     *            the class or interface that should be mocked.
     * @param constructorArgs
     *            constructor and parameters used to instantiate the mock.
     * @param mockedMethods
     *            methods that will be mocked, other methods will behave
     *            normally
     * @return the mock object.
     * 
     * @deprecated Use {@link #createMockBuilder(Class)} instead
     */
    @Deprecated
    public static <T> T createNiceMock(final String name, final Class<T> toMock,
            final ConstructorArgs constructorArgs, final Method... mockedMethods) {
        return createNiceControl().createMock(name, toMock, constructorArgs, mockedMethods);
    }

    /**
     * Create a mock builder allowing to create a partial mock for the given
     * class or interface.
     * 
     * @param <T>
     *            the interface that the mock object should implement.
     * @param toMock
     *            the class or interface that should be mocked.
     * @return a mock builder to create a partial mock
     */
    public static <T> IMockBuilder<T> createMockBuilder(final Class<T> toMock) {
        return new MockBuilder<T>(toMock);
    }

    /**
     * Creates a control of the requested type.
     * 
     * @param type the mock type
     * @return the control.
     * @since 3.2
     */
    public static IMocksControl createControl(final MockType type) {
        return new MocksControl(type);
    }

    /**
     * Creates a control, order checking is enabled by default.
     * 
     * @return the control.
     */
    public static IMocksControl createStrictControl() {
        return createControl(MockType.STRICT);
    }

    /**
     * Creates a control, order checking is disabled by default.
     * 
     * @return the control.
     */
    public static IMocksControl createControl() {
        return createControl(MockType.DEFAULT);
    }

    /**
     * Creates a control, order checking is disabled by default, and the mock
     * objects created by this control will return <code>0</code>,
     * <code>null</code> or <code>false</code> for unexpected invocations.
     * 
     * @return the control.
     */
    public static IMocksControl createNiceControl() {
        return createControl(MockType.NICE);
    }

    /**
     * Returns the expectation setter for the last expected invocation in the
     * current thread.
     * 
     * @param <T>
     *            type returned by the expected method
     * @param value
     *            the parameter is used to transport the type to the
     *            ExpectationSetter. It allows writing the expected call as
     *            argument, i.e.
     *            <code>expect(mock.getName()).andReturn("John Doe")<code>.
     * 
     * @return the expectation setter.
     */
    public static <T> IExpectationSetters<T> expect(final T value) {
        return EasyMock.getControlForLastCall();
    }

    /**
     * Returns the expectation setter for the last expected invocation in the
     * current thread. This method is used for expected invocations on void
     * methods.
     * 
     * @param <T>
     *            type returned by the expected method
     * @return the expectation setter.
     */
    public static <T> IExpectationSetters<T> expectLastCall() {
        return getControlForLastCall();
    }

    @SuppressWarnings("unchecked")
    private static <T> IExpectationSetters<T> getControlForLastCall() {
        final MocksControl lastControl = LastControl.lastControl();
        if (lastControl == null) {
            LastControl.pullMatchers(); // cleanup matchers to prevent impacting
            // other tests
            throw new IllegalStateException("no last call on a mock available");
        }
        return (IExpectationSetters<T>) lastControl;
    }

    /**
     * Expects any boolean argument. For details, see the EasyMock
     * documentation.
     * 
     * @return <code>false</code>.
     */
    public static boolean anyBoolean() {
        reportMatcher(Any.ANY);
        return false;
    }

    /**
     * Expects any byte argument. For details, see the EasyMock documentation.
     * 
     * @return <code>0</code>.
     */
    public static byte anyByte() {
        reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * Expects any char argument. For details, see the EasyMock documentation.
     * 
     * @return <code>0</code>.
     */
    public static char anyChar() {
        reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * Expects any int argument. For details, see the EasyMock documentation.
     * 
     * @return <code>0</code>.
     */
    public static int anyInt() {
        reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * Expects any long argument. For details, see the EasyMock documentation.
     * 
     * @return <code>0</code>.
     */
    public static long anyLong() {
        reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * Expects any float argument. For details, see the EasyMock documentation.
     * 
     * @return <code>0</code>.
     */
    public static float anyFloat() {
        reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * Expects any double argument. For details, see the EasyMock documentation.
     * 
     * @return <code>0</code>.
     */
    public static double anyDouble() {
        reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * Expects any short argument. For details, see the EasyMock documentation.
     * 
     * @return <code>0</code>.
     */
    public static short anyShort() {
        reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * Expects any Object argument. For details, see the EasyMock documentation.
     * This matcher (and {@link #anyObject(Class)}) can be used in these three
     * ways:
     * <ul>
     * <li><code>(T)EasyMock.anyObject() // explicit cast</code></li>
     * <li>
     * <code>EasyMock.&lt;T&gt; anyObject() // fixing the returned generic</code>
     * </li>
     * <li>
     * <code>EasyMock.anyObject(T.class) // pass the returned type in parameter</code>
     * </li>
     * </ul>
     * 
     * @param <T>
     *            type of the method argument to match
     * @return <code>null</code>.
     */
    public static <T> T anyObject() {
        reportMatcher(Any.ANY);
        return null;
    }

    /**
     * Expects any Object argument. For details, see the EasyMock documentation.
     * To work well with generics, this matcher can be used in three different
     * ways. See {@link #anyObject()}.
     * 
     * @param <T>
     *            type of the method argument to match
     * @param clazz
     *            the class of the argument to match
     * @return <code>null</code>.
     */
    public static <T> T anyObject(final Class<T> clazz) {
        reportMatcher(Any.ANY);
        return null;
    }

    /**
     * Expect any string whatever its content is. Exactly the same as
     * {@link #anyObject()} but prevents typing issues for the much used String
     * type. Consider this method to be a syntactic sugar.
     * 
     * @return <code>null</code>.
     */
    public static String anyString() {
        return anyObject();
    }

    /**
     * Expects a comparable argument greater than or equal the given value. For
     * details, see the EasyMock documentation.
     * 
     * @param <T>
     *            type of the method argument to match
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T geq(final Comparable<T> value) {
        reportMatcher(new GreaterOrEqual<T>(value));
        return null;
    }

    /**
     * Expects a byte argument greater than or equal to the given value. For
     * details, see the EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte geq(final byte value) {
        reportMatcher(new GreaterOrEqual<Byte>(value));
        return 0;
    }

    /**
     * Expects a double argument greater than or equal to the given value. For
     * details, see the EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double geq(final double value) {
        reportMatcher(new GreaterOrEqual<Double>(value));
        return 0;
    }

    /**
     * Expects a float argument greater than or equal to the given value. For
     * details, see the EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float geq(final float value) {
        reportMatcher(new GreaterOrEqual<Float>(value));
        return 0;
    }

    /**
     * Expects an int argument greater than or equal to the given value. For
     * details, see the EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int geq(final int value) {
        reportMatcher(new GreaterOrEqual<Integer>(value));
        return 0;
    }

    /**
     * Expects a long argument greater than or equal to the given value. For
     * details, see the EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long geq(final long value) {
        reportMatcher(new GreaterOrEqual<Long>(value));
        return 0;
    }

    /**
     * Expects a short argument greater than or equal to the given value. For
     * details, see the EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short geq(final short value) {
        reportMatcher(new GreaterOrEqual<Short>(value));
        return 0;
    }

    /**
     * Expects a comparable argument less than or equal the given value. For
     * details, see the EasyMock documentation.
     * 
     * @param <T>
     *            type of the method argument to match
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T leq(final Comparable<T> value) {
        reportMatcher(new LessOrEqual<T>(value));
        return null;
    }

    /**
     * Expects a byte argument less than or equal to the given value. For
     * details, see the EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte leq(final byte value) {
        reportMatcher(new LessOrEqual<Byte>(value));
        return 0;
    }

    /**
     * Expects a double argument less than or equal to the given value. For
     * details, see the EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double leq(final double value) {
        reportMatcher(new LessOrEqual<Double>(value));
        return 0;
    }

    /**
     * Expects a float argument less than or equal to the given value. For
     * details, see the EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float leq(final float value) {
        reportMatcher(new LessOrEqual<Float>(value));
        return 0;
    }

    /**
     * Expects an int argument less than or equal to the given value. For
     * details, see the EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int leq(final int value) {
        reportMatcher(new LessOrEqual<Integer>(value));
        return 0;
    }

    /**
     * Expects a long argument less than or equal to the given value. For
     * details, see the EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long leq(final long value) {
        reportMatcher(new LessOrEqual<Long>(value));
        return 0;
    }

    /**
     * Expects a short argument less than or equal to the given value. For
     * details, see the EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short leq(final short value) {
        reportMatcher(new LessOrEqual<Short>(value));
        return 0;
    }

    /**
     * Expects a comparable argument greater than the given value. For details,
     * see the EasyMock documentation.
     * 
     * @param <T>
     *            type of the method argument to match
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T gt(final Comparable<T> value) {
        reportMatcher(new GreaterThan<T>(value));
        return null;
    }

    /**
     * Expects a byte argument greater than the given value. For details, see
     * the EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte gt(final byte value) {
        reportMatcher(new GreaterThan<Byte>(value));
        return 0;
    }

    /**
     * Expects a double argument greater than the given value. For details, see
     * the EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double gt(final double value) {
        reportMatcher(new GreaterThan<Double>(value));
        return 0;
    }

    /**
     * Expects a float argument greater than the given value. For details, see
     * the EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float gt(final float value) {
        reportMatcher(new GreaterThan<Float>(value));
        return 0;
    }

    /**
     * Expects an int argument greater than the given value. For details, see
     * the EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int gt(final int value) {
        reportMatcher(new GreaterThan<Integer>(value));
        return 0;
    }

    /**
     * Expects a long argument greater than the given value. For details, see
     * the EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long gt(final long value) {
        reportMatcher(new GreaterThan<Long>(value));
        return 0;
    }

    /**
     * Expects a short argument greater than the given value. For details, see
     * the EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short gt(final short value) {
        reportMatcher(new GreaterThan<Short>(value));
        return 0;
    }

    /**
     * Expects a comparable argument less than the given value. For details, see
     * the EasyMock documentation.
     * 
     * @param <T>
     *            type of the method argument to match
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T lt(final Comparable<T> value) {
        reportMatcher(new LessThan<T>(value));
        return null;
    }

    /**
     * Expects a byte argument less than the given value. For details, see the
     * EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte lt(final byte value) {
        reportMatcher(new LessThan<Byte>(value));
        return 0;
    }

    /**
     * Expects a double argument less than the given value. For details, see the
     * EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double lt(final double value) {
        reportMatcher(new LessThan<Double>(value));
        return 0;
    }

    /**
     * Expects a float argument less than the given value. For details, see the
     * EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float lt(final float value) {
        reportMatcher(new LessThan<Float>(value));
        return 0;
    }

    /**
     * Expects an int argument less than the given value. For details, see the
     * EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int lt(final int value) {
        reportMatcher(new LessThan<Integer>(value));
        return 0;
    }

    /**
     * Expects a long argument less than the given value. For details, see the
     * EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long lt(final long value) {
        reportMatcher(new LessThan<Long>(value));
        return 0;
    }

    /**
     * Expects a short argument less than the given value. For details, see the
     * EasyMock documentation.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short lt(final short value) {
        reportMatcher(new LessThan<Short>(value));
        return 0;
    }

    /**
     * Expects an object implementing the given class. For details, see the
     * EasyMock documentation.
     * 
     * @param <T>
     *            the accepted type.
     * @param clazz
     *            the class of the accepted type.
     * @return <code>null</code>.
     */
    public static <T> T isA(final Class<T> clazz) {
        reportMatcher(new InstanceOf(clazz));
        return null;
    }

    /**
     * Expects a string that contains the given substring. For details, see the
     * EasyMock documentation.
     * 
     * @param substring
     *            the substring.
     * @return <code>null</code>.
     */
    public static String contains(final String substring) {
        reportMatcher(new Contains(substring));
        return null;
    }

    /**
     * Expects a boolean that matches both given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>false</code>.
     */
    public static boolean and(final boolean first, final boolean second) {
        LastControl.reportAnd(2);
        return false;
    }

    /**
     * Expects a byte that matches both given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static byte and(final byte first, final byte second) {
        LastControl.reportAnd(2);
        return 0;
    }

    /**
     * Expects a char that matches both given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static char and(final char first, final char second) {
        LastControl.reportAnd(2);
        return 0;
    }

    /**
     * Expects a double that matches both given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static double and(final double first, final double second) {
        LastControl.reportAnd(2);
        return 0;
    }

    /**
     * Expects a float that matches both given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static float and(final float first, final float second) {
        LastControl.reportAnd(2);
        return 0;
    }

    /**
     * Expects an int that matches both given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static int and(final int first, final int second) {
        LastControl.reportAnd(2);
        return 0;
    }

    /**
     * Expects a long that matches both given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static long and(final long first, final long second) {
        LastControl.reportAnd(2);
        return 0;
    }

    /**
     * Expects a short that matches both given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static short and(final short first, final short second) {
        LastControl.reportAnd(2);
        return 0;
    }

    /**
     * Expects an Object that matches both given expectations.
     * 
     * @param <T>
     *            the type of the object, it is passed through to prevent casts.
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>null</code>.
     */
    public static <T> T and(final T first, final T second) {
        LastControl.reportAnd(2);
        return null;
    }

    /**
     * Expects a boolean that matches one of the given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>false</code>.
     */
    public static boolean or(final boolean first, final boolean second) {
        LastControl.reportOr(2);
        return false;
    }

    /**
     * Expects a byte that matches one of the given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static byte or(final byte first, final byte second) {
        LastControl.reportOr(2);
        return 0;
    }

    /**
     * Expects a char that matches one of the given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static char or(final char first, final char second) {
        LastControl.reportOr(2);
        return 0;
    }

    /**
     * Expects a double that matches one of the given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static double or(final double first, final double second) {
        LastControl.reportOr(2);
        return 0;
    }

    /**
     * Expects a float that matches one of the given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static float or(final float first, final float second) {
        LastControl.reportOr(2);
        return 0;
    }

    /**
     * Expects an int that matches one of the given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static int or(final int first, final int second) {
        LastControl.reportOr(2);
        return first;
    }

    /**
     * Expects a long that matches one of the given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static long or(final long first, final long second) {
        LastControl.reportOr(2);
        return 0;
    }

    /**
     * Expects a short that matches one of the given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static short or(final short first, final short second) {
        LastControl.reportOr(2);
        return 0;
    }

    /**
     * Expects an Object that matches one of the given expectations.
     * 
     * @param <T>
     *            the type of the object, it is passed through to prevent casts.
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>null</code>.
     */
    public static <T> T or(final T first, final T second) {
        LastControl.reportOr(2);
        return null;
    }

    /**
     * Expects a boolean that does not match the given expectation.
     * 
     * @param first
     *            placeholder for the expectation.
     * @return <code>false</code>.
     */
    public static boolean not(final boolean first) {
        LastControl.reportNot();
        return false;
    }

    /**
     * Expects a byte that does not match the given expectation.
     * 
     * @param first
     *            placeholder for the expectation.
     * @return <code>0</code>.
     */
    public static byte not(final byte first) {
        LastControl.reportNot();
        return 0;
    }

    /**
     * Expects a char that does not match the given expectation.
     * 
     * @param first
     *            placeholder for the expectation.
     * @return <code>0</code>.
     */
    public static char not(final char first) {
        LastControl.reportNot();
        return 0;
    }

    /**
     * Expects a double that does not match the given expectation.
     * 
     * @param first
     *            placeholder for the expectation.
     * @return <code>0</code>.
     */
    public static double not(final double first) {
        LastControl.reportNot();
        return 0;
    }

    /**
     * Expects a float that does not match the given expectation.
     * 
     * @param first
     *            placeholder for the expectation.
     * @return <code>0</code>.
     */
    public static float not(final float first) {
        LastControl.reportNot();
        return first;
    }

    /**
     * Expects an int that does not match the given expectation.
     * 
     * @param first
     *            placeholder for the expectation.
     * @return <code>0</code>.
     */
    public static int not(final int first) {
        LastControl.reportNot();
        return 0;
    }

    /**
     * Expects a long that does not match the given expectation.
     * 
     * @param first
     *            placeholder for the expectation.
     * @return <code>0</code>.
     */
    public static long not(final long first) {
        LastControl.reportNot();
        return 0;
    }

    /**
     * Expects a short that does not match the given expectation.
     * 
     * @param first
     *            placeholder for the expectation.
     * @return <code>0</code>.
     */
    public static short not(final short first) {
        LastControl.reportNot();
        return 0;
    }

    /**
     * Expects an Object that does not match the given expectation.
     * 
     * @param <T>
     *            the type of the object, it is passed through to prevent casts.
     * @param first
     *            placeholder for the expectation.
     * @return <code>null</code>.
     */
    public static <T> T not(final T first) {
        LastControl.reportNot();
        return null;
    }

    /**
     * Expects a boolean that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static boolean eq(final boolean value) {
        reportMatcher(new Equals(value));
        return false;
    }

    /**
     * Expects a byte that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte eq(final byte value) {
        reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * Expects a char that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static char eq(final char value) {
        reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * Expects a double that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double eq(final double value) {
        reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * Expects a float that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float eq(final float value) {
        reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * Expects an int that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int eq(final int value) {
        reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * Expects a long that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long eq(final long value) {
        reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * Expects a short that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short eq(final short value) {
        reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * Expects an Object that is equal to the given value.
     * 
     * @param <T>
     *            type of the method argument to match
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T> T eq(final T value) {
        reportMatcher(new Equals(value));
        return null;
    }

    /**
     * Expects a boolean array that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static boolean[] aryEq(final boolean[] value) {
        reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * Expects a byte array that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static byte[] aryEq(final byte[] value) {
        reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * Expects a char array that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static char[] aryEq(final char[] value) {
        reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * Expects a double array that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static double[] aryEq(final double[] value) {
        reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * Expects a float array that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static float[] aryEq(final float[] value) {
        reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * Expects an int array that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static int[] aryEq(final int[] value) {
        reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * Expects a long array that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static long[] aryEq(final long[] value) {
        reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * Expects a short array that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static short[] aryEq(final short[] value) {
        reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * Expects an Object array that is equal to the given array, i.e. it has to
     * have the same type, length, and each element has to be equal.
     * 
     * @param <T>
     *            the type of the array, it is passed through to prevent casts.
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static <T> T[] aryEq(final T[] value) {
        reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * Expects null. To work well with generics, this matcher (and
     * {@link #isNull(Class)}) can be used in these three ways:
     * <ul>
     * <li><code>(T)EasyMock.isNull() // explicit cast</code></li>
     * <li>
     * <code>EasyMock.&lt;T&gt; isNull() // fixing the returned generic</code></li>
     * <li>
     * <code>EasyMock.isNull(T.class) // pass the returned type in parameter</code>
     * </li>
     * </ul>
     * 
     * @param <T>
     *            type of the method argument to match
     * @return <code>null</code>.
     */
    public static <T> T isNull() {
        reportMatcher(Null.NULL);
        return null;
    }

    /**
     * Expects null. To work well with generics, this matcher can be used in
     * three different ways. See {@link #isNull()}.
     * 
     * @param <T>
     *            type of the method argument to match
     * @param clazz
     *            the class of the argument to match
     * @return <code>null</code>.
     * 
     * @see #isNull()
     */
    public static <T> T isNull(final Class<T> clazz) {
        reportMatcher(Null.NULL);
        return null;
    }

    /**
     * Expects not null. To work well with generics, this matcher (and
     * {@link #notNull(Class)}) can be used in these three ways:
     * <ul>
     * <li><code>(T)EasyMock.notNull() // explicit cast</code></li>
     * <li>
     * <code>EasyMock.&lt;T&gt; notNull() // fixing the returned generic</code></li>
     * <li>
     * <code>EasyMock.notNull(T.class) // pass the returned type in parameter</code>
     * </li>
     * </ul>
     * 
     * @param <T>
     *            type of the method argument to match
     * @return <code>null</code>.
     */
    public static <T> T notNull() {
        reportMatcher(NotNull.NOT_NULL);
        return null;
    }

    /**
     * Expects not null. To work well with generics, this matcher can be used in
     * three different ways. See {@link #notNull()}.
     * 
     * @param <T>
     *            type of the method argument to match
     * @param clazz
     *            the class of the argument to match
     * @return <code>null</code>.
     * 
     * @see #notNull()
     */
    public static <T> T notNull(final Class<T> clazz) {
        reportMatcher(NotNull.NOT_NULL);
        return null;
    }

    /**
     * Expects a string that contains a substring that matches the given regular
     * expression. For details, see the EasyMock documentation.
     * 
     * @param regex
     *            the regular expression.
     * @return <code>null</code>.
     */
    public static String find(final String regex) {
        reportMatcher(new Find(regex));
        return null;
    }

    /**
     * Expects a string that matches the given regular expression. For details,
     * see the EasyMock documentation.
     * 
     * @param regex
     *            the regular expression.
     * @return <code>null</code>.
     */
    public static String matches(final String regex) {
        reportMatcher(new Matches(regex));
        return null;
    }

    /**
     * Expects a string that starts with the given prefix. For details, see the
     * EasyMock documentation.
     * 
     * @param prefix
     *            the prefix.
     * @return <code>null</code>.
     */
    public static String startsWith(final String prefix) {
        reportMatcher(new StartsWith(prefix));
        return null;
    }

    /**
     * Expects a string that ends with the given suffix. For details, see the
     * EasyMock documentation.
     * 
     * @param suffix
     *            the suffix.
     * @return <code>null</code>.
     */
    public static String endsWith(final String suffix) {
        reportMatcher(new EndsWith(suffix));
        return null;
    }

    /**
     * Expects a double that has an absolute difference to the given value that
     * is less than the given delta. For details, see the EasyMock
     * documentation.
     * 
     * @param value
     *            the given value.
     * @param delta
     *            the given delta.
     * @return <code>0</code>.
     */
    public static double eq(final double value, final double delta) {
        reportMatcher(new EqualsWithDelta(value, delta));
        return 0;
    }

    /**
     * Expects a float that has an absolute difference to the given value that
     * is less than the given delta. For details, see the EasyMock
     * documentation.
     * 
     * @param value
     *            the given value.
     * @param delta
     *            the given delta.
     * @return <code>0</code>.
     */
    public static float eq(final float value, final float delta) {
        reportMatcher(new EqualsWithDelta(value, delta));
        return 0;
    }

    /**
     * Expects an Object that is the same as the given value. For details, see
     * the EasyMock documentation.
     * 
     * @param <T>
     *            the type of the object, it is passed through to prevent casts.
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T> T same(final T value) {
        reportMatcher(new Same(value));
        return null;
    }

    /**
     * Expects a comparable argument equals to the given value according to
     * their compareTo method. For details, see the EasMock documentation.
     * 
     * @param <T>
     *            type of the method argument to match
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T cmpEq(final Comparable<T> value) {
        reportMatcher(new CompareEqual<T>(value));
        return null;
    }

    /**
     * Expects an argument that will be compared using the provided comparator.
     * The following comparison will take place:
     * <p>
     * <code>comparator.compare(actual, expected) operator 0</code>
     * </p>
     * For details, see the EasyMock documentation.
     * 
     * @param <T>
     *            type of the method argument to match
     * @param value
     *            the given value.
     * @param comparator
     *            Comparator used to compare the actual with expected value.
     * @param operator
     *            The comparison operator.
     * @return <code>null</code>
     */
    public static <T> T cmp(final T value, final Comparator<? super T> comparator,
            final LogicalOperator operator) {
        reportMatcher(new Compare<T>(value, comparator, operator));
        return null;
    }

    /**
     * Expect any object but captures it for later use.
     * 
     * @param <T>
     *            Type of the captured object
     * @param captured
     *            Where the parameter is captured
     * @return <code>null</code>
     */
    public static <T> T capture(final Capture<T> captured) {
        reportMatcher(new Captures<T>(captured));
        return null;
    }

    /**
     * Expect any boolean but captures it for later use.
     * 
     * @param captured
     *            Where the parameter is captured
     * @return <code>false</code>
     */
    public static boolean captureBoolean(final Capture<Boolean> captured) {
        reportMatcher(new Captures<Boolean>(captured));
        return false;
    }

    /**
     * Expect any int but captures it for later use.
     * 
     * @param captured
     *            Where the parameter is captured
     * @return <code>0</code>
     */
    public static int captureInt(final Capture<Integer> captured) {
        reportMatcher(new Captures<Integer>(captured));
        return 0;
    }

    /**
     * Expect any long but captures it for later use.
     * 
     * @param captured
     *            Where the parameter is captured
     * @return <code>0</code>
     */
    public static long captureLong(final Capture<Long> captured) {
        reportMatcher(new Captures<Long>(captured));
        return 0;
    }

    /**
     * Expect any float but captures it for later use.
     * 
     * @param captured
     *            Where the parameter is captured
     * @return <code>0</code>
     */
    public static float captureFloat(final Capture<Float> captured) {
        reportMatcher(new Captures<Float>(captured));
        return 0;
    }

    /**
     * Expect any double but captures it for later use.
     * 
     * @param captured
     *            Where the parameter is captured
     * @return <code>0</code>
     */
    public static double captureDouble(final Capture<Double> captured) {
        reportMatcher(new Captures<Double>(captured));
        return 0;
    }

    /**
     * Expect any byte but captures it for later use.
     * 
     * @param captured
     *            Where the parameter is captured
     * @return <code>0</code>
     */
    public static byte captureByte(final Capture<Byte> captured) {
        reportMatcher(new Captures<Byte>(captured));
        return 0;
    }

    /**
     * Expect any char but captures it for later use.
     * 
     * @param captured
     *            Where the parameter is captured
     * @return <code>0</code>
     */
    public static char captureChar(final Capture<Character> captured) {
        reportMatcher(new Captures<Character>(captured));
        return 0;
    }

    /**
     * Switches the given mock objects (more exactly: the controls of the mock
     * objects) to replay mode. For details, see the EasyMock documentation.
     * 
     * @param mocks
     *            the mock objects.
     */
    public static void replay(final Object... mocks) {
        for (final Object mock : mocks) {
            getControl(mock).replay();
        }
    }

    /**
     * Resets the given mock objects (more exactly: the controls of the mock
     * objects). For details, see the EasyMock documentation.
     * 
     * @param mocks
     *            the mock objects.
     */
    public static void reset(final Object... mocks) {
        for (final Object mock : mocks) {
            getControl(mock).reset();
        }
    }

    /**
     * Resets the given mock objects (more exactly: the controls of the mock
     * objects) and turn them to a mock with nice behavior. For details, see the
     * EasyMock documentation.
     * 
     * @param mocks
     *            the mock objects
     */
    public static void resetToNice(final Object... mocks) {
        for (final Object mock : mocks) {
            getControl(mock).resetToNice();
        }
    }

    /**
     * Resets the given mock objects (more exactly: the controls of the mock
     * objects) and turn them to a mock with default behavior. For details, see
     * the EasyMock documentation.
     * 
     * @param mocks
     *            the mock objects
     */
    public static void resetToDefault(final Object... mocks) {
        for (final Object mock : mocks) {
            getControl(mock).resetToDefault();
        }
    }

    /**
     * Resets the given mock objects (more exactly: the controls of the mock
     * objects) and turn them to a mock with strict behavior. For details, see
     * the EasyMock documentation.
     * 
     * @param mocks
     *            the mock objects
     */
    public static void resetToStrict(final Object... mocks) {
        for (final Object mock : mocks) {
            getControl(mock).resetToStrict();
        }
    }

    /**
     * Verifies the given mock objects (more exactly: the controls of the mock
     * objects).
     * 
     * @param mocks
     *            the mock objects.
     */
    public static void verify(final Object... mocks) {
        for (final Object mock : mocks) {
            getControl(mock).verify();
        }
    }

    /**
     * Switches order checking of the given mock object (more exactly: the
     * control of the mock object) the on and off. For details, see the EasyMock
     * documentation.
     * 
     * @param mock
     *            the mock object.
     * @param state
     *            <code>true</code> switches order checking on,
     *            <code>false</code> switches it off.
     */
    public static void checkOrder(final Object mock, final boolean state) {
        getControl(mock).checkOrder(state);
    }

    /**
     * Reports an argument matcher. This method is needed to define own argument
     * matchers. For details, see the EasyMock documentation.
     * 
     * @param matcher
     */
    public static void reportMatcher(final IArgumentMatcher matcher) {
        LastControl.reportMatcher(matcher);
    }

    private static MocksControl getControl(final Object mock) {
        return MocksControl.getControl(mock);
    }

    /**
     * Returns the arguments of the current mock method call, if inside an
     * <code>IAnswer</code> callback - be careful here, reordering parameters of
     * method changes the semantics of your tests.
     * 
     * @return the arguments of the current mock method call.
     * @throws IllegalStateException
     *             if called outside of <code>IAnswer</code> callbacks.
     */
    public static Object[] getCurrentArguments() {
        final Invocation result = LastControl.getCurrentInvocation();
        if (result == null) {
            throw new IllegalStateException(
                    "current arguments are only available when executing callback methods");
        }
        return result.getArguments();
    }

    /**
     * By default, a mock is thread safe (unless
     * {@link #NOT_THREAD_SAFE_BY_DEFAULT} is set). This method can change this
     * behavior. Two reasons are known for someone to do that: Performance or
     * dead-locking issues.
     * 
     * @param mock
     *            the mock to make thread safe
     * @param threadSafe
     *            If the mock should be thread safe or not
     */
    public static void makeThreadSafe(final Object mock, final boolean threadSafe) {
        getControl(mock).makeThreadSafe(threadSafe);
    }

    /**
     * Tell that the mock should be used in only one thread. An exception will
     * be thrown if that's not the case. This can be useful when mocking an
     * object that isn't thread safe to make sure it is used correctly in a
     * multithreaded environment. By default, no check is done unless
     * {@link #ENABLE_THREAD_SAFETY_CHECK_BY_DEFAULT} was set to true.
     * 
     * @param mock
     *            the mock
     * @param shouldBeUsedInOneThread
     *            If the mock should be used in only one thread
     */
    public static void checkIsUsedInOneThread(final Object mock, final boolean shouldBeUsedInOneThread) {
        getControl(mock).checkIsUsedInOneThread(shouldBeUsedInOneThread);
    }

    /**
     * Get the current value for an EasyMock property
     * 
     * @param key
     *            key for the property
     * @return the property value
     */
    public static String getEasyMockProperty(final String key) {
        return EasyMockProperties.getInstance().getProperty(key);
    }

    /**
     * Set a property to modify the default EasyMock behavior. These properties
     * can also be set as System properties or in easymock.properties. This
     * method can then be called to overload them. For details and a list of
     * available properties see the EasyMock documentation.
     * <p>
     * <b>Note:</b> This method is static. Setting a property will change the
     * entire EasyMock behavior.
     * 
     * @param key
     *            property key
     * @param value
     *            property value. A null value will remove the property
     * @return the previous property value
     */
    public static String setEasyMockProperty(final String key, final String value) {
        return EasyMockProperties.getInstance().setProperty(key, value);
    }

    // ///CLOVER:OFF
    protected EasyMock() { // Need to be protected since the Class extension extends it
    }
    // ///CLOVER:ON
}
