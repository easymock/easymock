/**
 * Copyright 2003-2010 the original author or authors.
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

package org.easymock.classextension.tests;

import static org.junit.Assert.*;

import java.util.AbstractList;

import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import org.junit.Test;

/**
 * Test the limitations of class mocking
 * 
 * @author Henri Tremblay
 */
@SuppressWarnings("deprecation")
public class LimitationsTest {

    public static class MyClass {
        public final int foo() {
            throw new RuntimeException();
        }
    }

    public static class PrivateClass {
        private PrivateClass() {
        }
    }
    
    public static class NativeClass {
        public native int foo();
    }

    public void finalClass() {
        try {
            MockClassControl.createControl(String.class);
            fail("Magic, we can mock a final class");
        } catch (Exception e) {
        }
    }

    @Test
    public void abstractClass() {
        MockControl<?> ctrl = MockClassControl.createControl(AbstractList.class);
        assertTrue(ctrl.getMock() instanceof AbstractList<?>);
    }

    @Test
    public void mockFinalMethod() {
        MockControl<MyClass> ctrl = MockClassControl
                .createControl(MyClass.class);
        MyClass c = ctrl.getMock();

        try {
            c.foo();
            fail("Final method shouldn't be mocked");
        } catch (Exception e) {
        }
    }

    @Test
    public void privateConstructor() {
        MockClassControl.createControl(PrivateClass.class);
    }
    
    @Test
    public void mockNativeMethod() {
        MockControl<NativeClass> ctrl = MockClassControl.createControl(NativeClass.class);        
        NativeClass mock = ctrl.getMock();
        ctrl.expectAndReturn(mock.foo(), 1);
        ctrl.replay();
        assertEquals(1, mock.foo());
        ctrl.verify();
    }
}
