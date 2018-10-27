This release adds support for Java 11 and moves to Java 8.

EasyMock inference has been changed. It should be backward compatible in most cases. However,
a type witness might be necessary.

To be clear, starting now `List<String> list = mock(List.class);` will compile perfectly without
any warning. However, `String s = mock(List.class);` will also compile. But I'm expecting you
not to be crazy enough to do such thing. It will do a `ClassCastException` at runtime anyway.

However, you might see your code failing to infer the return type, in that case, use a type
witness e.g. `foo(EasyMock.<List<String>>mock(List.class)` and it will some the problem nicely,
and again, without a warning.

Release notes
-------------
* Add Java 11 support
* Drop Java 6 and 7 support. Support now starts at Java 8
* Decorrelate mock requested and returned type

Change log
----------
* Remove most long time deprecated methods ([#231](https://github.com/easymock/easymock/issues/231))
* Relax typing for the mocking result ([#229](https://github.com/easymock/easymock/issues/229))
* Upgrade Objenesis to 3.0.1 ([#228](https://github.com/easymock/easymock/issues/228))
* Update cglib to 3.2.8 and asm to 6.2.1 ([#225](https://github.com/easymock/easymock/pull/225))
* Java 11 Compatibility check: EasyMock ([#224](https://github.com/easymock/easymock/issues/224))
* easymock 3.6 can't work with JDK11 EA kit ([#218](https://github.com/easymock/easymock/issues/218))
* update testng to 6.14.3 ([#216](https://github.com/easymock/easymock/pull/216))
