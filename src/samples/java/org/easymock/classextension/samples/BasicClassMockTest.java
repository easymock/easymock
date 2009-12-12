/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.classextension.samples;

import static org.easymock.classextension.EasyMock.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Example of how to use <code>org.easymock.classextension.EasyMock</code>
 */
public class BasicClassMockTest {

    /**
     * Our nice class that is allowed to print
     */
    public static class Document {

        private final Printer printer;

        private String content;

        public Document(Printer printer) {
            this.printer = printer;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
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
        replay(printer);

        document.setContent("Hello world");
        document.print();

        verify(printer); // make sure Printer.print was called
    }

    @Test
    public void testPrintEmptyContent() {
        printer.print("");
        replay(printer);

        document.setContent("");
        document.print();

        verify(printer); // make sure Printer.print was called
    }
}
