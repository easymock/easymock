package org.easymock.cdi;

public class HelloWorldDao {

    public void doSomething() {
        throw new IllegalStateException("Shouldn't be called.");
    }
}
