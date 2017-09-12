package org.easymock;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

public class EasyMockListener implements IInvokedMethodListener {

    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        EasyMockSupport.injectMocks(method.getTestMethod().getInstance());
    }

    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
    }
}
