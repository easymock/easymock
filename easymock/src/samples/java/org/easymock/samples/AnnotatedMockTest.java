package org.easymock.samples;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.InjectMocks;
import org.easymock.Mock;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Example of how to use <code>@Mock</code> and <code>@InjectMocks</code> annotations
 * 
 * @author Henri Tremblay
 */
@RunWith(EasyMockRunner.class)
public class AnnotatedMockTest extends EasyMockSupport {

    @InjectMocks
    private final ClassTested classUnderTest = new ClassTested();

    @Mock
    private Collaborator collaborator;

    @Test
    public void addDocument() {
        classUnderTest.setListener(collaborator);
        collaborator.documentAdded("New Document");
        replayAll();
        classUnderTest.addDocument("New Document", new byte[0]);
        verifyAll();
    }
}
