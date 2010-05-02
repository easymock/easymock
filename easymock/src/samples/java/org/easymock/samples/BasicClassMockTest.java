/**
 * Copyright 2001-2010 the original author or authors.
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

package org.easymock.samples;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Example of how to use <code>org.easymock.classextension.EasyMock</code>
 * 
 * @author Henri Tremblay
 */
public class BasicClassMockTest extends EasyMockSupport {

    /**
     * Our nice class that is allowed to print
     */
    public static class Document {

        private final Printer printer;

        private String content;

        public Document(final Printer printer) {
            this.printer = printer;
        }

        public String getContent() {
            return content;
        }

        public void setContent(final String content) {
            this.content = content;
        }

        public void print() {
            printer.print(content);
        }
    }

    /**
     * The terrible 3rd party class that is not an interface but that we really
     * want to mock.
     */
    public static abstract class Printer {
        public abstract void print(String toPrint);
    }

    private Printer printer;

    private Document document;

    @Before
    public void setUp() {
        printer = createMock(Printer.class);
        document = new Document(printer);
    }

    @After
    public void tearDown() {
        printer = null;
        document = null;
    }

    @Test
    public void testPrintContent() {
        printer.print("Hello world");
        replayAll();

        document.setContent("Hello world");
        document.print();

        verifyAll(); // make sure Printer.print was called
    }

    @Test
    public void testPrintEmptyContent() {
        printer.print("");
        replayAll();

        document.setContent("");
        document.print();

        verifyAll(); // make sure Printer.print was called
    }
}
