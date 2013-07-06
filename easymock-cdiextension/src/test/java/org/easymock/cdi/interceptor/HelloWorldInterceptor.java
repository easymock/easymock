package org.easymock.cdi.interceptor;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@HelloWorldInterceptorBinding
public class HelloWorldInterceptor {

    @Inject
    private HelloWorldInterceptorFacade helloWorldInterceptorFacade;

    public HelloWorldInterceptor() {
        System.out.println("New interceptor instance.");
    }

    @AroundInvoke
    public Object aroundInvoke(final InvocationContext ctx) throws Exception {
        helloWorldInterceptorFacade.say();
        return ctx.proceed();
    }
}
