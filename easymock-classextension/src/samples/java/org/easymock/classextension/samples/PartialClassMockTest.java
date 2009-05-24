/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.classextension.samples;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Henri Tremblay
 */
public class PartialClassMockTest {

    public static class Rect {

        private int x;

        private int y;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getArea() {
            return getX() * getY();
        }
    }

    private Rect rect;

    @Before
    public void setUp() throws Exception {
        rect = createMock(Rect.class, Rect.class.getMethod("getX",
                (Class[]) null), Rect.class.getMethod("getY", (Class[]) null));
    }

    @After
    public void tearDown() {
        rect = null;
    }

    @Test
    public void testGetArea() {
        expect(rect.getX()).andReturn(4);
        expect(rect.getY()).andReturn(5);
        replay(rect);
        assertEquals(20, rect.getArea());
        verify(rect);
    }
}
