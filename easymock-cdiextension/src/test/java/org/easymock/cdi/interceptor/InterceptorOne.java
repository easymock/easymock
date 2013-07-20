package org.easymock.cdi.interceptor;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;

@Interceptor
@InterceptorOneBinding
public class InterceptorOne {

    @Inject
    private Logger logger;

    @Inject
    private InterceptorOneFacade interceptorOneFacade;

    @AroundInvoke
    public Object aroundInvoke(final InvocationContext ctx) throws Exception {
        logger.info(getClass().getName() + " interceptor running.");
        interceptorOneFacade.executedFromInterceptorOne();
        return ctx.proceed();
    }
}
