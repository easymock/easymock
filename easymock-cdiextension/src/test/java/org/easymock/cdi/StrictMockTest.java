package org.easymock.cdi;

import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

import javax.inject.Inject;

import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.gov.frameworkdemoiselle.junit.DemoiselleRunner;

@RunWith(DemoiselleRunner.class)
public class StrictMockTest {

    @Inject
    @TestSubject
    private HelloWorldFacade helloWorldFacade;

    @Inject
    @Mock(type = MockType.STRICT)
    private HelloWorldBusiness helloWorldBusinessStrictMock;

    @After
    public void afterTest() {
        reset(helloWorldBusinessStrictMock);
    }

    @Test
    public void testSay() {

        helloWorldBusinessStrictMock.say();
        replay(helloWorldBusinessStrictMock);

        helloWorldFacade.say();

        verify(helloWorldBusinessStrictMock);
    }
}
