package org.easymock.cdi.junit;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * Weld CDI bootstrapper.
 */
public class WeldBootstrapper implements CdiBootstrapper {
    /**
     * Weld.
     */
    private final Weld weld;

    /**
     * Weld container.
     */
    private WeldContainer weldContainer;

    public WeldBootstrapper() {
        weld = new Weld();
    }

    public void initialize() {
        weldContainer = weld.initialize();
    }

    public void shutdown() {
        weld.shutdown();
    }

    public BeanManager getBeanManager() {
        if (weldContainer == null) {
            initialize();
        }
        return weldContainer.getBeanManager();
    }

}
