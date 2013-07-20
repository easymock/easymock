package org.easymock.cdi.interceptor;

public class InterceptorOneFacade {

    public void executedFromInterceptorOne() {
        throwRuntimeError();
    }

    private void throwRuntimeError() {
        throw new IllegalStateException("Should't be executed.");
    }
}
