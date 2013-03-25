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
package org.easymock.tests2;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import org.easymock.internal.AssertionErrorWrapper;
import org.easymock.internal.MocksBehavior;
import org.easymock.tests.IMethods;
import org.junit.Test;

/**
 * Test that EasyMock works in replay state in a multithreaded environment. Note
 * that sadly this test isn't sure to fail all the time. Only if there's a
 * concurrency issue and we're lucky enough to fell on it during testing.
 * 
 * @author Henri Tremblay
 */
public class ThreadingTest {

    private static final int THREAD_COUNT = 10;

    @Test
    public void testThreadSafe() throws Throwable {

        final IMethods mock = createMock(IMethods.class);
        expect(mock.oneArg("test")).andReturn("result").times(THREAD_COUNT);

        replay(mock);

        final Callable<String> replay = new Callable<String>() {
            public String call() throws Exception {
                return mock.oneArg("test");
            }
        };

        final ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);

        final List<Callable<String>> tasks = Collections.nCopies(THREAD_COUNT, replay);

        final List<Future<String>> results = service.invokeAll(tasks);

        for (final Future<String> future : results) {
            assertEquals("result", future.get());
        }

        verify(mock);
    }

    @Test
    public void testThreadNotSafe() throws Throwable {

        final IMethods mock = createMock(IMethods.class);
        expect(mock.oneArg("test")).andReturn("result").times(THREAD_COUNT);

        makeThreadSafe(mock, false);

        checkIsUsedInOneThread(mock, true);

        replay(mock);

        final Callable<String> replay = new Callable<String>() {
            public String call() throws Exception {
                return mock.oneArg("test");
            }
        };

        final ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);

        final List<Callable<String>> tasks = Collections.nCopies(THREAD_COUNT, replay);

        final List<Future<String>> results = service.invokeAll(tasks);

        boolean exceptionThrown = false;

        for (final Future<String> future : results) {
            try {
                assertEquals("result", future.get());
            } catch (final ExecutionException e) {
                // Since I don't know which one the lastThread is, that's the
                // best assert I can do except doing
                // a regular exception and I don't think it worth it
                assertTrue(e.getCause().getMessage().startsWith(
                        "\n Mock isn't supposed to be called from multiple threads. Last: "));
                exceptionThrown = true;
            }
        }

        assertTrue(exceptionThrown);
    }

    @Test
    public void testMockUsedCorrectly() {
        final IMethods mock = createMock(IMethods.class);
        expect(mock.oneArg("test")).andReturn("result").times(2);

        checkIsUsedInOneThread(mock, true);

        replay(mock);

        mock.oneArg("test");
        mock.oneArg("test");

        verify(mock);
    }

    @Test
    public void testChangeDefault() throws Throwable {
        final String previousThreadSafetyCheck = setEasyMockProperty(ENABLE_THREAD_SAFETY_CHECK_BY_DEFAULT,
                Boolean.TRUE.toString());
        final String previousThreadSafe = setEasyMockProperty(NOT_THREAD_SAFE_BY_DEFAULT, Boolean.TRUE
                .toString());
        try {
            final MocksBehavior behavior = new MocksBehavior(true);
            assertFalse(behavior.isThreadSafe());

            final Thread t = new Thread() {
                @Override
                public void run() {
                    behavior.checkThreadSafety();
                }
            };
            t.start();
            t.join();
            try {
                behavior.checkThreadSafety();
                fail("Shouldn't work");
            } catch (final AssertionErrorWrapper e) {

            }

        } finally {
            setEasyMockProperty(ENABLE_THREAD_SAFETY_CHECK_BY_DEFAULT, previousThreadSafetyCheck);
            setEasyMockProperty(NOT_THREAD_SAFE_BY_DEFAULT, previousThreadSafe);
        }
    }

    @Test
    public void testRecordingInMultipleThreads() throws InterruptedException, ExecutionException {

        final Callable<String> replay = new Callable<String>() {
            public String call() throws Exception {
                final IMethods mock = createMock(IMethods.class);
                expect(mock.oneArg("test")).andReturn("result");

                replay(mock);

                final String s = mock.oneArg("test");

                verify(mock);

                return s;
            }
        };

        final ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);

        final List<Callable<String>> tasks = Collections.nCopies(THREAD_COUNT, replay);

        final List<Future<String>> results = service.invokeAll(tasks);

        for (final Future<String> future : results) {
            assertEquals("result", future.get());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCleanupAfterFailureInRecordPhase() {
        Comparable<String> mock = createNiceMock(Comparable.class);

        // Mocking equals() doesn't work
        try {
            expect(mock.equals(eq(mock))).andReturn(true);
        } catch (final IllegalStateException e) {

        }

        // However, the recorded matchers should be cleaned to prevent impacting
        // other tests
        mock = createNiceMock(Comparable.class);
        expect(mock.compareTo((String) isNull())).andReturn(1);
        replay(mock);
        assertEquals(1, mock.compareTo(null));
    }
}
