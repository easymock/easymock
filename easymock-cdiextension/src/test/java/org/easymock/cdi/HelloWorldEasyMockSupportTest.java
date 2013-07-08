package org.easymock.cdi;

import javax.inject.Inject;

import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.gov.frameworkdemoiselle.junit.DemoiselleRunner;

@RunWith(DemoiselleRunner.class)
public class HelloWorldEasyMockSupportTest {

    @Inject
    @TestSubject
    private HelloWorldBusiness helloWorldBusiness;

    @Inject
    @Mock
    private HelloWorldDao helloWorldDaoMock;

    @Inject
    private EasyMockSupport easyMockSupport;

    @After
    public void afterTest() {
        easyMockSupport.resetAll();
    }

    @Test
    public void testEasyMockSupportExample() {

        helloWorldDaoMock.doSomething();
        easyMockSupport.replayAll();

        helloWorldBusiness.say();

        easyMockSupport.verifyAll();
    }

}
