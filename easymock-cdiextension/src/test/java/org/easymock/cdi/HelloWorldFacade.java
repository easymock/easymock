package org.easymock.cdi;

import javax.inject.Inject;

import org.slf4j.Logger;

public class HelloWorldFacade {

    @Inject
    private Logger logger;

    @Inject
    private HelloWorldBusiness helloWorldBusiness;

    public void say() {
        logger.info("Facade logging via CDI.");
        helloWorldBusiness.say();
    }

    public int sayNumber() {
        logger.info("Facade logging via CDI.");
        return helloWorldBusiness.sayNumber();
    }
}
