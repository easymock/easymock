/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
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
