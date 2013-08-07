easymock-cdiextension
============

This project is an Easymock CDI extension.

It provides mock injection in test classes and fields explicitly annotated with @TestSubject.
Each test class in execution has its own test context, so you can have the same CDI bean behaving as a strict mock in one test and as a normal bean in another one.

Browse the unit tests to see how they look like (https://github.com/marciopd/easymock/blob/master/easymock-cdiextension/src/test/java/org/easymock/cdi/DefaultMockTest.java).

You can run the tests using different CDI implementations: weld and openwebbeans.

This extension also provides:
- An EasyMock junit runner that bootstaps CDI (EasyMockCdiRunner).
- EasyMockSupport injection (Ex: https://github.com/marciopd/easymock/blob/master/easymock-cdiextension/src/test/java/org/easymock/cdi/EasyMockSupportTest.java).
- CDI interceptors unit testing (Ex: https://github.com/marciopd/easymock/blob/master/easymock-cdiextension/src/test/java/org/easymock/cdi/interceptor/InterceptorTest.java).


JIRA feature request linked with this fork at: https://jira.codehaus.org/browse/EASYMOCK-126 .
If you agree, please vote.


This work was inspired by the question in 
http://stackoverflow.com/questions/16761905/unit-test-with-cdi-unit-and-easymock/16814237#16814237 .


Project features tested successfully against:
- Weld: 1.1.3.SP1, 1.1.5.AS71.Final and 2.0.3.Final.
- OpenWebBeans: 1.1.6.

[![Build Status](https://buildhive.cloudbees.com/job/marciopd/job/easymock/badge/icon)](https://buildhive.cloudbees.com/job/marciopd/job/easymock/)
