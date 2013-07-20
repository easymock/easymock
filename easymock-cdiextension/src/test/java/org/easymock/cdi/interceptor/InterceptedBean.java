package org.easymock.cdi.interceptor;

import javax.inject.Inject;

import org.slf4j.Logger;

public class InterceptedBean {

    @Inject
    private Logger logger;

    @InterceptorOneBinding
    public void doSomething() {
        logger.info("Executing " + this.getClass().getSimpleName());
    }

    @InterceptorTwoBinding
    public void doSomethingTwo() {
        logger.info("Executing " + this.getClass().getSimpleName());
    }
}
