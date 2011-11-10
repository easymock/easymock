var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":112,"id":7959,"methods":[{"el":44,"sc":5,"sl":30},{"el":66,"sc":5,"sl":46},{"el":89,"sc":5,"sl":68},{"el":111,"sc":5,"sl":91}],"name":"MockedExceptionTest","sl":28}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_1286":{"methods":[{"sl":46}],"name":"testExplicitFillInStackTrace","pass":true,"statements":[{"sl":49},{"sl":50},{"sl":51},{"sl":53},{"sl":54},{"sl":56},{"sl":58},{"sl":59},{"sl":61},{"sl":62},{"sl":65}]},"test_2385":{"methods":[{"sl":91}],"name":"testRealException","pass":true,"statements":[{"sl":94},{"sl":96},{"sl":97},{"sl":99},{"sl":101},{"sl":102},{"sl":104},{"sl":105},{"sl":110}]},"test_2549":{"methods":[{"sl":30}],"name":"testMockedException","pass":true,"statements":[{"sl":32},{"sl":33},{"sl":34},{"sl":35},{"sl":37},{"sl":38},{"sl":40},{"sl":43}]},"test_2827":{"methods":[{"sl":68}],"name":"testNotMockedFillInStackTrace","pass":true,"statements":[{"sl":71},{"sl":74},{"sl":75},{"sl":77},{"sl":79},{"sl":80},{"sl":82},{"sl":83},{"sl":88}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [2549], [], [2549], [2549], [2549], [2549], [], [2549], [2549], [], [2549], [], [], [2549], [], [], [1286], [], [], [1286], [1286], [1286], [], [1286], [1286], [], [1286], [], [1286], [1286], [], [1286], [1286], [], [], [1286], [], [], [2827], [], [], [2827], [], [], [2827], [2827], [], [2827], [], [2827], [2827], [], [2827], [2827], [], [], [], [], [2827], [], [], [2385], [], [], [2385], [], [2385], [2385], [], [2385], [], [2385], [2385], [], [2385], [2385], [], [], [], [], [2385], [], []]
