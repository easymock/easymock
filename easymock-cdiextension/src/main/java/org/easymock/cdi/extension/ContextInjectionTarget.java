package org.easymock.cdi.extension;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionTarget;

import org.easymock.cdi.model.EasyMockTestContext;
import org.powermock.reflect.Whitebox;

/**
 * Injection target wrapper to test classes.
 *
 * @param <T> test class type
 */
public final class ContextInjectionTarget<T>
        extends AbstractDelegateInjectionTarget<T> {

    /**
     * Mocks to be used.
     */
    private final Set<Object> mocks;

    /**
     * Constructor with params.
     * @param injectionTarget injection target
     * @param mockSet mocks to be used
     */
    public ContextInjectionTarget(
            final InjectionTarget<T> injectionTarget,
            final Set<Object> mockSet) {
        super(injectionTarget);
        this.mocks = mockSet;
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
        for (final Object mock : mocks) {
            Whitebox.setInternalState(instance, mock);
        }
    }
}
