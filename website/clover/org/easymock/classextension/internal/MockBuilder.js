var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":35,"id":7444,"methods":[{"el":33,"sc":5,"sl":31}],"name":"MockBuilder","sl":28}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_2574":{"methods":[{"sl":31}],"name":"test","pass":true,"statements":[{"sl":32}]},"test_2739":{"methods":[{"sl":31}],"name":"testCreateMockBuilder","pass":true,"statements":[{"sl":32}]},"test_2827":{"methods":[{"sl":31}],"name":"testNotMockedFillInStackTrace","pass":true,"statements":[{"sl":32}]},"test_2899":{"methods":[{"sl":31}],"name":"testWithEmptyConstructor_NoEmptyConstructor","pass":true,"statements":[{"sl":32}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [2827, 2899, 2574, 2739], [2827, 2899, 2574, 2739], [], [], []]
