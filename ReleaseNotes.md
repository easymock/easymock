Here is the long awaited 3.5 version. It contains many bug fixes and some improvement. We allowed ourselves to possibly break
the compatibility with older versions for the greater good. So please read these notes thoroughly.

Release notes
-------------
* Java 5 is no longer supported. I dearly hope this won't harm anyone
* Java 9 is supported
* TestNG support is added. Have a look at `EasyMockListener`
* Class Mocking now works correctly for cross bundle mocking
* `verify()` now checks for unexpected calls in case an AssertionError was swallowed during the test. It is in general
what you want but you can use `verifyRecording()` to bring back the old behavior
* Default matcher for an array argument is now `aryEq` instead of `eq`. This should as well make sense for everyone and should 
allow you to remove tons of `aryEq` all over your code. If you don't like it, you can specify `eq` explicitly for the array
argument

Change log
----------
* isNull and notNull with generic class parameter ([#93](https://github.com/easymock/easymock/issues/93))
* Return a meaningful error when null on a primitive ([#92](https://github.com/easymock/easymock/issues/92))
* Create opportunity to disable SingleThread checks ([#88](https://github.com/easymock/easymock/issues/88))
* slightly more intuitive error message ([#80](https://github.com/easymock/easymock/issues/80))
* Enhancement for andAnswer / andStubAnswer ([#79](https://github.com/easymock/easymock/issues/79))
* Make easymock and easymock-ce OSGi-ready ([#78](https://github.com/easymock/easymock/issues/78))
* Enable Multiple Captures ([#77](https://github.com/easymock/easymock/issues/77))
* Improve multithreading error report in MocksBehavior ([#73](https://github.com/easymock/easymock/issues/73))
* Stack trace clobbered when exception thrown by IAnswer impl ([#34](https://github.com/easymock/easymock/issues/34))
* Possible bug with captures() ([#30](https://github.com/easymock/easymock/issues/30))
* Actual value in byte array failure is not helpful ([#29](https://github.com/easymock/easymock/issues/29))
* Regression caused by new threadsafe API and defaults ([#27](https://github.com/easymock/easymock/issues/27))
* Capturing parameters from single argument methods ([#24](https://github.com/easymock/easymock/issues/24))
* NPE with varargs in record state ([#22](https://github.com/easymock/easymock/issues/22))
* capture(Capture) only captures last method call ([#21](https://github.com/easymock/easymock/issues/21))
