This major release announce the move from Cglib to ByteBuddy. 
Sadly good old Cglib can't cope with all the tricks needed to workaround JPMS and reflection limitations.
It means you will most probably experience some issues until it stabilizes.

The good news are that this version is working up to Java 18.

Known issues:
* Serialization of a mock is broken (#312)
* Running in OSGi doesn't work (#313)

All help is greatly appreciated.
