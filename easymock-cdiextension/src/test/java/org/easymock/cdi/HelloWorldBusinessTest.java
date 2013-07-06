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
public class HelloWorldBusinessTest {

    @Inject
    @TestSubject
    private HelloWorldBusiness helloWorldBusiness;

    @Inject
    @Mock
    private HelloWorldDao helloWorldDaoMock;

    @After
    public void afterTest() {
        reset(helloWorldDaoMock);
    }

    @Test
    public void testSayHelloWorldDaoDefaultMock() {

        helloWorldDaoMock.doSomething();
        replay(helloWorldDaoMock);

        helloWorldBusiness.say();

        verify(helloWorldDaoMock);
    }
}
