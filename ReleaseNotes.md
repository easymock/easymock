This is a bug fix release of 3.5. It mainly fix dependency issues (ant in the classpath) caused by the latest
cglib release.

Release notes
-------------
* Remove ant from the classpath
* Put TestNG and JUnit in provided
* Pull ASM 6 out of beta

Change log
----------
* Easymock 3.5 is aliasing other packages on the classpath ([#207](https://github.com/easymock/easymock/issues/207))
* Ant dependency is incorrectly packaged into easymock:jar:3.5 ([#205](https://github.com/easymock/easymock/issues/205))
