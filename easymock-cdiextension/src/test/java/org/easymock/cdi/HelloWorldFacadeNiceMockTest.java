package org.easymock.cdi;

import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.gov.frameworkdemoiselle.junit.DemoiselleRunner;

@RunWith(DemoiselleRunner.class)
public class HelloWorldFacadeNiceMockTest {

    @Inject
    @TestSubject
    private HelloWorldFacade helloWorldFacade;

    @Inject
    @Mock(type = MockType.NICE)
    private HelloWorldBusiness helloWorldBusinessMock;

    @After
    public void afterTest() {
        reset(helloWorldBusinessMock);
    }

    @Test
    public void testSay() {

        replay(helloWorldBusinessMock);

        final int niceMockIntReturn = 0;
        assertEquals(niceMockIntReturn, helloWorldFacade.sayNumber());

        verify(helloWorldBusinessMock);
    }
}
