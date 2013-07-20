package org.easymock.cdi;

import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

import javax.inject.Inject;

import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.gov.frameworkdemoiselle.junit.DemoiselleRunner;

@RunWith(DemoiselleRunner.class)
public class DefaultMockTest {

    @Inject
    @TestSubject
    private HelloWorldBusiness helloWorldBusiness;

    @Inject
    @Mock
    private HelloWorldDao helloWorldDaoDefaultMock;

    @After
    public void afterTest() {
        reset(helloWorldDaoDefaultMock);
    }

    @Test
    public void testDefaultMock() {

        helloWorldDaoDefaultMock.doSomething();
        replay(helloWorldDaoDefaultMock);

        helloWorldBusiness.say();

        verify(helloWorldDaoDefaultMock);
    }
}
