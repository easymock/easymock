/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.classextension;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Helps the creation of partial mocks with
 * {@link org.easymock.classextension.EasyMock}.
 */
public interface IMockBuilder<T> {

    /**
     * Adds a method to be mocked in the testing class. Each call will add a new
     * method to the result mock.
     * 
     * The method is searched for in the class itself as well as superclasses.
     * 
     * @param method
     *            method to be mocked
     * @return this
     */
    IMockBuilder<T> addMockedMethod(Method method);

    /**
     * Adds a method to be mocked in the testing class. Each call will add a new
     * method to the result mock.
     * 
     * The method is searched for in the class itself as well as superclasses.
     * 
     * There must be no overload of the method. You will have to rely on the
     * other <code>addMockedMethod</code>s in this class if that is the case.
     * 
     * @param methodName
     *            name of the method to be mocked
     * @return this
     */
    IMockBuilder<T> addMockedMethod(String methodName);

    /**
     * Adds a method to be mocked in the testing class. Each call will add a new
     * method to the result mock.
     * 
     * The method is searched for in the class itself as well as superclasses.
     * 
     * @param methodName
     *            name of the method to be mocked
     * @param parameterTypes
     *            types of the parameters of the method
     * @return this
     */
    IMockBuilder<T> addMockedMethod(String methodName,
            Class<?>... parameterTypes);

    /**
     * Adds methods to be mocked in the testing class. Same as
     * {@link #addMockedMethod(String)} but to mock many methods at once.
     * 
     * @param methodNames
     *            names of the methods to be mocked
     * @return this
     */
    IMockBuilder<T> addMockedMethods(String... methodNames);

    /**
     * Adds methods to be mocked in the testing class. Same as
     * {@link #addMockedMethod(Method)} but to mock many methods at once.
     * 
     * @param methods
     *            methods to be mocked
     * @return this
     */
    IMockBuilder<T> addMockedMethods(Method... methods);

    /**
     * Defines the constructor to use to instantiate the mock. It is expected
     * that you call {@link #withArgs} with the actual constructor argument
     * values after this.
     * 
     * @param constructor
     *            the constructor to be called
     * @return this
     */
    IMockBuilder<T> withConstructor(Constructor<?> constructor);

    /**
     * Defines the constructor and arguments to use to instantiate the mock.
     * 
     * @param constructorArgs
     *            constructor and arguments
     * @return this
     */
    IMockBuilder<T> withConstructor(ConstructorArgs constructorArgs);

    /**
     * Defines the constructor parameters for the mocked class. The builder will
     * automatically find a constructor with compatible argument types. This
     * throws an exception if there is more than one constructor which would
     * accept the given parameters.
     * 
     * @param initArgs
     *            arguments of the constructor
     * @return this
     */
    IMockBuilder<T> withConstructor(Object... initArgs);

    /**
     * Defines the exact argument types for the constructor to use. It is
     * expected that you call {@link #withArgs} with the actual constructor
     * argument values after this.
     * 
     * @param argTypes
     *            the exact argument types of the constructor
     * @return this
     */
    IMockBuilder<T> withConstructor(Class<?>... argTypes);

    /**
     * Defines the arguments to be passed to the constructor of the class. The
     * types of the arguments must match those previously defined with
     * {@link #withConstructor(Class...)} or
     * {@link #withConstructor(Constructor)}.
     * 
     * @param initArgs
     *            the arguments to pass to the constructor
     * @return this
     */
    IMockBuilder<T> withArgs(Object... initArgs);

    /**
     * Create a strict mock from this builder. The same builder can be called to
     * create multiple mocks.
     * 
     * @return the newly created mock
     */
    T createStrictMock();

    /**
     * Create a default mock from this builder. The same builder can be called
     * to create multiple mocks.
     * 
     * @return the newly created mock
     */
    T createMock();

    /**
     * Create a nice mock from this builder. The same builder can be called to
     * create multiple mocks.
     * 
     * @return the newly created mock
     */
    T createNiceMock();

    /**
     * Create mock from the provided mock control using the arguments passed to
     * the builder.
     * 
     * @return the newly created mock
     */
    T createMock(IMocksControl control);

    /**
     * Create a named strict mock from this builder. The same builder can be
     * called to create multiple mocks.
     * 
     * @param name
     *            the mock name
     * @return the newly created mock
     */
    T createStrictMock(String name);

    /**
     * Create named mock from the provided mock control using the arguments
     * passed to the builder.
     * 
     * @param name
     *            the mock name
     * @return the newly created mock
     */
    T createMock(String name);

    /**
     * Create a named nice mock from this builder. The same builder can be
     * called to create multiple mocks.
     * 
     * @param name
     *            the mock name
     * @return the newly created mock
     */
    T createNiceMock(String name);

    /**
     * Create named mock from the provided mock control using the arguments
     * passed to the builder.
     * 
     * @param name
     *            the mock name
     * @return the newly created mock
     */
    T createMock(String name, IMocksControl control);
}
