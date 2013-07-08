easymock-cdiextension
============

This project is an Easymock CDI extension.

It provides mock injection in test classes and fields explicitly annotated with @TestSubject.
Each test class in execution has its own test context, so you can have the same CDI bean behaving as a strict mock in one test and as a nice mock in another one.

Browse the unit tests to see how the tests looks like (https://github.com/marciopd/easymock/blob/master/easymock-cdiextension/src/test/java/org/easymock/cdi/HelloWorldBusinessTest.java).

This extension also supports:
- CDI interceptors unit testing (Ex: https://github.com/marciopd/easymock/blob/master/easymock-cdiextension/src/test/java/org/easymock/cdi/interceptor/HelloWorldInterceptorTest.java).
- EasyMockSupport injection (Ex: https://github.com/marciopd/easymock/blob/master/easymock-cdiextension/src/test/java/org/easymock/cdi/HelloWorldEasyMockSupportTest.java).



This work was inspired by the question in 
http://stackoverflow.com/questions/16761905/unit-test-with-cdi-unit-and-easymock/16814237#16814237 and has the following projects as references:
- http://junitcdi.sandbox.seasar.org/junitcdi-easymock/index.html
- http://docs.mockito.googlecode.com/hg/latest/org/mockito/InjectMocks.html
