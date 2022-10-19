package org.easymock.tests;

import static org.easymock.EasyMock.expect;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import org.easymock.EasyMockSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MethodTest extends EasyMockSupport {

    private Component component;

    @BeforeEach
    void setUp() {
        component = mock(Component.class);
    }

    @Test
    void publicMethodIsWorking() {
        expect(component.getPublicValue()).andReturn("public");

        replayAll();
        assertEquals("public", component.getPublicValue());
        verifyAll();
    }

    @Test
    void packageMethodIsWorking() {
        expect(component.getPackagePrivateValue()).andReturn("package");

        replayAll();
        assertEquals("package", component.getPackagePrivateValue());
        verifyAll();
    }

    @Test
    void protectedMethodIsWorking() {
        expect(component.getProtectedValue()).andReturn("protected");

        replayAll();
        assertEquals("protected", component.getProtectedValue());
        verifyAll();
    }

}
