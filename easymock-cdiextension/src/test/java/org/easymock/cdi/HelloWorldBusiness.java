package org.easymock.cdi;

import javax.inject.Inject;

import org.slf4j.Logger;

public class HelloWorldBusiness {

    private static final int NON_ZERO_NUMBER = 30;

    @Inject
    private Logger logger;

    @Inject
    private HelloWorldDao helloWorldDao;

    public void say() {
        logger.info("Saying hello on console");
        helloWorldDao.doSomething();
    }

    public int sayNumber() {
        return NON_ZERO_NUMBER;
    }
}
