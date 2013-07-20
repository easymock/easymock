package org.easymock.cdi.extension;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionTarget;

import org.easymock.cdi.model.EasyMockTestContext;
import org.easymock.cdi.model.ReflectionHelper;

/**
 * Injection target wrapper to test classes.
 *
 * @param <T> test class type
 */
public final class ContextInjectionTarget<T>
        extends AbstractDelegateInjectionTarget<T> {

    /**
     * Constructor with params.
     * @param injectionTarget injection target
     */
    public ContextInjectionTarget(
            final InjectionTarget<T> injectionTarget) {
        super(injectionTarget);
    }

    /**
     * Produce the bean instance and sets current test context.
     *
     * @param ctx creational context
     * @return bean instance
     */
    @Override
    public T produce(final CreationalContext<T> ctx) {
        final T bean = getDelegate().produce(ctx);
        final EasyMockTestContext easyMockTestContext =
                EasyMockTestContext.getInstance();
        easyMockTestContext.setCurrentExecutionContext(bean.getClass());
        return bean;
    }

    /**
     * Injects CDI beans and mocks.
     * @param instance instance
     * @param ctx creational context
     */
    @Override
    public void inject(final T instance, final CreationalContext<T> ctx) {
        getDelegate().inject(instance, ctx);
        final EasyMockTestContext easyMockTestContext =
                EasyMockTestContext.getInstance();
        final Set<Object> mocks = easyMockTestContext.getMocks(instance.getClass());
        ReflectionHelper.setInternalState(instance, mocks);
    }
}
