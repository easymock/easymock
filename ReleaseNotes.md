This version has finally removed the old (and ugly) partial mocking methods. You are now required to use the partialMockBuilder. In exchange, you get really nice short methods to create your mocks: `mock`, `niceMock` and `strictMock`.
 
You also get a better stability since cglib and ASM are now embedded to remove a possible version mismatch with your own dependencies. Note that Objenesis will stay as an explicit dependency.

Change log
----------
* #163 Incomplete sentence in documentation bug website
* #160 EasyMock should not depend on ASM but inline the code enhancement major
* #159 Remove partial mocking deprecated methods enhancement fixed major
* #158 Shorter mock methods core enhancement fixed major
* #155 Disable jar indexing enhancement fixed minor
* #11 User guide refers to methods not in latest release website
