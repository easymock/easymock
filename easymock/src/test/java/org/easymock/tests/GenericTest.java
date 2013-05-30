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

import org.easymock.tests2.ChildEquals;
import org.junit.Test;

import java.util.Collection;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * Bridges are generated methods used for generics. They shouldn't be mocked to
 * keep delegating correctly to the real implementation.
 * 
 * @author Henri Tremblay
 */
public class GenericTest {

    public interface C<U> {
        public void doCMethod(U u);
    }

    public class B implements C<Integer> {
        public void doCMethod(final Integer u) {
            fail("Should be mocked");
        }
    }

    @Test
    public void testTheBridgeMethodIsRecordedNotTheBridge() {
        final B b = createMock(B.class);
        b.doCMethod(Integer.valueOf(6));
        replay(b);
        ((C<Integer>) b).doCMethod(Integer.valueOf(6));
        verify(b);
    }

    /**
     * Test cglib bug. See ClassProxyFactory.intercept for details
     */
    @Test
    public void testPartialMockingSeesBridgeHasUnmocked() {
        final ConcreteFoo b = createMockBuilder(ConcreteFoo.class).addMockedMethod("getSomeStrings")
                .createMock();
        final AbstractFoo c = b;
        expect(c.getSomeStrings()).andReturn(null);
    }

    static abstract class AbstractFoo {
        public Collection<String> getSomeStrings() {
            fail("Should be mocked");
            return null;
        }
    }

    public static class ConcreteFoo extends AbstractFoo {
    }

    /**
     * The JDK (and not Eclipse) creates a bridge in a concrete class extending
     * a package scope one. In this case, the bridge contains a call to the
     * super method. So we want to make sure the mocking is forwarding
     * correctly.
     */
    @Test
    public void testPackageScope() {
        final ConcreteFoo b = createMock(ConcreteFoo.class);
        expect(b.getSomeStrings()).andReturn(null);
        replay(b);
        ((AbstractFoo) b).getSomeStrings();
        verify(b);
    }

    static abstract class GenericHolder<T> {
        abstract void set(T value);

        void go(final T value) {
            set(value);
        }
    }

    public static class StringHolder extends GenericHolder<String> {
        private String value;

        @Override
        void set(final String value) {
            this.value = value;
        }
    }

    /**
     * This test makes sure that a bridge method won't be considered mocked.
     * Bridge are never mocked. Only the underlying method should be.
     */
    @Test
    public void testPartialMockBridgeMethodAreUnmocked() {
        final StringHolder holder = createMockBuilder(StringHolder.class).createMock();
        holder.go("hello");
        assertEquals("hello", holder.value);
    }

    @Test
    public void testBridgedEquals() {
        ChildEquals c = createMock(ChildEquals.class);
        assertTrue(c.equals(c));

    }
}
