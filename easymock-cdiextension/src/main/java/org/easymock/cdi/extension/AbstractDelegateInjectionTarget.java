package org.easymock.cdi.extension;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;

/**
 * Abstract class to {@link InjectionTarget} wrappers.
 *
 * @param <T> type
 */
public abstract class AbstractDelegateInjectionTarget<T>
    implements InjectionTarget<T> {

    /**
     * Delegate.
     */
    private final InjectionTarget<T> delegate;

    /**
     * Constructor with params.
     * @param injectionTarget delegate
     */
    public AbstractDelegateInjectionTarget(
        final InjectionTarget<T> injectionTarget) {
        this.delegate = injectionTarget;
    }

    /**
     * Getter.
     * @return delegate
     */
    protected final InjectionTarget<T> getDelegate() {
        return delegate;
    }

    /** {@inheritDoc} */
    public T produce(final CreationalContext<T> ctx) {
        return delegate.produce(ctx);
    }

    /** {@inheritDoc} */
    public void dispose(final T instance) {
        delegate.dispose(instance);
    }

    /** {@inheritDoc} */
    public Set<InjectionPoint> getInjectionPoints() {
        return delegate.getInjectionPoints();
    }

    /** {@inheritDoc} */
    public void inject(final T instance, final CreationalContext<T> ctx) {
        delegate.inject(instance, ctx);
    }

    /** {@inheritDoc} */
    public void postConstruct(final T instance) {
        delegate.postConstruct(instance);
    }

    /** {@inheritDoc} */
    public void preDestroy(final T instance) {
        delegate.preDestroy(instance);
    }
}
