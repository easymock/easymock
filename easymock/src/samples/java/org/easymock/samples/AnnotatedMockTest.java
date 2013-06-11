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
package org.easymock.samples;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.TestSubject;
import org.easymock.Mock;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Example of how to use <code>@Mock</code> and <code>@TestSubject</code> annotations
 * 
 * @author Henri Tremblay
 */
@RunWith(EasyMockRunner.class)
public class AnnotatedMockTest extends EasyMockSupport {

    @TestSubject
    private final ClassTested classUnderTest = new ClassTested();

    @Mock
    private Collaborator collaborator;

    @Test
    public void addDocument() {
        collaborator.documentAdded("New Document");
        replayAll();
        classUnderTest.addDocument("New Document", new byte[0]);
        verifyAll();
    }
}
