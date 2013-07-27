package org.easymock.cdi.producer;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.impl.SimpleLoggerFactory;

public class LoggerProducer {

    @Produces
    @Dependent
    public Logger produceLogger(final InjectionPoint injectionPoint) {
        return new SimpleLoggerFactory().getLogger(injectionPoint.getBean().getBeanClass().getName());
    }
}
