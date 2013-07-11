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
package org.easymock.test;

import java.io.IOException;

import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * Test that everything is working fine after a deployment to Sonatype
 * 
 * @author Henri Tremblay
 */
public class EasyMockTest {

    @Test
    public void test() throws IOException {
        Appendable mock = createMock(Appendable.class);
        expect(mock.append("test")).andReturn(mock);
        replay(mock);
        assertSame(mock, mock.append("test"));
        verify(mock);
    }
}
