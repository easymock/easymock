/**
 * Copyright 2001-2013 the original author or authors.
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

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Henri Tremblay
 */
public class SerializationTest implements Serializable {

    private static final long serialVersionUID = -774994679161263654L;

    @SuppressWarnings("unchecked")
    @Test
    public void test() throws Exception {

        List<String> mock = createMock(List.class);

        mock = serialize(mock);

        expect(mock.get(1)).andReturn("a");

        mock = serialize(mock);

        replay(mock);

        mock = serialize(mock);

        assertEquals("a", mock.get(1));

        mock = serialize(mock);

        verify(mock);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testClass() throws Exception {

        ArrayList<String> mock = createMockBuilder(ArrayList.class).addMockedMethod("get").withConstructor()
                .createMock();

        mock = serialize(mock);

        expect(mock.get(1)).andReturn("a");

        mock = serialize(mock);

        replay(mock);

        mock = serialize(mock);

        assertEquals("a", mock.get(1));

        mock = serialize(mock);

        verify(mock);
    }

    @Test
    public void testAllMockedMethod() throws Exception {

        SerializationTest mock = createMock(SerializationTest.class);

        mock = serialize(mock);

        mock.test();

        mock = serialize(mock);

        replay(mock);

        mock = serialize(mock);

        mock.test();

        mock = serialize(mock);

        verify(mock);
    }

    @Test
    @Ignore
    // to code one day to make sure we can recreate a mock in another class loader
    public void testChangingClassLoader() {

    }

    @SuppressWarnings("unchecked")
    private <T> T serialize(T o) throws IOException, ClassNotFoundException {
        final ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        final ObjectOutputStream out = new ObjectOutputStream(bOut);
        out.writeObject(o);
        out.close();

        final ByteArrayInputStream bIn = new ByteArrayInputStream(bOut.toByteArray());
        final ObjectInputStream in = new ObjectInputStream(bIn);
        o = (T) in.readObject();
        in.close();

        return o;
    }
}
