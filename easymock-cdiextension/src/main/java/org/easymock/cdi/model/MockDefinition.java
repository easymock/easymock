package org.easymock.cdi.model;

import java.io.Serializable;

import org.easymock.MockType;

/**
 * Mock definition.
 */
public final class MockDefinition implements Serializable {

    /** serial. */
    private static final long serialVersionUID = 5672584709142269950L;

    /**
     * Java type to be mocked.
     */
    private Class<?> javaType;

    /**
     * Mock type.
     */
    private MockType mockType;

    /**
     * Setter.
     * @param type type
     */
    public void setMockType(final MockType type) {
        this.mockType = type;
    }

    /**
     * Getter.
     * @return value
     */
    public Class<?> getJavaType() {
        return javaType;
    }

    /**
     * Setter.
     * @param type type
     */
    public void setJavaType(final Class<?> type) {
        this.javaType = type;
    }

    /**
     * Returns if mocktype is default.
     * @return <code>true</code>, if yes. Otherwise, <code>false</code>.
     */
    public boolean isDefault() {
        return mockType == null || MockType.STRICT.equals(mockType);
    }

    /**
     * Returns if mocktype is strict.
     * @return <code>true</code>, if yes. Otherwise, <code>false</code>.
     */
    public boolean isStrictType() {
        return mockType != null && MockType.STRICT.equals(mockType);
    }

    /**
     * Returns if mocktype is nice.
     * @return <code>true</code>, if yes. Otherwise, <code>false</code>.
     */
    public boolean isNiceType() {
        return mockType != null && MockType.NICE.equals(mockType);
    }
}
