package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.internal.Result;
import org.junit.Test;


public class ResultTest {

    @Test
    public void createThrowResultToString() {
        Exception e = new Exception("Error message");
        Result r = Result.createThrowResult(e);
        assertEquals("Answer throwing " + e, r.toString());
    }
    
    @Test
    public void createReturnResultToString() {
        String value = "My value";
        Result r = Result.createReturnResult(value);
        assertEquals("Answer returning " + value, r.toString());
    }
    
    @Test
    public void createDelegateResultToString() {
        String value = "my value";
        Result r = Result.createDelegatingResult(value);
        assertEquals("Delegated to " + value, r.toString());
    }
}
