package org.easymock.cdi.extension;

import java.util.logging.Level;
import java.util.logging.Logger;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import org.easymock.cdi.model.EasyMockTestContext;

/**
 * {@link org.apache.webbeans.intercept.webbeans.WebBeansInterceptor}
 * injection wrapper.
 *
 * This is OWB class responsible for creating and injecting dependencies
 * in interceptor beans.
 *
 */
public class OpenWebBeansInterceptorInjectionWrapper {

    /**
     * Logger.
     */
    private final static Logger LOGGER = Logger.
            getLogger(OpenWebBeansInterceptorInjectionWrapper.class.getName());

    /**
     * Original method suffix.
     */
    private static final String ORIGINAL_METHOD_SUFFIX = "_orig";

    /**
     * Wrapped method name.
     */
    private static final String SET_INJECTIONS_METHOD_NAME = "setInjections";

    /**
     * Static instance.
     */
    private static OpenWebBeansInterceptorInjectionWrapper INSTANCE = new OpenWebBeansInterceptorInjectionWrapper();

    /**
     * Class name.
     */
    private static final String WEB_BEANS_INTERCEPTOR_CLASSNAME = "org.apache.webbeans.intercept.webbeans.WebBeansInterceptor";

    /**
     * Message.
     */
    private static final String MSG_OWB_INTERCEPTOR_CLASS_FROZEN = WEB_BEANS_INTERCEPTOR_CLASSNAME
            + " class frozen to modifications.";

    /**
     * Message.
     */
    private static final String MSG_OWB_INTERCEPTOR_NOT_FOUND = WEB_BEANS_INTERCEPTOR_CLASSNAME
            + " not found.";

    /**
     * Message.
     */
    private static final String MSG_SUCESSFULL_WRAP = WEB_BEANS_INTERCEPTOR_CLASSNAME
            + " succesfully wrapped.";

    /**
     * Message.
     */
    private static final String MSG_WRAPPING_ERROR = "Interceptor mocking not supported: WebBeansInterceptor.setInjections couldn't be wrapped.";

    /**
     * Returns a instance.
     * @return instance
     */
    public static OpenWebBeansInterceptorInjectionWrapper getInstance() {
        return INSTANCE;
    }

    /**
     * Wraps {@link org.apache.webbeans.intercept.webbeans.WebBeansInterceptor#setInjections(Object, javax.enterprise.context.spi.CreationalContext)}
     * to disable the container's dependency injection in the inteceptor when it's a test subject.
     *
     *  That's needed because the container re-injects the dependencies before
     *  each interceptor is executed, overriding the mocks previously
     *  set by the {@link TestSubjectCandidateInjectionTarget}.
     */
    public void wrapSetInjectionsMethod() {

        final ClassPool classPool = ClassPool.getDefault();
        CtClass webBeansInterceptorClass;
        try {
            webBeansInterceptorClass = classPool
                    .get(WEB_BEANS_INTERCEPTOR_CLASSNAME);
        } catch (final NotFoundException e1) {
            LOGGER.info(MSG_OWB_INTERCEPTOR_NOT_FOUND);
            return;
        }

        if (webBeansInterceptorClass.isFrozen()) {
            LOGGER.info(MSG_OWB_INTERCEPTOR_CLASS_FROZEN);
            return;
        }

        wrapSetInjectionsMethod(webBeansInterceptorClass);
    }

    /**
     * Wraps method.
     * @param webBeansInterceptorClass web beans interceptor class.
     */
    private void wrapSetInjectionsMethod(final CtClass webBeansInterceptorClass) {
        try {
            final CtMethod setInjectionsMethod = webBeansInterceptorClass
                    .getDeclaredMethod(SET_INJECTIONS_METHOD_NAME);
            final CtMethod setInjectionsMethodWrapper = CtNewMethod.copy(setInjectionsMethod,
                    webBeansInterceptorClass, null);
            setInjectionsMethod.setName(setInjectionsMethod.getName() + ORIGINAL_METHOD_SUFFIX);
            setInjectionsMethodWrapper.setBody(
                    "if (!"
                            + EasyMockTestContext.class.getName()
                            + ".getInstance().isCurrentContextTestSubject($1.getClass())) {"
                            + "$proceed($$);"
                            + "}", "this", setInjectionsMethod.getName()
                    );
            webBeansInterceptorClass.addMethod(setInjectionsMethodWrapper);
            webBeansInterceptorClass.toClass();
            LOGGER.info(MSG_SUCESSFULL_WRAP);
        } catch (final Exception errorCause) {
            LOGGER.log(
                    Level.SEVERE,
                    MSG_WRAPPING_ERROR,
                    errorCause);
        }
    }
}
