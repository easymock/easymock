/*
 * Copyright 2009-2022 the original author or authors.
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

import org.easymock.EasyMockSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URL;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.powermock.api.easymock.PowerMock.*;

/**
 * Test doing a basic smoke test to make sure EasyMock is still compatible with
 * PowerMock. A more complete test is to run the entire PowerMock test suite using the latest
 * EasyMock version.
 * <p>
 * This test class is in the <code>org.itests</code> package instead of in a classical <code>org.easymock</code>
 * package because Powermock ignores classes from the <code>org.easymock</code> to be able to work as
 * expected (mocking itself is never a good idea)
 *
 * @author Henri Tremblay
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest( { StaticService.class })
public class PowermockTest {

    @Test
    public void testMockStatic() {
        mockStatic(StaticService.class);

        String expected = "Hello altered World";
        expect(StaticService.say("hello")).andReturn(expected);

        replay(StaticService.class);

        String actual = StaticService.say("hello");

        verify(StaticService.class);

        assertEquals(expected, actual);

        // Singleton still be mocked by now.
        try {
            StaticService.say("world");
            fail("Should throw AssertionError!");
        } catch (AssertionError e) {
            assertEquals("\n  Unexpected method call StaticService.say(\"world\"):", e.getMessage());
        }
    }

    @Test
    public void mockType() {
        Object mock = PowerMock.createMock(getClass());
        assertEquals(getClass(), EasyMockSupport.getMockedClass(mock));
    }

}

