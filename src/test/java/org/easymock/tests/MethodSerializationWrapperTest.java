package org.easymock.tests;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.easymock.internal.MethodSerializationWrapper;
import org.junit.Test;

public class MethodSerializationWrapperTest {

    public static class A {
        public void foo(String s, int i, String[] sArray, int[] iArray, String...varargs ) {            
        }
    }
    
    @Test
    public void testGetMethod() throws Exception {
        Method foo = A.class.getMethod("foo", String.class, Integer.TYPE, String[].class, int[].class, String[].class);
        MethodSerializationWrapper wrapper = new MethodSerializationWrapper(foo);
        assertEquals(foo, wrapper.getMethod());
    }

}
