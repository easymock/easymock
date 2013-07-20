package org.easymock.cdi.interceptor;

public class InterceptorFacade {

    public void executedFromInterceptorOne() {
        throwRuntimeError();
    }

    public void executedFromInterceptorTwo() {
        throwRuntimeError();
    }

    private void throwRuntimeError() {
        throw new IllegalStateException("Should't be executed.");
    }
}
