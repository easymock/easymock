var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":42,"id":8080,"methods":[{"el":37,"sc":5,"sl":36},{"el":41,"sc":5,"sl":39}],"name":"NopInvocationHandler","sl":26}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_2774":{"methods":[{"sl":36}],"name":"testGetControl_ProxyButNotMock","pass":true,"statements":[]},"test_2872":{"methods":[{"sl":39}],"name":"testStrictMock_Partial","pass":true,"statements":[{"sl":40}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [2774], [], [], [2872], [2872], [], []]
