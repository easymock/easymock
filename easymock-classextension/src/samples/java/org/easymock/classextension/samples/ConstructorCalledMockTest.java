/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.classextension.samples;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Example of how to partial mock with actually calling a constructor
 */
public class ConstructorCalledMockTest {

    /**
     * Class to test and partially mock
     */
    public static abstract class TaxCalculator {

        private final BigDecimal[] values;

        public TaxCalculator(BigDecimal... values) {
            this.values = values;
        }

        protected abstract BigDecimal rate();

        public BigDecimal tax() {
            BigDecimal result = BigDecimal.ZERO;

            for (BigDecimal d : values) {
                result = result.add(d);
            }

            return result.multiply(rate());
        }
    }

    private TaxCalculator tc;

    @Before
    public void setUp() {
        tc = createMockBuilder(TaxCalculator.class).withConstructor(
                BigDecimal[].class).withArgs(
                (Object) new BigDecimal[] { new BigDecimal("5"),
                        new BigDecimal("15") }) // varargs are special since they are in fact arrays
                .createMock(); // no need to mock any methods, abstract ones are mocked by default
    }

    @After
    public void tearDown() {
        verify(tc);
    }

    @Test
    public void testTax() {

        expect(tc.rate()).andStubReturn(new BigDecimal("0.20"));
        replay(tc);

        assertEquals(new BigDecimal("4.00"), tc.tax());
    }

    @Test
    public void testTax_ZeroRate() {

        expect(tc.rate()).andStubReturn(BigDecimal.ZERO);
        replay(tc);

        assertEquals(BigDecimal.ZERO, tc.tax());
    }
}
