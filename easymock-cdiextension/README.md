easymock-cdiextension
============

This project is an Easymock CDI extension.

It provides mock injection in test classes and fields explicitly annotated with @TestSubject.
Each test class in execution has its own test context, so you can have the same CDI bean behaving as a strict mock in one test and as a normal bean in another one.

Browse the unit tests to see how they look like (https://github.com/marciopd/easymock/blob/master/easymock-cdiextension/src/test/java/org/easymock/cdi/DefaultMockTest.java).

This extension also provides:
- An EasyMock junit runner that bootstaps CDI (EasyMockCdiRunner).
- EasyMockSupport injection (Ex: https://github.com/marciopd/easymock/blob/master/easymock-cdiextension/src/test/java/org/easymock/cdi/EasyMockSupportTest.java).
- CDI interceptors unit testing (Ex: https://github.com/marciopd/easymock/blob/master/easymock-cdiextension/src/test/java/org/easymock/cdi/interceptor/InterceptorTest.java).


JIRA feature request linked with this fork at: https://jira.codehaus.org/browse/EASYMOCK-126 .
If you agree, please vote.


This work was inspired by the question in 
http://stackoverflow.com/questions/16761905/unit-test-with-cdi-unit-and-easymock/16814237#16814237 .

