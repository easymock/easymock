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

