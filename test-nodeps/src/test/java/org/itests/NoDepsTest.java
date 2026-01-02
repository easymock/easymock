/*
 * Copyright 2015-2026 the original author or authors.
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

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintStream;

import static org.easymock.EasyMock.*;

/**
 *
 * @author Henri Tremblay
 */
class NoDepsTest {

    @Test
    void interfaceMocking() throws Exception {
        Appendable appendable = mock(Appendable.class);
        test(appendable);
    }

    @Test
    void classMocking() throws Exception {
        PrintStream stream = mock(PrintStream.class);
        test(stream);
    }

    private void test(Appendable appendable) throws IOException {
        expect(appendable.append('c')).andReturn(appendable);
        replay(appendable);
        appendable.append('c');
        verify(appendable);
    }
}

