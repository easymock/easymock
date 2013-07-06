package org.easymock.cdi.interceptor;

import javax.inject.Inject;

import org.slf4j.Logger;

@HelloWorldInterceptorBinding
public class HelloWorldInterceptedBean {

    @Inject
    private Logger logger;

    public void doSomething() {
        logger.info("Executing " + this.getClass().getSimpleName());
    }
}
