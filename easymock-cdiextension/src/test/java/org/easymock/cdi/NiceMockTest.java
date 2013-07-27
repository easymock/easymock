package org.easymock.cdi;

import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
import org.easymock.cdi.junit.EasyMockCdiRunner;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EasyMockCdiRunner.class)
public class NiceMockTest {

    @Inject
    @TestSubject
    private HelloWorldFacade helloWorldFacade;

    @Inject
    @Mock(type = MockType.NICE)
    private HelloWorldBusiness helloWorldBusinessNiceMock;

    @After
    public void afterTest() {
        reset(helloWorldBusinessNiceMock);
    }

    @Test
    public void testNiceMock() {

        replay(helloWorldBusinessNiceMock);

        final int niceMockIntReturn = 0;
        assertEquals(niceMockIntReturn, helloWorldFacade.sayNumber());

        verify(helloWorldBusinessNiceMock);
    }
}
