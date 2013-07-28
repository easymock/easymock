package org.easymock.cdi.junit;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * Easymock junit runner to unit test CDI beans.
 */
public class EasyMockCdiRunner extends BlockJUnit4ClassRunner {

    /**
     * CDI bootstrapper.
     */
    private final CdiBootstrapper cdiBootstrapper = new DeltaSpikeCdiBootstrapper();

    public EasyMockCdiRunner(final Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    public void run(final RunNotifier notifier) {
        getCdiBootstrapper().initialize();
        super.run(notifier);
        getCdiBootstrapper().shutdown();
    }

    @Override
    protected Object createTest() throws Exception {
        final Class<?> testJavaClass = getTestClass().getJavaClass();
        final Bean<?> testCdiBean = getBean(testJavaClass);
        final CreationalContext<?> testCreationalContext = createContextualContext(testCdiBean);

        return getBeanManager().getReference(testCdiBean, testJavaClass, testCreationalContext);
    }

    private CreationalContext<?> createContextualContext(final Bean<?> testClassBean) {
        final CreationalContext<?> testClassCreationalContext = getBeanManager()
                .createCreationalContext(testClassBean);
        return testClassCreationalContext;
    }

    private Bean<?> getBean(final Class<?> testJavaClass) {
        final BeanManager beanManager = getBeanManager();
        final Set<Bean<?>> beans = beanManager.getBeans(testJavaClass);
        final Bean<?> testClassBean = beans.iterator().next();
        return testClassBean;
    }

    private BeanManager getBeanManager() {
        return getCdiBootstrapper().getBeanManager();
    }

    private CdiBootstrapper getCdiBootstrapper() {
        return cdiBootstrapper;
    }
}
