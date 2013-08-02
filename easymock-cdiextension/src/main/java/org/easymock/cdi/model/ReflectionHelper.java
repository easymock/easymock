package org.easymock.cdi.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.powermock.reflect.Whitebox;

/**
 * Helper class.
 */
public final class ReflectionHelper {

    /**
     * Logger.
     */
    private final static Logger LOGGER = Logger.
            getLogger(ReflectionHelper.class.getName());

    /**
     * Not to be instantiated.
     */
    private ReflectionHelper() {
    }

    /**
     * Return object's field value.
     * @param object object
     * @param fieldType type of the field
     * @return field's value
     */
    public static <T> T getInternalState(final Object object,
            final Class<T> fieldType) {
        return Whitebox.getInternalState(object, fieldType);
    }

    /**
     * Sets each value from the collection in object, if assignable.
     * @param object object
     * @param values value collection
     */
    public static void setInternalState(final Object object,
            final Collection<Object> values) {
        for (final Object value : values) {
            setInternalState(object, value);
        }
    }

    /**
     * Sets value in object, if assignable.
     * @param object object
     * @param value value
     */
    public static void setInternalState(final Object object, final Object value) {
        try {
            Whitebox.setInternalState(object, value);
        } catch (final RuntimeException e) {
            LOGGER.info("Ignoring field type " + value.getClass()
                + " in " + object.getClass() );
        }
    }

    /**
     * Returns fields from javaClass annotated with annotation.
     * @param javaClass java class
     * @param annotation annotation
     * @return fields or empty list
     */
    public static <T extends Annotation> List<Field> getFieldsAnnotated(final Class<?> javaClass,
            final Class<T> annotation) {

        final List<Field> fieldsAnnotated = new ArrayList<Field>();

        final Field[] declaredFields = javaClass.getDeclaredFields();
        if (declaredFields != null) {
            for (final Field javaField : declaredFields) {
                final Annotation[] annotations = javaField.getDeclaredAnnotations();
                if (annotations != null) {
                    for (final Annotation fieldAnnotation : annotations) {
                        if (annotation.equals(fieldAnnotation.annotationType())) {
                            fieldsAnnotated.add(javaField);
                        }
                    }
                }
            }
        }

        return fieldsAnnotated;
    }
}
