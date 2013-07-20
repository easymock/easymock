package org.easymock.cdi.interceptor;

public class InterceptorTwoFacade {

    public void executedFromInterceptorTwo() {
        throwRuntimeError();
    }

    private void throwRuntimeError() {
        throw new IllegalStateException("Should't be executed.");
    }
}
