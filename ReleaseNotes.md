This major release announce the move from Cglib to ByteBuddy. 
Sadly good old Cglib can't cope with all the tricks needed to workaround JPMS and reflection limitations.
It means you will most probably experience some issues until it stabilizes.

The good news are that this version is working up to Java 18.

Known issues:
* Serialization of a mock is broken (#312)
* Running in OSGi doesn't work (#313)

All help is greatly appreciated.

Change log
----------
* Replace Cglib with Bytebuddy to support Java 9+ ([#300](https://github.com/easymock/easymock/pull/300))
* Fix core source-jar, added maven-source-plugin ([#283](https://github.com/easymock/easymock/pull/283))
* Upgrade to ASM 9.2 so that easymock can work with JDK18-EA #277 ([#278](https://github.com/easymock/easymock/pull/278))
* Upgrade to ASM 9.2 so that easymock can work with JDK18-EA ([#277](https://github.com/easymock/easymock/issues/277))
* Does not work with Java 17 ([#274](https://github.com/easymock/easymock/issues/274))
* Allow @Mock to get a default name from the variable being mocked ([#260](https://github.com/easymock/easymock/issues/260))
* Easymock doesn't work in Java 11 with --illegal-access=deny ([#235](https://github.com/easymock/easymock/issues/235))
