package org.easymock.cdi.junit;

import javax.enterprise.inject.spi.BeanManager;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;

/**
 * CDI bootstrapper. Uses deltaspike to select cdi container based on runtime
 * dependencies.
 */
public class DeltaSpikeCdiBootstrapper implements CdiBootstrapper {

    private CdiContainer cdiContainer;

    public void initialize() {
        getCdiContainer().boot();
    }

    public void shutdown() {
        getCdiContainer().shutdown();
    }

    public BeanManager getBeanManager() {
        return getCdiContainer().getBeanManager();
    }

    private synchronized CdiContainer getCdiContainer() {
        if (cdiContainer == null) {
            cdiContainer = CdiContainerLoader.getCdiContainer();
        }
        return cdiContainer;
    }
}
