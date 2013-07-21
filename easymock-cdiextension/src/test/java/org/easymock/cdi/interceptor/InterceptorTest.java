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
    private InterceptorOneFacade interceptorOneFacadeMock;

    @Inject
    @Mock(type = MockType.STRICT)
    private InterceptorTwoFacade interceptorTwoFacadeMock;

    @Inject
    private InterceptedBean interceptedBean;

    @After
    public void afterTest() {
        reset(interceptorOneFacadeMock, interceptorTwoFacadeMock);
    }

    @Test
    public void testInterceptorOne() {

        interceptorOneFacadeMock.executedFromInterceptorOne();
        replay(interceptorOneFacadeMock);

        interceptedBean.doSomething();

        verify(interceptorOneFacadeMock);
    }

    @Test
    public void testInterceptorTwo() {

        interceptorTwoFacadeMock.executedFromInterceptorTwo();
        replay(interceptorTwoFacadeMock);

        interceptedBean.doSomethingTwo();

        verify(interceptorTwoFacadeMock);
    }
}
