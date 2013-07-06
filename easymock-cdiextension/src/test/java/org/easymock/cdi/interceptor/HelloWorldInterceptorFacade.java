package org.easymock.cdi.interceptor;

public class HelloWorldInterceptorFacade {

    public void say() {
        throw new IllegalStateException("Should't be executed.");
    }
}
