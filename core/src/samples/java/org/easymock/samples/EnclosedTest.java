/*
 * Copyright 2001-2019 the original author or authors.
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

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test demonstrating the use of JUnit 4.12 new enclosed feature
 *
 * @author Henri Tremblay
 */
@RunWith(Enclosed.class)
public class EnclosedTest {

    public static abstract class Fixtures extends EasyMockSupport {

        @TestSubject
        ClassTested classUnderTest = new ClassTested();

        @Mock
        Collaborator mock;
    }

    @RunWith(EasyMockRunner.class)
    public static class VoteRemoval extends Fixtures {

        @Before
        public void when() {
            // expect document addition
            mock.documentAdded("Document");
        }

        @Test
        public void voteForRemoval() {
            // expect to be asked to vote, and vote for it
            expect(mock.voteForRemoval("Document")).andReturn((byte) 42);
            // expect document removal
            mock.documentRemoved("Document");

            replayAll();
            classUnderTest.addDocument("Document", "content");
            assertTrue(classUnderTest.removeDocument("Document"));
            verifyAll();
        }

        @Test
        public void voteAgainstRemoval() {
            // expect to be asked to vote, and vote against it
            expect(mock.voteForRemoval("Document")).andReturn((byte) -42); //
            // document removal is *not* expected

            replayAll();
            classUnderTest.addDocument("Document", "content");
            assertFalse(classUnderTest.removeDocument("Document"));
            verifyAll();
        }
    }

    @RunWith(EasyMockRunner.class)
    public static class VoteRemovals extends Fixtures {

        @Before
        public void when() {
            mock.documentAdded("Document 1");
            mock.documentAdded("Document 2");
        }

        @Test
        public void voteForRemovals() {
            expect(mock.voteForRemovals("Document 1", "Document 2")).andReturn((byte) 42);
            mock.documentRemoved("Document 1");
            mock.documentRemoved("Document 2");
            replayAll();
            classUnderTest.addDocument("Document 1", "content 1");
            classUnderTest.addDocument("Document 2", "content 2");
            assertTrue(classUnderTest.removeDocuments("Document 1", "Document 2"));
            verifyAll();
        }

        @Test
        public void voteAgainstRemovals() {
            expect(mock.voteForRemovals("Document 1", "Document 2")).andReturn((byte) -42);
            replayAll();
            classUnderTest.addDocument("Document 1", "content 1");
            classUnderTest.addDocument("Document 2", "content 2");
            assertFalse(classUnderTest.removeDocuments("Document 1", "Document 2"));
            verifyAll();
        }
    }
}
