/*
 * Copyright 2001-2025 the original author or authors.
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

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Henri Tremblay
 */
class SerializationTest implements Serializable {

    private static final long serialVersionUID = -774994679161263654L;

    @Test
    void test() throws Exception {

        List<String> mock = createMock(List.class);

        mock = serialize(mock);

        expect(mock.get(1)).andReturn("a");

        mock = serialize(mock);

        replay(mock);

        mock = serialize(mock);

        Assertions.assertEquals("a", mock.get(1));

        mock = serialize(mock);

        verify(mock);
    }

    @Test
    void testClass() throws Exception {

        ArrayList<String> mock = createMockBuilder(ArrayList.class).addMockedMethod("get").withConstructor()
                .createMock();

        mock = serialize(mock);

        expect(mock.get(1)).andReturn("a");

        mock = serialize(mock);

        replay(mock);

        mock = serialize(mock);

        Assertions.assertEquals("a", mock.get(1));

        mock = serialize(mock);

        verify(mock);
    }

    @Test
    void testAllMockedMethod() throws Exception {

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
    void testChangingClassLoader() {

    }

    @SuppressWarnings("unchecked")
    private <T> T serialize(T o) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bOut);
        out.writeObject(o);
        out.close();

        ByteArrayInputStream bIn = new ByteArrayInputStream(bOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bIn);
        o = (T) in.readObject();
        in.close();

        return o;
    }
}
