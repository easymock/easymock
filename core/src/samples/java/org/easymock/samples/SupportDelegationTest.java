/**
 * Copyright 2001-2017 the original author or authors.
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
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Henri Tremblay
 */
public class SupportDelegationTest {

    private EasyMockSupport support = new EasyMockSupport();

    private Collaborator collaborator;

    private ClassTested classUnderTest;

    @Before
    public void setup() {
        classUnderTest = new ClassTested();
    }

    @Test
    public void addDocument() {
        collaborator = support.mock(Collaborator.class);
        classUnderTest.setListener(collaborator);
        collaborator.documentAdded("New Document");
        support.replayAll();
        classUnderTest.addDocument("New Document", "content");
        support.verifyAll();
    }

    @Test
    public void voteForRemovals() {

        IMocksControl ctrl = support.createControl();
        collaborator = ctrl.createMock(Collaborator.class);
        classUnderTest.setListener(collaborator);

        collaborator.documentAdded("Document 1");

        expect(collaborator.voteForRemovals("Document 1")).andReturn((byte) 20);

        collaborator.documentRemoved("Document 1");

        support.replayAll();

        classUnderTest.addDocument("Document 1", "content");
        assertTrue(classUnderTest.removeDocuments("Document 1"));

        support.verifyAll();
    }
}
