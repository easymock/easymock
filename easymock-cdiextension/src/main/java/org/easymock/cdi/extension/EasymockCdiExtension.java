package org.easymock.cdi.extension;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

import org.easymock.Mock;
import org.easymock.TestSubject;
import org.easymock.cdi.model.EasyMockTestContext;
import org.easymock.cdi.model.MockDefinition;

/**
 * Easymock CDI extension.
 */
public class EasymockCdiExtension implements Extension {

    /**
     * Tests context.
     */
    private final EasyMockTestContext easyMockTestContext = EasyMockTestContext
            .getInstance();

    /**
     * Processes {@link ProcessAnnotatedType} event.
     * @param pat event
     * @param <T> type
     */
    public <T> void processAnnotatedType(
            @Observes final ProcessAnnotatedType<T> pat) {
        final AnnotatedType<T> annotatedType = pat.getAnnotatedType();
        processContextOrTestSubject(annotatedType);
    }

    /**
     * Process annotated type, if it's a test context or a
     * easymock test subject.
     * @param annotatedType annotated type
     * @param <T> type
     */
    private <T> void processContextOrTestSubject(
            final AnnotatedType<T> annotatedType) {
        final Set<AnnotatedField<? super T>> javaClassFields = annotatedType
                .getFields();
        for (final AnnotatedField<? super T> annotatedField : javaClassFields) {
            final Field javaField = annotatedField.getJavaMember();
            final Annotation[] annotations = javaField.getDeclaredAnnotations();
            if (annotations != null) {
                for (final Annotation fieldAnnotation : annotations) {
                    final Class<? extends Annotation> fieldAnnotationType =
                            fieldAnnotation.annotationType();
                    if (TestSubject.class.equals(fieldAnnotationType)) {
                        final Class<T> context = annotatedType.getJavaClass();
                        setTestSubject(context, javaField.getType());
                    }
                    if (Mock.class.equals(fieldAnnotationType)) {
                        final Class<T> context = annotatedType.getJavaClass();
                        addMockDefinition(context, javaField);
                    }
                }
            }
        }
    }

    /**
     * Adds mock definition to a context.
     * @param context test class
     * @param javaField java field
     * @param <T> type
     */
    private <T> void addMockDefinition(final Class<T> context,
            final Field javaField) {
        final MockDefinition mockDefinition = new MockDefinition();
        mockDefinition.setJavaType(javaField.getType());
        final Mock easyMockAnnotation = javaField.getAnnotation(Mock.class);
        mockDefinition.setMockType(easyMockAnnotation.type());
        easyMockTestContext.addMockDefinition(context, mockDefinition);
    }

    /**
     * Sets test subject to a context.
     * @param context test subject
     * @param testSubject test subject
     * @param <T> context type
     * @param <X> test subject type
     */
    private <T, X> void setTestSubject(final Class<T> context,
            final Class<X> testSubject) {
        easyMockTestContext.setTestSubject(context, testSubject);
    }

    /**
     * Decorates test contexts and test subjects injection targets.
     * @param pit event
     * @param <T> type
     */
    public <T> void decorateEasyMockInjectionTarget(
            @Observes final ProcessInjectionTarget<T> pit) {
        final AnnotatedType<T> annotatedType = pit.getAnnotatedType();

        final Class<T> javaClass = annotatedType.getJavaClass();
        if (easyMockTestContext.isContext(javaClass)) {
            final Set<Object> mocks = easyMockTestContext.getMocks(javaClass);
            pit.setInjectionTarget(new ContextInjectionTarget<T>(pit
                    .getInjectionTarget(), mocks));
        }

        if (easyMockTestContext.isTestSubject(javaClass)) {
            pit.setInjectionTarget(new TestSubjectInjectionTarget<T>(pit
                    .getInjectionTarget()));
        }

    }

    /**
     * Executes before CDI shutdown.
     *
     * @param event before shutdown event
     */
    public void beforeShutdown(@Observes final BeforeShutdown event) {
        EasyMockTestContext.clean();
    }
}
