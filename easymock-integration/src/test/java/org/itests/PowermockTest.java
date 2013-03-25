/**
 * Copyright 2009-2013 the original author or authors.
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
package org.itests;

import static org.junit.Assert.*;
import static org.powermock.api.easymock.PowerMock.*;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Test doing a basic smoke test to make sure EasyMock is still compatible with
 * PowerMock. A complete means to run the PowerMock test suite using the latest
 * EasyMock version.
 * <p>
 * They are in this package because PowerMock doesn't work on classes in the
 * org.easymock package.
 * 
 * @author Henri Tremblay
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest( { StaticService.class })
public class PowermockTest {

    @Test
    public void testMockStatic() throws Exception {
        mockStatic(StaticService.class);
        final String expected = "Hello altered World";
        EasyMock.expect(StaticService.say("hello")).andReturn("Hello altered World");
        replay(StaticService.class);

        final String actual = StaticService.say("hello");

        verify(StaticService.class);
        assertEquals("Expected and actual did not match", expected, actual);

        // Singleton still be mocked by now.
        try {
            StaticService.say("world");
            fail("Should throw AssertionError!");
        } catch (final AssertionError e) {
            assertEquals("\n  Unexpected method call StaticService.say(\"world\"):", e.getMessage());
        }
    }

}
