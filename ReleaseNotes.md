This version has finally removed the old (and ugly) partial mocking methods. You are now required to use the partialMockBuilder. In exchange, you get really nice short methods to create your mocks: `mock`, `niceMock` and `strictMock`.
 
You also get a better stability since cglib and ASM are now embedded to remove a possible version mismatch with your own dependencies. Note that Objenesis will stay as an explicit dependency.
 
