package org.easymock.tests;

public class Component {

    String getPackagePrivateValue() {
        return "bad";
    }

    public String getPublicValue() {
        return "bad";
    }

    protected String getProtectedValue() {
        return "bad";
    }
}
