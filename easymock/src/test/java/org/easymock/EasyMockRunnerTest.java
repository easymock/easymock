package org.easymock;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EasyMockRunner.class)
public class EasyMockRunnerTest extends EasyMockSupport {

    @Mock
    private List<?> mock;

    @Test
    public void testApply() {
        expect(mock.isEmpty()).andReturn(true);
        replayAll();
        assertEquals(true, mock.isEmpty());
        verifyAll();
    }

}
