package org.easymock.cdi.interceptor;

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
public class InterceptorTest {

    @SuppressWarnings("unused")
    @TestSubject
    private InterceptorOne interceptorOne;

    @SuppressWarnings("unused")
    @TestSubject
    private InterceptorTwo interceptorTwo;

    @Inject
    @Mock(type = MockType.STRICT)
    private InterceptorFacade helloWorldInterceptorFacadeMock;

    @Inject
    private InterceptedBean helloWorldInterceptedBean;

    @After
    public void afterTest() {
        reset(helloWorldInterceptorFacadeMock);
    }

    @Test
    public void testInterceptorOne() {

        helloWorldInterceptorFacadeMock.executedFromInterceptorOne();
        replay(helloWorldInterceptorFacadeMock);

        helloWorldInterceptedBean.doSomething();

        verify(helloWorldInterceptorFacadeMock);
    }

    @Test
    public void testInterceptorTwo() {

        helloWorldInterceptorFacadeMock.executedFromInterceptorTwo();
        replay(helloWorldInterceptorFacadeMock);

        helloWorldInterceptedBean.doSomethingTwo();

        verify(helloWorldInterceptorFacadeMock);
    }
}
