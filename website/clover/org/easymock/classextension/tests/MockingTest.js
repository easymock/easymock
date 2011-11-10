var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":90,"id":7495,"methods":[{"el":63,"sc":5,"sl":52},{"el":69,"sc":5,"sl":65},{"el":75,"sc":5,"sl":71},{"el":81,"sc":5,"sl":77},{"el":85,"sc":5,"sl":83},{"el":89,"sc":5,"sl":87}],"name":"MockingTest","sl":35},{"el":47,"id":7495,"methods":[{"el":42,"sc":9,"sl":40},{"el":46,"sc":9,"sl":44}],"name":"MockingTest.ClassToMock","sl":39}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_1238":{"methods":[{"sl":52}],"name":"testTwoMocks","pass":true,"statements":[{"sl":54},{"sl":55},{"sl":58},{"sl":61},{"sl":62}]},"test_1341":{"methods":[{"sl":77},{"sl":83},{"sl":87}],"name":"testStrictInterfaceMocking","pass":true,"statements":[{"sl":79},{"sl":80},{"sl":84},{"sl":88}]},"test_2200":{"methods":[{"sl":65},{"sl":83},{"sl":87}],"name":"testInterfaceMocking","pass":true,"statements":[{"sl":67},{"sl":68},{"sl":84},{"sl":88}]},"test_2400":{"methods":[{"sl":71},{"sl":83},{"sl":87}],"name":"testNiceInterfaceMocking","pass":true,"statements":[{"sl":73},{"sl":74},{"sl":84},{"sl":88}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [1238], [], [1238], [1238], [], [], [1238], [], [], [1238], [1238], [], [], [2200], [], [2200], [2200], [], [], [2400], [], [2400], [2400], [], [], [1341], [], [1341], [1341], [], [], [2200, 2400, 1341], [2200, 2400, 1341], [], [], [2200, 2400, 1341], [2200, 2400, 1341], [], []]
