/*
 * Copyright 2001-2025 the original author or authors.
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

import org.easymock.internal.AssertionErrorWrapper;
import org.easymock.internal.MocksBehavior;
import org.easymock.tests.IMethods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static org.easymock.EasyMock.*;

/**
 * Test that EasyMock works in replay state in a multithreaded environment. Note
 * that sadly this test isn't sure to fail all the time. Only if there's a
 * concurrency issue, and we're lucky enough to fell on it during testing.
 *
 * @author Henri Tremblay
 */
class ThreadingTest {

    private static final int THREAD_COUNT = 10;

    @Test
    void testThreadSafe() throws Throwable {

        final IMethods mock = createMock(IMethods.class);
        expect(mock.oneArg("test")).andReturn("result").times(THREAD_COUNT);

        replay(mock);

        Callable<String> replay = () -> mock.oneArg("test");

        ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);

        List<Callable<String>> tasks = Collections.nCopies(THREAD_COUNT, replay);

        List<Future<String>> results = service.invokeAll(tasks);

        for (Future<String> future : results) {
            Assertions.assertEquals("result", future.get());
        }

        verify(mock);
    }

    @Test
    void testThreadNotSafe() throws Throwable {

        final IMethods mock = createMock(IMethods.class);
        expect(mock.oneArg("test")).andReturn("result").times(THREAD_COUNT);

        makeThreadSafe(mock, false);

        checkIsUsedInOneThread(mock, true);

        replay(mock);

        Callable<String> replay = () -> mock.oneArg("test");

        ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);

        List<Callable<String>> tasks = Collections.nCopies(THREAD_COUNT, replay);

        List<Future<String>> results = service.invokeAll(tasks);

        boolean exceptionThrown = false;

        for (Future<String> future : results) {
            try {
                Assertions.assertEquals("result", future.get());
            } catch (ExecutionException e) {
                // Since I don't know which one the lastThread is, that's the
                // best assert I can do except doing
                // a regular exception and I don't think it worth it
                Assertions.assertTrue(e.getCause().getMessage().startsWith(
                        "\n Mock isn't supposed to be called from multiple threads. Last: "));
                exceptionThrown = true;
            }
        }

        Assertions.assertTrue(exceptionThrown);
    }

    @Test
    void testMockUsedCorrectly() {
        IMethods mock = createMock(IMethods.class);
        expect(mock.oneArg("test")).andReturn("result").times(2);

        checkIsUsedInOneThread(mock, true);

        replay(mock);

        mock.oneArg("test");
        mock.oneArg("test");

        verify(mock);
    }

    @Test
    void testChangeDefault() throws Throwable {
        String previousThreadSafetyCheck = setEasyMockProperty(ENABLE_THREAD_SAFETY_CHECK_BY_DEFAULT,
                Boolean.TRUE.toString());
        String previousThreadSafe = setEasyMockProperty(NOT_THREAD_SAFE_BY_DEFAULT, Boolean.TRUE
                .toString());
        try {
            final MocksBehavior behavior = new MocksBehavior(true);
            Assertions.assertFalse(behavior.isThreadSafe());

            Thread t = new Thread(behavior::checkThreadSafety);
            t.start();
            t.join();
            try {
                behavior.checkThreadSafety();
                Assertions.fail("Shouldn't work");
            } catch (AssertionErrorWrapper e) {

            }

        } finally {
            setEasyMockProperty(ENABLE_THREAD_SAFETY_CHECK_BY_DEFAULT, previousThreadSafetyCheck);
            setEasyMockProperty(NOT_THREAD_SAFE_BY_DEFAULT, previousThreadSafe);
        }
    }

    @Test
    void testRecordingInMultipleThreads() throws Exception {

        Callable<String> replay = () -> {
            IMethods mock = createMock(IMethods.class);
            expect(mock.oneArg("test")).andReturn("result");

            replay(mock);

            String s = mock.oneArg("test");

            verify(mock);

            return s;
        };

        ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);

        List<Callable<String>> tasks = Collections.nCopies(THREAD_COUNT, replay);

        List<Future<String>> results = service.invokeAll(tasks);

        for (Future<String> future : results) {
            Assertions.assertEquals("result", future.get());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    void testCleanupAfterFailureInRecordPhase() {
        Comparable<String> mock = createNiceMock(Comparable.class);

        // Mocking equals() doesn't work
        try {
            expect(mock.equals(eq(mock))).andReturn(true);
        } catch (IllegalStateException e) {

        }

        // However, the recorded matchers should be cleaned to prevent impacting
        // other tests
        mock = createNiceMock(Comparable.class);
        expect(mock.compareTo(isNull())).andReturn(1);
        replay(mock);
        Assertions.assertEquals(1, mock.compareTo(null));
    }
}
