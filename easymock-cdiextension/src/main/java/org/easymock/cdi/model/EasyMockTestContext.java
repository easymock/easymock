package org.easymock.cdi.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.easymock.EasyMockSupport;

/**
 * Holds data about the tests context.
 */
public final class EasyMockTestContext {

    /**
     * Static thread local instance.
     */
    private static final ThreadLocal<EasyMockTestContext> THREAD_INSTANCE =
            new ThreadLocal<EasyMockTestContext>() {
        @Override
        protected EasyMockTestContext initialValue() {
            return new EasyMockTestContext();
        }
    };

    /**
     * Private constructor.
     */
    private EasyMockTestContext() {
    }

    /**
     * Returns static instance.
     *
     * @return static instance
     */
    public static EasyMockTestContext getInstance() {
        return THREAD_INSTANCE.get();
    }

    /**
     * Cleans context.
     */
    public static void clean() {
        THREAD_INSTANCE.set(new EasyMockTestContext());
    }

    /**
     * Current execution context.
     */
    private Class<?> currentExecutionContext = null;

    /**
     * Class that will have mocks injected per context.
     */
    private final Map<Class<?>, Class<?>> testSubjectsPerContext =
        new HashMap<Class<?>, Class<?>>();

    /**
     * Mock definitions per context.
     */
    private final Map<Class<?>, Set<MockDefinition>> mockDefinitionsPerContext =
        new HashMap<Class<?>, Set<MockDefinition>>();

    /**
     * Mocks per context.
     */
    private final Map<Class<?>, Set<Object>> mocksPerContext =
        new HashMap<Class<?>, Set<Object>>();

    /**
     * Easymock support for mocks.
     */
    private final EasyMockSupport easyMockSupport = new EasyMockSupport();

    /**
     * Returns test context easymock support.
     * @return easy mock support
     */
    public EasyMockSupport getEasyMockSupport() {
        return easyMockSupport;
    }

    /**
     * Sets current execution context.
     * @param context context
     */
    public void setCurrentExecutionContext(final Class<?> context) {
        this.currentExecutionContext = context;
    }

    /**
     * Set the test subject class in a test context.
     *
     * @param context
     *            context (test class)
     * @param testSubject
     *            class where mocks should be used
     * @throws RuntimeException if there is already a test subject in the
     * context
     */
    public void setTestSubject(
            final Class<?> context,
            final Class<?> testSubject) {

        if (testSubjectsPerContext.containsKey(context)) {
            final Class<?> existingTestSubject = testSubjectsPerContext
                    .get(context);
            throw new IllegalArgumentException(
                    "Context "
                    + context.getName()
                    + " already has a test subject"
                    + existingTestSubject.getName());
        }

        testSubjectsPerContext.put(context, testSubject);
    }

    /**
     * Returns if candidate is a test subject.
     * @param candidate candidate
     * @return <code>true</code>, if yes. Otherwise, <code>false</code>.
     */
    public boolean isTestSubject(final Class<?> candidate) {
        final Collection<Class<?>> testSubjectClasses = testSubjectsPerContext
                .values();
        return testSubjectClasses.contains(candidate);
    }

    /**
     * Adds mock definition to context.
     * @param context context
     * @param mockDefinition mock definition
     */
    public void addMockDefinition(final Class<?> context,
            final MockDefinition mockDefinition) {
        Set<MockDefinition> mockDefinitions = mockDefinitionsPerContext
                .get(context);
        if (mockDefinitions == null) {
            mockDefinitions = new HashSet<MockDefinition>();
            mockDefinitionsPerContext.put(context, mockDefinitions);
        }
        mockDefinitions.add(mockDefinition);
    }

    /**
     * Returns if candidate is a context.
     * @param candidate candidate
     * @return <code>true</code>, if yes. Otherwise, <code>false</code>.
     */
    public boolean isContext(final Class<?> candidate) {
        return testSubjectsPerContext.containsKey(candidate);
    }

    /**
     * Returns all mocks to be used in context.
     * @param context context
     * @return context's mocks
     */
    public Set<Object> getMocks(final Class<?> context) {
        if (!mocksPerContext.containsKey(context)) {
            final Set<Object> mocks = createContextMocks(context);
            mocksPerContext.put(context, mocks);
        }

        return mocksPerContext.get(context);
    }

    /**
     * Create mocks used in the context.
     * @param context context
     * @return context's mocks
     */
    private Set<Object> createContextMocks(final Class<?> context) {
        final Set<MockDefinition> mockDefinitions = getMockDefinition(context);
        final Set<Object> mocks = new HashSet<Object>();
        for (final MockDefinition mockDefinition : mockDefinitions) {
            final Object mock = createMock(mockDefinition);
            mocks.add(mock);
        }
        return mocks;
    }

    /**
     * Creates mock.
     * @param mockDefinition mock definition
     * @return mock
     */
    private Object createMock(final MockDefinition mockDefinition) {
        if (mockDefinition.isNiceType()) {
            return easyMockSupport.createNiceControl().
                createMock(mockDefinition.getJavaType());
        } else if (mockDefinition.isStrictType()) {
            return easyMockSupport.createStrictControl().
                createMock(mockDefinition.getJavaType());
        }

        return easyMockSupport.createControl().createMock(
                mockDefinition.getJavaType());
    }

    /**
     * Returns mock definitions associated with context.
     * @param context context
     * @return mock definitions
     */
    private Set<MockDefinition> getMockDefinition(final Class<?> context) {
        final Set<MockDefinition> mockDefinitions = mockDefinitionsPerContext
                .get(context);
        if (mockDefinitions == null) {
            return Collections.emptySet();
        }
        return mockDefinitions;
    }

    /**
     * Get mocks to be used in current execution context.
     * @return current context mocks
     */
    public Set<Object> getCurrentExecutionMocks() {
        return getMocks(currentExecutionContext);
    }

    /**
     * Returns if candidate is the current context's test subject.
     * @param candidate candidate
     * @return <code>true</code>, if yes. Otherwise, <code>false</code>.
     */
    public boolean isCurrentContextTestSubject(
        final Class<?> candidate) {
        final Class<?> contextTestSubject = testSubjectsPerContext
                .get(currentExecutionContext);
        return contextTestSubject != null
                && contextTestSubject.equals(candidate);
    }
}
