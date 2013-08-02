package org.easymock.cdi.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.easymock.EasyMockSupport;
import org.easymock.TestSubject;

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
    private final Map<Class<?>, Set<Class<?>>> testsSubjectPerContext =
            new HashMap<Class<?>, Set<Class<?>>>();

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
        cleanAllCreatedMocks();
    }

    /**
     * Clean mocks created (thread).
     */
    private void cleanAllCreatedMocks() {
        this.mocksPerContext.clear();
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
    public void addTestSubject(
            final Class<?> context,
            final Class<?> testSubject) {

        final Set<Class<?>> testSubjects = getContextTestSubjects(context);
        if (testSubjects.contains(testSubject)) {
            throw new RuntimeException("Test subject " + testSubject +
                    " already declared in " + context);
        }
        testSubjects.add(testSubject);
    }

    /**
     * Gets context test subjects.
     * @param context context
     * @return test subjects
     */
    private Set<Class<?>> getContextTestSubjects(final Class<?> context) {
        Set<Class<?>> testSubjects = testsSubjectPerContext
                .get(context);
        if (testSubjects == null) {
            testSubjects = new HashSet<Class<?>>();
            testsSubjectPerContext.put(context, testSubjects);
        }
        return testSubjects;
    }

    /**
     * Returns if candidate is a test subject.
     * @param candidate candidate
     * @return <code>true</code>, if yes. Otherwise, <code>false</code>.
     */
    public boolean isTestSubject(final Class<?> candidate) {
        final Collection<Set<Class<?>>> testSubjectsAllContexts =
                testsSubjectPerContext.values();
        if (testSubjectsAllContexts != null) {
            for (final Set<Class<?>> testSubjects : testSubjectsAllContexts) {
                if (testSubjects.contains(candidate)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds mock definition to context.
     * @param context context
     * @param candidateMockDefinition mock definition
     */
    public void addMockDefinition(final Class<?> context,
            final MockDefinition candidateMockDefinition) {
        final Set<MockDefinition> mockDefinitions = getContextMockDefinitions(context);
        validateCandidateMockDefinition(context, mockDefinitions,
                candidateMockDefinition);
        mockDefinitions.add(candidateMockDefinition);
    }

    /**
     * Gets context mock definitions.
     * @param context context
     * @return mock definitions
     */
    private Set<MockDefinition> getContextMockDefinitions(final Class<?> context) {
        Set<MockDefinition> mockDefinitions = mockDefinitionsPerContext
                .get(context);
        if (mockDefinitions == null) {
            mockDefinitions = new HashSet<MockDefinition>();
            mockDefinitionsPerContext.put(context, mockDefinitions);
        }
        return mockDefinitions;
    }

    /**
     * Validates if mock definition candidate can be added to context.
     * @param context context
     * @param existingMockDefinitions existing context mock definitions
     * @param mockDefinitionCandidate mock definition candidate
     */
    private void validateCandidateMockDefinition(final Class<?> context,
            final Set<MockDefinition> existingMockDefinitions, final MockDefinition mockDefinitionCandidate) {
        for (final MockDefinition existingMockDefinition : existingMockDefinitions) {
            final Class<?> mockJavaType = mockDefinitionCandidate.getJavaType();
            final Class<?> existingMockJavaType = existingMockDefinition.getJavaType();
            if (existingMockJavaType.equals(mockJavaType)) {
                throw new RuntimeException(
                        mockJavaType + " already is mocked in " + context);
            }
        }
    }

    /**
     * Returns if candidate is a context.
     * @param candidate candidate
     * @return <code>true</code>, if yes. Otherwise, <code>false</code>.
     */
    public boolean isContext(final Class<?> candidate) {
        return !ReflectionHelper.getFieldsAnnotated(candidate, TestSubject.class).isEmpty();
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
        final Set<Class<?>> contextTestSubjects = testsSubjectPerContext
                .get(currentExecutionContext);
        return contextTestSubjects != null
                && contextTestSubjects.contains(candidate);
    }
}
