package org.easymock.cdi.junit;

import javax.enterprise.inject.spi.BeanManager;

/**
 * Interface to hide CDI implementations bootstrapping process.
 */
public interface CdiBootstrapper {

    /**
     * Initializes CDI.
     */
    void initialize();

    /**
     * Shuts down CDI.
     */
    void shutdown();

    /**
     * Returns {@link BeanManager}.
     * @return bean manager
     */
    BeanManager getBeanManager();
}
