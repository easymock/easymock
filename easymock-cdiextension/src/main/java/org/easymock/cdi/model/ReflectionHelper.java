package org.easymock.cdi.model;

import java.util.Collection;
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
}
