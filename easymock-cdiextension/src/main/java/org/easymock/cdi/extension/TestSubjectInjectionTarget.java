package org.easymock.cdi.extension;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionTarget;

import org.easymock.cdi.model.EasyMockTestContext;
import org.powermock.reflect.Whitebox;

/**
 * Test subject {@link InjectionTarget} wrapper.
 *
 * @param <T> type
 */
public final class TestSubjectInjectionTarget<T> extends
        AbstractDelegateInjectionTarget<T> {

    /**
     * Constructor with args.
     * @param delegate delegate
     */
    public TestSubjectInjectionTarget(final InjectionTarget<T> delegate) {
        super(delegate);
    }

    /**
     * Injects CDI beans and mocks in the instance, if it's a test subject.
     * @param instance bean instance
     * @param ctx creational context
     */
    @Override
    public void inject(final T instance, final CreationalContext<T> ctx) {
        getDelegate().inject(instance, ctx);
        final EasyMockTestContext easyMockTestContext = EasyMockTestContext
                .getInstance();
        if (easyMockTestContext.isCurrentContextTestSubject(instance
                .getClass())) {
            final Set<Object> mocks = easyMockTestContext
                    .getCurrentExecutionMocks();
            setInternalStateWithMocks(instance, mocks);
        }
    }

    /**
     * Sets mocks in bean.
     * @param bean bean
     * @param mocks mocks
     */
    private void setInternalStateWithMocks(
            final T bean, final Set<Object> mocks) {
        for (final Object mock : mocks) {
            Whitebox.setInternalState(bean, mock);
        }
    }
}
