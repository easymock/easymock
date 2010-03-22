/**
 * Copyright 2003-2009 OFFIS, Henri Tremblay
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

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

/**
 * Bridges are generated method used for generics. They shouldn't be mocked to
 * keep delegating correctly to the real implementation.
 */
public class GenericTest {

    public interface C<U> {
        public void doCMethod(U u);
    }

    public class B implements C<Integer> {
        public void doCMethod(Integer u) {
            fail("Should be mocked");
        }
    }

    @Test
    public void testBridgeUnmocked() {
        B b = createMock(B.class);
        b.doCMethod(new Integer(6));
        replay(b);
        ((C<Integer>) b).doCMethod(new Integer(6));
        verify(b);
    }
    
    static abstract class AbstractFoo {
        public Collection<String> getSomeStrings() {
            return null;
        }

        public abstract Collection<Integer> getSomeIntegers();
    }
    
    public class ConcreteFoo extends AbstractFoo {
        public Collection<Integer> getSomeIntegers() {
            return null;
        }
    }
    
    /**
     * The JDK (and not Eclipse) creates a bridge in a concrete class
     * extending a package scope one. In this case, the bridge contains
     * a call to the super method. So we want to make sure the mocking 
     * is forwarding correctly. 
     */
    @Test
    public void testPackageScope() {
        ConcreteFoo b = createMock(ConcreteFoo.class);
        expect(b.getSomeStrings()).andReturn(null);
        replay(b);
        ((AbstractFoo) b).getSomeStrings();
        verify(b);
    }
}
