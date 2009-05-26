/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.itests;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

/**
 * Test to make sure the latest class extension is working with the latest
 * EasyMock
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
