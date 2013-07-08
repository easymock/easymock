package org.easymock.cdi.producer;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

import org.easymock.EasyMockSupport;
import org.easymock.cdi.model.EasyMockTestContext;

/**
 * CDI Producer.
 */
public class EasyMockSupportProducer {

    /**
     * Producer method.
     * @return tests context {@link EasyMockSupport}
     */
    @Produces
    @Dependent
    public EasyMockSupport produceEasyMockSupport() {
        return EasyMockTestContext.getInstance().getEasyMockSupport();
    }
}
