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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Helps the creation of partial mocks with {@link EasyMock}.
 * 
 * <p>
 * Example of usage:
 * 
 * <pre>
 * public class MyClass {
 *     public MyClass(A a, B b) {
 *     }
 * }
 * 
 * public class MyClassTest {
 *     &#064;Test
 *     public void testFoo() throws Exception {
 *         IMocksControl mockControl = createControl();
 *         A a = mockControl.createMock(A.class);
 *         B b = mockControl.createMock(B.class);
 * 
 *         MyClass myClass = createMockBuilder(MyClass.class)
 *                 .withConstructor(a, b).createMock(mockControl);
 * 
 *         // Set the expectations of A and B and test some method in MyClass
 *     }
 * }
 * </pre>
 * 
 * <p>
 * This class also has support for partial mocks as shown by the example below:
 * 
 * <pre>
 * public class MyMockedClass {
 *     // Empty class is also valid for {@link IMockBuilder}.
 *     public MyMockedClass() {
 *     }
 * 
 *     public void foo(int a) {
 *         blah(a);
 *         bleh();
 *     }
 * 
 *     public void blah(int a) {
 *     }
 * 
 *     public void bleh() {
 *     }
 * }
 * 
 * public class MyMockedClassTest {
 *     &#064;Test
 *     public void testFoo() throws Exception {
 *         MyMockedClass myMockedClass = createMockBuilder(MyMockedClass.class)
 *                 .withConstructor().addMockedMethod(&quot;blah&quot;, int.class)
 *                 .addMockedMethod(&quot;bleh&quot;).createMock();
 * 
 *         // These are the expectations.
 *         myMockedClass.blah(1);
 *         myMockedClass.bleh();
 *         replay(myMockedClass);
 * 
 *         myMockedClass.foo(1);
 *         verify(myMockedClass);
 *     }
 * }
 * </pre>
 * 
 * <p>
 * Warning: There may be ambiguities when there are two different constructors
 * with compatible types. For instance:
 * 
 * <pre>
 * public class A {
 * }
 * 
 * public class B extends A {
 * }
 * 
 * public class ClassWithAmbiguity {
 *     public ClassWithAmbiguity(A a) {
 *     }
 * 
 *     public ClassWithAmbiguity(B b) {
 *     }
 * }
 * </pre>
 * 
 * will cause problems if using {@link #withConstructor(Object...)}. To solve
 * this, you can explicitly define the constructor parameter types to use by
 * calling {@link #withConstructor(Class...)} and then
 * {@link #withArgs(Object...)}, like this:
 * 
 * <pre>
 * createMockBuilder(MyMockedClass.class).withConstructor(A.class).withArgs(
 *         new A()).createMock();
 * </pre>
 * 
 * @param <T>
 *            type of the object being created
 * 
 * @author Henri Tremblay
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
    IMockBuilder<T> addMockedMethod(String methodName, Class<?>... parameterTypes);

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
     * Defines the empty constructor should be called.
     * 
     * @return this
     */
    IMockBuilder<T> withConstructor();

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
     * Create mock of the request type from this builder. The same builder can be called to
     * create multiple mocks.
     * 
     * @param type the mock type
     * @return the newly created mock
     * @since 3.2
     */
    T createMock(MockType type);

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
     * @param control
     *            {@link org.easymock.IMocksControl} used to create the object
     * @return the newly created mock
     */
    T createMock(IMocksControl control);

    /**
     * Create a named mock of the request type from this builder. The same builder can be 
     * called to create multiple mocks.
     * 
     * @param name
     *            the mock name
     * @param type
     *            the mock type
     * @return the newly created mock
     * @since 3.2
     */
    T createMock(String name, MockType type);

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
     * @param control
     *            {@link org.easymock.IMocksControl} used to create the object
     * @return the newly created mock
     */
    T createMock(String name, IMocksControl control);
}
