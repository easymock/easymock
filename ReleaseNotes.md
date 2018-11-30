This is a quick fix to remove the second generic parameter that was introduced
in version 4. It breaks backward compatibility and is actually unnecessary.

Change log
----------
* No need to have two generic parameters on EasyMock.createMock ([#237](https://github.com/easymock/easymock/issues/237))
