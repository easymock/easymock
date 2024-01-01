/*
 * Copyright 2001-2024 the original author or authors.
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

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.modifier.FieldManifestation;
import net.bytebuddy.description.modifier.SyntheticState;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.easymock.EasyMock;
import org.easymock.internal.ClassInstantiatorFactory;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This test case is used to make sure that the way ByteBuddy is used is providing
 * the expected behavior
 *
 * @author Henri Tremblay
 */
public class ByteBuddyTest {

    /**
     * Check that an interceptor is used by only one instance of a class
     *
     * @throws Exception just a test
     */
    @Test
    public void test() throws Exception {
        Object f1 = createMock();
        Object f2 = createMock();

        assertNotSame(getCallback(f1), getCallback(f2));
    }

    private Object createMock() throws Exception {
        ElementMatcher.Junction<MethodDescription> junction = ElementMatchers.any();
        @SuppressWarnings("Convert2Lambda") // if a lambda is used, since it is stateless, the same instance will be reused. This will defeat the purpose of this test
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(proxy, args);
            }
        };
        Class<?> mockClass = new ByteBuddy()
            .subclass(ArrayList.class)
            .defineField("$callback", InvocationHandler.class, SyntheticState.SYNTHETIC, Visibility.PRIVATE, FieldManifestation.FINAL)
            .method(junction)
            .intercept(InvocationHandlerAdapter.of(handler))
            .make()
            .load(ArrayList.class.getClassLoader(), new ClassLoadingStrategy.ForUnsafeInjection())
            .getLoaded();

        Object mock = ClassInstantiatorFactory.getInstantiator().newInstance(mockClass);

        Field callbackField = getCallbackField(mock);
        try {
            callbackField.set(mock, handler);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return mock;
    }

    private InvocationHandler getCallback(Object mock) {
        try {
            return (InvocationHandler) getCallbackField(mock).get(mock);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Field getCallbackField(Object mock) {
        Field field;
        try {
            field = mock.getClass().getDeclaredField("$callback");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(true);
        return field;
    }

    /**
     * Mocking the File class isn't working when using an old ASM version. So we make sure our version works
     * fine
     */
    @Test
    public void testJava8() {
        File file = EasyMock.createMock(File.class);
        EasyMock.expect(file.canExecute()).andReturn(true);
        EasyMock.replay(file);
        assertTrue(file.canExecute());
        EasyMock.verify(file);
    }
}
