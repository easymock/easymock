Add support to Java 15. TestSubject is now initialized when null by calling the no-arg constructor.

Change log
----------
* Support OpenJDK 15-EA and class version 59 ([#252](https://github.com/easymock/easymock/issues/252))
* Try instantiating TestSubject automatically using its constructor without arguments ([#251](https://github.com/easymock/easymock/pull/251))
