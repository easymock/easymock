package org.easymock.internal;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;

public class ClassProxyFactoryTest {

    @Test
    public void test() throws Exception {
        Class<?> clazz = new ByteBuddy()
            .subclass(getClass())
            .method(any())
            .intercept(InvocationHandlerAdapter.of(new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("Allo");
                    return null;
                }
            }))
            .make()
            .load(getClass().getClassLoader())
            .getLoaded();
        System.out.println(clazz.getName());
        ClassProxyFactoryTest t = (ClassProxyFactoryTest) clazz.newInstance();
        t.hello();
    }

    public void hello() {
        System.out.println("Hello");
    }
}
