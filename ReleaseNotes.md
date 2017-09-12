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

