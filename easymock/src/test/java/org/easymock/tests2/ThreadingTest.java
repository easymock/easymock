/*
 * Copyright (c) 2001-2009 OFFIS, Henri Tremblay.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests2;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import org.easymock.tests.IMethods;
import org.junit.Test;

/**
 * Test that EasyMock works in replay state in a multithreaded
 * environment. Note that sadly this test isn't sure to fail all
 * the time. Only if there's a concurrency issue and we're lucky
 * enough to fell on it during testing.
 */
public class ThreadingTest {

    private static final int THREAD_COUNT = 10;

    @Test
    public void testThreadSafe() throws Throwable {

        final IMethods mock = createMock(IMethods.class);
        expect(mock.oneArg("test")).andReturn("result").times(THREAD_COUNT);
        
        makeThreadSafe(mock, true);
        
        replay(mock);
        
        Callable<String> replay = new Callable<String>() {
            public String call() throws Exception {
                return mock.oneArg("test");
            }
        };
        
        ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);

        List<Callable<String>> tasks = Collections.nCopies(THREAD_COUNT, replay);

        List<Future<String>> results = service.invokeAll(tasks);

        for (Future<String> future : results) {
            assertEquals("result", future.get());
        }

        verify(mock);
    }
    
    @Test
    public void testThreadNotSafe() throws Throwable {

        final IMethods mock = createMock(IMethods.class);
        expect(mock.oneArg("test")).andReturn("result").times(THREAD_COUNT);
        
        replay(mock);
        
        Callable<String> replay = new Callable<String>() {
            public String call() throws Exception {
                return mock.oneArg("test");
            }
        };
        
        ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);

        List<Callable<String>> tasks = Collections.nCopies(THREAD_COUNT, replay);

        List<Future<String>> results = service.invokeAll(tasks);
        
        boolean exceptionThrown = false;

        for (Future<String> future : results) {
            try {
                assertEquals("result", future.get());
            }
            catch(ExecutionException e) {
                // Since I don't know which one the lastThread is, that's the
                // best assert I can do except doing
                // a regular exception and I don't think it worth it
                assertTrue(e.getCause().getMessage().startsWith(                        
                        "\n Un-thread-safe mock called from multiple threads. Last: "));
                exceptionThrown = true;
            }
        }
        
        assertTrue(exceptionThrown);
    }    
    
    @Test
    public void testDisablingThreadSafetyCheck() throws Throwable {
        String previous = setEasyMockProperty(DISABLE_THREAD_SAFETY_CHECK,
                Boolean.TRUE.toString());
        try {
            // Call the thread-safe test. Shouldn't fail
            testThreadNotSafe();            
        } finally {
            setEasyMockProperty(DISABLE_THREAD_SAFETY_CHECK, previous);
        }
    }
}

