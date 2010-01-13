/*
 * Copyright 2009 Henri Tremblay
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
package org.easymock.itests;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

/**
 * Test to make sure the latest class extension is working with the latest
 * EasyMock
 * 
 * @author Henri Tremblay
 */
public class ClassExtensionTest {

    @Test
    public void test() throws IOException {
        StringReader in = createMock(StringReader.class);
        expect(in.read()).andReturn(3);
        replay(in);
        assertSame(3, in.read());
        verify(in);
    }
}
