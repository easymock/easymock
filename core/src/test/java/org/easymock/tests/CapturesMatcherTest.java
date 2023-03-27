/*
 * Copyright 2001-2023 the original author or authors.
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

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.internal.Invocation;
import org.easymock.internal.LastControl;
import org.easymock.internal.matchers.Captures;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Henri Tremblay
 */
public class CapturesMatcherTest {

    private final Capture<String> capture = Capture.newInstance(CaptureType.ALL);

    private final Captures<String> matcher = new Captures<>(capture);

    private StringBuffer buffer;

    @BeforeEach
    public void setUp() throws Exception {
        LastControl.pushCurrentInvocation(new Invocation(this, getClass().getMethod("test"), new Object[0]));
        buffer = new StringBuffer();
    }

    @AfterEach
    public void tearDown() {
        LastControl.popCurrentInvocation();
    }

    @Test
    public void test() {

        matcher.appendTo(buffer);
        assertEquals("capture(Nothing captured yet)", buffer.toString());

        assertTrue(matcher.matches(null));

        matcher.validateCapture();

        clearBuffer();
        matcher.appendTo(buffer);
        assertEquals("capture(null)", buffer.toString());

        assertTrue(matcher.matches("s"));

        matcher.validateCapture();

        clearBuffer();
        matcher.appendTo(buffer);
        assertEquals("capture([null, s])", buffer.toString());
    }

    private void clearBuffer() {
        buffer.delete(0, buffer.length());
    }
}
