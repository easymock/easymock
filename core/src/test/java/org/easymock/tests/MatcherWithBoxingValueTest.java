package org.easymock.tests;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class MatcherWithBoxingValueTest {

    static class ClassToMock {
        public int checkLong(long lvalue) {
            return 0;
        }
    }

    @Test
    public void testAnyObject() {
        ClassToMock mock = createMock(ClassToMock.class);
        expect(mock.checkLong(anyObject(Long.class))).andReturn(1);
    }

    @Test
    public void testGeq() {
        ClassToMock mock = createMock(ClassToMock.class);
        Long longObject = 101L;
        expect(mock.checkLong(geq(longObject))).andReturn(1);
    }

    @Test
    public void testLeq() {
        ClassToMock mock = createMock(ClassToMock.class);
        Long longObject = 101L;
        expect(mock.checkLong(leq(longObject))).andReturn(1);
    }

    @Test
    public void testGt() {
        ClassToMock mock = createMock(ClassToMock.class);
        Long longObject = 101L;
        expect(mock.checkLong(gt(longObject))).andReturn(1);
    }

    @Test
    public void testLt() {
        ClassToMock mock = createMock(ClassToMock.class);
        Long longObject = 101L;
        expect(mock.checkLong(lt(longObject))).andReturn(1);
    }

    @Test
    public void testIsA() {
        ClassToMock mock = createMock(ClassToMock.class);
        expect(mock.checkLong(isA(Long.class))).andReturn(1);
    }

    @Test
    public void testEq() {
        ClassToMock mock = createMock(ClassToMock.class);
        Long longObject = 101L;
        expect(mock.checkLong(eq(longObject))).andReturn(1);
    }
    
    @Test
    public void testSame() {
        ClassToMock mock = createMock(ClassToMock.class);
        Long longObject = 101L;
        expect(mock.checkLong(same(longObject))).andReturn(1);
    }
   
    @Test
    public void testCmpEq() {
        ClassToMock mock = createMock(ClassToMock.class);
        Long longObject = 101L;
        expect(mock.checkLong(cmpEq(longObject))).andReturn(1);
    }

    @Test
    public void testCmp() {
        ClassToMock mock = createMock(ClassToMock.class);
        Long longObject = 101L;
        expect(mock.checkLong(cmp(longObject, null, null))).andReturn(1);
    }


}
