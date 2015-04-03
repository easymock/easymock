package org.easymock.tests;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.IMocksControl;
import org.junit.Test;

public class UnexpectedCallVerifyTest {
    @Test
    public void testUnexpectedMethodCallVerification() {
        final IMocksControl mockControl = createStrictControl();

        final IMethods mock = mockControl
                .createMock(IMethods.class);

        replay(mock);
        try {
            mock.simpleMethod();
            fail();
        } catch (final AssertionError err) {

        }
        try {
            verify(mock);
            fail();
        } catch (final AssertionError err) {

        }

    }

    @Test
    public void testUnexpectedMethodCallVerificationWithResest() {
        final IMocksControl mockControl = createStrictControl();

        final IMethods mock = mockControl
                .createMock(IMethods.class);

        replay(mock);
        try {
            mock.simpleMethod();
            fail();
        } catch (final AssertionError err) {

        }

        resetUnexpectedCallVerification(mock);
        verify(mock);

    }

}