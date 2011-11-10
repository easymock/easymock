function showContent() {
    document.getElementById('loading').style.display = 'none';
    document.getElementById('content').style.display = '';
}

function toggleInlineStats(ele, hiddenEleId) {
    var statsEle = document.getElementById(hiddenEleId);
    var showStats = ele.className.match(/\bmore\b/);
    statsEle.style.display = showStats ? '' : 'none';
    var regex = showStats ? /\bmore\b/ : /\bless\b/ ;
    var replacement = showStats ? 'less' : 'more';
    replaceClass(ele, regex, replacement);
}
function toggleNodeExpansion(ele, collapsed, expanded) {
    toggleNodeEx(document.getElementById(ele), document.getElementById(collapsed), document.getElementById(expanded));
}

function toggleNodeEx(ele, collapsed, expanded) {
    var expand = expanded.style.display == 'none';
    collapsed.style.display = expand ? 'none' : '';
    expanded.style.display = expand ? '' : 'none';

    var regex = expand ? /\bexpand\b/ : /\bcollapse\b/ ;
    var replacement = expand ? 'collapse' : 'expand';
    replaceClass(ele, regex, replacement);
}


var cloverHideTestsDesc = 'Hide Tests';
var cloverShowTestsDesc = 'Show Tests';

// Toggle all tests
function toggleAllTests(element) {
    var hideAll = (element.innerHTML == cloverHideTestsDesc);
    forceToggleAllTests(element, hideAll);
}


// Force toggle all tests
function forceToggleAllTests(element, hideAll) {

    var testsBody = document.getElementById("tests-body");
    var testsInfo = document.getElementById("tests-info");
    var display = !hideAll ? '' : 'none';
    testsBody.style.display = display;
    if (testsInfo != undefined) {
        testsInfo.style.display = display;
    }
    element.innerHTML = hideAll ? cloverShowTestsDesc : cloverHideTestsDesc;
}
// toggle all the tests, and return true. 
function toTest(testId) {
    forceToggleAllTests(document.getElementById('testsToggle'));
    window.location.hash = 'testA-' + testId;
    return true;
}

// Toggle all method summaries
function toggleAllClasses(element) {
    var expandAll = (element.innerHTML == 'Expand All');
    forceToggleAllClasses(element, expandAll);
}
//
function forceToggleAllClasses(element, expandAll) {

    setToggleLabel(element,  expandAll);
    var display = (expandAll) ? '' : 'none';
    var visitor = function (classData) {
        setDisplayOnAll(classData.name, classData.methods.length, display);
        var span = document.getElementById('span-' + classData.name);
        if (expandAll) {
            swapExpand(span);
        } else {
            swapCollapse(span);
        }
    };
    visitAllClasses(visitor);
}

// traverses the class data, and executes visitor on each class
function visitAllClasses(visitor) {
    for (var i = 0; i < clover.pageData.classes.length; i++) {
        var classData = clover.pageData.classes[i];
        visitor(classData);
    }
}

// toggle the display of all method summaries for a given class 
function toggleClass(ele, className, rowCount) {

    var firstnode = document.getElementById(className + "-" + (1));
    if (firstnode == null) return;
    var display = 'none' ;
    if (firstnode.style.display == 'none') {
        display = '';
        swapExpand(ele);
    } else {
        swapCollapse(ele);
    }
    setDisplayOnAll(className, rowCount, display);
    return display;
}

// sets the display on each method summary for a class.
function setDisplayOnAll(className, rowCount, display) {
    for (var i = 0; i < rowCount; i++) {
        var node = document.getElementById(className + "-" + (i + 1));
        node.style.display = display;
    }
}

function toggleAllInlineMethods(element) {
    var inlineExpandAll = (element.innerHTML == 'Expand All');
    setToggleLabel(element, inlineExpandAll);
    var visitor = function (method) {
        var startEle = document.getElementById('img-' + method.sl);
        forceToggleSrcRowVis(startEle, method.sl, method.el, inlineExpandAll);
    };
    visitAllMethods(visitor);
}

// traveses the data model executes the visitor() function on each method.
function visitAllMethods(visitor) {

    var classes = clover.pageData.classes;
    for (var i = 0; i < classes.length; i++) {
        var classData = classes[i];
        var methods = classData.methods;
        for (var j = 0; j < methods.length; j++) {
            var method = methods[j];
            visitor(method);
        }
    }
}

// traveses the DOM, executes visitor for each src-LineNumber element
function visitAllSrcLines(visitor) {

    var ele;
    var i = 1;
    while ((ele = document.getElementById("src-" + i)) != undefined){
        visitor(ele, i);
        i++;
    }
}

function toggleSrcRowVis(toggle, start, end) {
    var first = start + 1;
    var display = 'none';
    var expand = document.getElementById("l" + first).style.display == 'none';
    forceToggleSrcRowVis(toggle, start, end, expand);
}

// expands or collapses a method
function forceToggleSrcRowVis(toggle, start, end, expand) {
    var display = expand ? '' : 'none';
    if (expand) {
        swapExpandImg(toggle);
        document.getElementById("e"+start).style.display='none';
    } else {
        swapCollapseImg(toggle);
        document.getElementById("e"+start).style.display='';
    }

    for (var i = start + 1; i <= end; i++) {
        document.getElementById("l" + i).style.display = display;
    }
}

// Acts as a map: methodStartLine --> hit count
var methodsToHiLight = new Object();
// srcLine --> hit count
var linesToHiLight = new Object();
// linuNum --> LineTestInfo
var selectedLinesTestResult = new Object();

function loadPkgPane(title, fileName) {
    if (parent.packagePane != undefined && fileName != undefined) {
        parent.packagePane.location.href = fileName;
        setBrowserTitle(title);
    }
}

function setBrowserTitle(title) {
    if (title != undefined && parent) {
        parent.document.title = title;
    }
}

function onLoad(title) {
    var qs = new QueryString();
    qs.Read();

    var testId = qs.GetValue("id");
    var lineNo = qs.GetValue("line");
    
    if (testId != undefined) {
        hilightTestOnLoad(testId);
    } else if (lineNo != undefined) {
        hilightLineOnLoad(lineNo);
    }

    setBrowserTitle(title);

}

function hilightLineOnLoad(lineNo) {
    var srcLineSpan = document.getElementById('src-' + lineNo);
    if (srcLineSpan == undefined) return;
    window.setTimeout("window.scrollBy(0, -50);", 10);
    flashLine(srcLineSpan);
}

function flashLine(obj) {
    var originalColor = obj.style.backgroundColor;
    var fade = {
        full : function() {
            obj.style.backgroundColor = '#ffe7c6';
        },
        clear : function() {
            obj.style.backgroundColor = originalColor;
        }
    };
    for (var i = 0; i <= 12; i++) {
        var fadeMethod = (i % 2 == 0) ? fade.clear : fade.full;
        setTimeout(fadeMethod, 150 * i);
    }
}

function hilightTestOnLoad(testId) {
    var cbox = document.getElementById('cb-' + testId);
    if (cbox == undefined) return;
    cbox.checked = true;
    hiLightByTest(testId, true);
    toggleAllClasses(document.getElementById('stat-expander'));
}

// visit all test checkboxes on the page.
function visitAllCheckboxes(visitor) {
    var testCheckboxes = document.getElementsByName("testMethod");
    for (var i = 0; i < testCheckboxes.length; i++ ) {
        visitor(testCheckboxes[i]);
    }
}

var selectTestVisitor = function(testCheckbox) {
    if (!testCheckbox.checked) {
        testCheckbox.checked = true;
        hiLightByTest(testCheckbox.value, true);
    }
};


var deselectTestVisitor = function(testCheckbox) {
    if (testCheckbox.checked) {
        testCheckbox.checked = false;
        hiLightByTest(testCheckbox.value, false);
    }
};

// select all tests for hi-lighting
function selectAllTests(ele) {
    visitAllCheckboxes(selectTestVisitor);
    toggleTestSelectionButtons(ele, 'deselectalltests');
}

// deselect all tests 
function deselectAllTests(ele) {
    visitAllCheckboxes(deselectTestVisitor);
    toggleTestSelectionButtons(ele, 'selectalltests');

}

function toggleTestSelectionButtons(ele, ele2Id) {
    replaceClass(ele, /\bunselected\b/, 'selected');
    var ele2 = document.getElementById(ele2Id);
    replaceClass(ele2, /\bselected\b/, 'unselected');
}

// called in the onlick on the srcLine margin
function showTestsForLine(ele, startLine, overTitle) {
    var parentTestDiv = createTableForPopup(startLine);
    var xValue = ele.offsetLeft + ele.offsetWidth + 10;
    var aOverTitle = "<a href='javascript:return cClick();' title='Close' onclick='return cClick();'>" + overTitle + "</a>";
    return overlib(parentTestDiv.innerHTML, WRAP, TEXTSIZE, 3, SHADOW, SHADOWOPACITY, 80, CAPTION, aOverTitle, FIXX, xValue, STICKY);
}


function showFailingTestsPopup(element, line, traces) {
    var holderDiv = document.createElement('div');

    for (var i = 0; i < traces.length; i++) {
        var tid = traces[i][0];
        var fid = traces[i][1];

        var traceDiv = document.getElementById('trace-'+tid);
        if (traceDiv == undefined) {
            continue;
        }
        var traceControl = document.getElementById('traceControl').cloneNode(true);
        traceControl.className = 'expand';
        traceControl.id = "traceControl"+tid+'-'+i;

        var traceCol = document.getElementById('traceCol'+tid).cloneNode(true);
        traceCol.id = 'traceCol'+tid+'-'+i;
        var traceEx = document.getElementById('traceEx'+tid).cloneNode(true);
        traceEx.id = 'traceEx'+tid+'-'+i;

        var traceLines = traceEx.getElementsByTagName('div');

        traceLines[fid].className='errorTraceStrong';

        holderDiv.appendChild(traceControl);
        holderDiv.appendChild(traceCol);
        holderDiv.appendChild(traceEx);
        holderDiv.appendChild(document.createElement('br'));


        traceControl.setAttribute("onclick","toggleNodeExpansion('"+traceControl.id+"', '"+traceCol.id+"', '"+traceEx.id+"');");
    }

    var xValue = element.offsetLeft + element.offsetWidth + 75; // hack. need better position control here.
    return overlib(holderDiv.innerHTML, TEXTSIZE, 3, BGCLASS, "stOverBG", CGCLASS, "stOverCaption", CAPTIONFONTCLASS,"stOverCaption", CAPTION, "Test failures at line " + line, FIXX, xValue, STICKY);
}

// gernerate the contents for the per-srcline popup on the fly.
function createTableForPopup(startLine) {

    var holderDiv = document.createElement('div');
    var table = document.createElement('table');
    var tbody = document.createElement('tbody');
    holderDiv.appendChild(table);
    table.appendChild(tbody);
    
    tbody.appendChild(document.getElementById("testHeaderRow").cloneNode(true));
    var testIdsForLine = clover.srcFileLines[startLine];

    var missingTestCount = 0;
    for (var i = 0; i < testIdsForLine.length; i++) {
        var testId = testIdsForLine[i];
        var linksRow = document.getElementById('test-' + testId);
        if (linksRow == undefined) {
            missingTestCount = missingTestCount + 1;
            continue;
        }

        var clonedLinksRow = linksRow.cloneNode(true);
        var tds = clonedLinksRow.childNodes;

        var cbTd = findFirstChild(tds, 'TD', 'checkbox');
        var cb = findFirstChild(cbTd.childNodes, 'INPUT', 'testMethod');
        var checkBox = document.getElementById('cb-' + testId);

        while (cbTd.firstChild) {
            cbTd.removeChild(cbTd.firstChild);
        }

        var img = document.createElement('img');
        cbTd.appendChild(img);
        var imgSrc = checkBox.checked ? 'tick.gif' : 'back.gif';
        img.src = rootRelPath + 'img/' + imgSrc;
        img.className = "icon";
        img.setAttribute('onclick', 'toTest(' + testId + '); return false');

        tbody.appendChild(clonedLinksRow);
    }
    if (missingTestCount > 0) {
        var row = document.createElement('row');
        var col = document.createElement('td');
        col.colSpan = 4;
        var text = document.createTextNode(missingTestCount + ' ' +
                                           pluralise(missingTestCount, 'test is', 'tests are') +
                                           ' not displayed. This report was configured to display the top ' +
                                           testsPerFile + ' contributing tests for this file.');
        col.appendChild(text);
        col.className = 'hint helpOverBG';
        row.appendChild(col);
        table.appendChild(row);
    }

    return holderDiv;
}

function pluralise(count, singular, plural) {
    return count != 1 ? plural : singular;
}

function findFirstChild(children, tagName, name) {
    for (var j = 0; j < children.length; j++) {
        var element = children[j];
        if (element.tagName == tagName) {
            if (element.getAttribute('title') == name) {
                return element;
            }
        }
    }
    return undefined;
}

function hilightTest(testDiv, checked) {
    if (checked) {
        replaceClass(testDiv, /\btestUnselected\b/, 'testSelected');
    } else {
        replaceClass(testDiv, /\btestSelected\b/, 'testUnselected');
    }
}

function hiLightByTestFromPopup(testId, checked) {
    hiLightByTest(testId, checked);
    // also check the inline checkbox.
    var inlineCb = document.getElementById('cb-' + testId);
    if (inlineCb == undefined) return;
    inlineCb.checked = checked;
}

// highlights source and summary depending on which tests are checked.
function hiLightByTest(testId, checked) {

    // get all methods hit by the test
    var methods = getMethodsForTest(testId);
    if (methods == undefined) return;
    var testData = getDataForTest(testId);
    if (testData == undefined) return;
    var passed = testData.pass;

    addHitsToMap(methods, methodsToHiLight, checked, passed);
    // now visit all methods on the page, and highlight or unhighlight as needed
    var visitor = function(method) {
        var summTd = document.getElementById('summary-' + method.sl + '-' + method.sc);

        if (methodsToHiLight[method.sl] > 0) {
            addCoverageClass(summTd, selectedLinesTestResult[method.sl]);
        } else {
            removeCoverageClass(summTd, selectedLinesTestResult[method.sl]);            
        }
    };
    visitAllMethods(visitor);

    // hi-light individual src lines.
    var statements = getStatementsForTest(testId);
    if (statements == undefined) return;
    addHitsToMap(statements, linesToHiLight, checked, passed);


    var srcLineVisitor = function(srcEle, lineNumber) {

        if (linesToHiLight[lineNumber] > 0 || methodsToHiLight[lineNumber] > 0) {
            addCoverageClass(srcEle, selectedLinesTestResult[lineNumber]);
        } else {
            removeCoverageClass(srcEle, selectedLinesTestResult[lineNumber]);
        }
        
    }
    visitAllSrcLines(srcLineVisitor);
}

// collects test info for selected tests per line
var LineTestInfo = function(lineNum) {
    this.lineNum = lineNum;
    this.passes = 0;
    this.fails = 0;
}

LineTestInfo.prototype.addResult = function(passed) {
    if (passed) {
        this.passes++;
    } else {
        this.fails++;
    }
}

LineTestInfo.prototype.removeResult = function(passed) {
    if (passed) {
        this.passes--;
    } else {
        this.fails--;
    }
}

LineTestInfo.prototype.isUniqueHit = function() {
    return clover.srcFileLines[this.lineNum].length == 1;
}

// Returns true if all selected tests for the line failed
LineTestInfo.prototype.showFailed = function() {
    return this.passes <= 0 && this.fails > 0;
}

LineTestInfo.prototype.calcCoverageClass = function() {
    if (this.showFailed()) {
        return "coveredByFailedTest";
    }

    if (this.isUniqueHit()) {
        return "coveredByTestUniq";
    }

    return "coveredByTest"

}


function addHitsToMap(hitElements, elementsToHiLight, checked, passed) {

    for(var i = 0; i < hitElements.length; i++) {
        var ele = hitElements[i];
        var currCount = elementsToHiLight[ele.sl];
        currCount = currCount == undefined ? 0 : currCount;
        var increment = checked ? 1 : -1;
        elementsToHiLight[ele.sl] = currCount + increment;

        // get the test info object for the current line and add result
        var info = selectedLinesTestResult[ele.sl] ? selectedLinesTestResult[ele.sl] : new LineTestInfo(ele.sl);
        if (checked) {
            info.addResult(passed);
        } else {
            info.removeResult(passed);
        }
        selectedLinesTestResult[ele.sl] = info;
    }
}

// gets all the methods hit by a given test.
function getMethodsForTest(testId) {
    var testData = getDataForTest(testId);
    if (testData == undefined) return;

    var methods = testData.methods;
    if (methods == undefined) return;
    return methods;
}
// gets all the statements hit by a given test.
function getStatementsForTest(testId) {
    var testData = getDataForTest(testId);
    if (testData == undefined) return;

    var statements = testData.statements;
    if (statements == undefined) return;
    return statements;
}

function getDataForTest(testId) {
    if (testId == undefined) return;

    var testData = clover.testTargets['test_' + testId];
    if (testData == undefined) return;

    return testData;
}

function toggleStats(eleToHide, eleToDisplay) {
    displayEle(document.getElementById(eleToDisplay));
    hideEle(document.getElementById(eleToHide));
}

function displayEle(ele) {
    if (ele == undefined) return;
    ele.style.display = '';
}

function hideEle(ele) {
    if (ele == undefined) return;
    ele.style.display = 'none';
}

function setToggleLabel(ele, expandAll) {
    ele.innerHTML = (expandAll) ? 'Collapse All' : 'Expand All';
}

function swapExpand(ele) {
    replaceClass(ele, /expand/, 'collapse');
}

function swapCollapse(ele) {
    replaceClass(ele, /collapse/, 'expand');
}

function swapExpandImg(ele) {
    replaceImg(ele, /expand/, 'collapse');
}

function swapCollapseImg(ele) {
    replaceImg(ele, /collapse/, 'expand');
}

var coveredByRegExp = /\b(coveredBy.*)\b/; // matches one of the *coveredBy* classes: ~Test, ~TestUniq, ~FailedTest
var srcLineHilight = /\bsrcLineHilight\b/; // matches already hilighted src lines

// adds the appropriate coverage class to the given element
// testInfo must be a LineTestInfo, and is used to calculate the coverage class to use
function addCoverageClass(ele, testInfo) {
    if (testInfo != null && !ele.className.match(srcLineHilight)) { // if line already hilighted, nothing to do
        var coverageClass = testInfo.calcCoverageClass();
        var matchArray = coveredByRegExp.exec(ele.className); 
        if(matchArray && matchArray.length > 0) { // replace the existing coveredBy class
            replaceClass(ele, coveredByRegExp, coverageClass);
        } else { // add a coveredBy class to the existing className
            ele.className = ele.className + ' ' + testInfo.calcCoverageClass();            
        }        
    }
}
// removes the coverageBy class from the existing element
function removeCoverageClass(ele, testInfo) {
    if (testInfo != null && ele.className.match(coveredByRegExp)) { // do nothing if no coveredBy class
        replaceClass(ele, coveredByRegExp, '');
    }
}


function replaceClass(ele, regex, newClass) {
    ele.className = ele.className.replace(regex, newClass);
}
function replaceImg(ele, regex, newClass) {
    ele.src = ele.src.replace(regex, newClass);
}

function methodSortFunction(aa, bb) {
    if (aa.sl == bb.sl) return 0;
    if (aa.sl < bb.sl) return -1;
    return 1;
}

function createTreeMap(json) {

    return new TM.Squarified({
        //Where to inject the Treemap
        rootId: 'infovis',
        offset: 1,
        titleHeight: 14,

        //Add click handlers for
        //zooming the Treemap in and out
        addLeftClickHandler: true,
        addRightClickHandler: true,

        //When hovering a node highlight the nodes
        //between the root node and the hovered node. This
        //is done by adding the 'in-path' CSS class to each node.
        selectPathOnHover: true,

        Color: {
            //Allow coloring
            allow: true,
            //Select a value range for the $color
            //property. Default's to -100 and 100.
            minValue: 0,
            maxValue: 100,
            //Set color range. Default's to reddish and
            //greenish. It takes an array of three
            //integers as R, G and B values.
            maxColorValue: [0, 255, 50],
            minColorValue: [255, 0, 50]
        },

        //Allow tips
        Tips: {
            allow: true,
            //add positioning offsets
            offsetX: 20,
            offsetY: 20,
            //implement the onShow method to
            //add content to the tooltip when a node
            //is hovered
            onShow: function(tip, node, isLeaf, domElement) {
                tip.innerHTML = node.data.title;

            }}
            ,
            // This method is called on each newly created node.
            onCreateElement: function(content, node, isLeaf, elem1, elem2) {
                if (isLeaf) {
                    elem1.innerHTML = "";
                } 
            },

        // Called for each click to a node.
        request: function(nodeId, level, onComplete) {
            var tree = eval(json);
            var subtree = TreeUtil.getSubtree(tree, nodeId);
            TreeUtil.prune(subtree, 1);
            if (level < 3) { // Leaves are level 3
                onComplete.onComplete(nodeId, subtree);
            } else {
                window.location = subtree.data.path;
            }
        }

    });

}

function processTreeMapJson(json) {
    var tm = createTreeMap(json);
    //load JSON data
    tm.loadJSON(json);

}

function processTreeMapDashJson(json) {
    var tm = createTreeMap(json);

    // customize treemap for the dashboard.
    tm.config.titleHeight = 0;
    tm.config.onCreateElement = function(content, node, isLeaf, elem1, elem2) { };
    // Called for each click to a node.
    tm.config.request = function(nodeId, level, onComplete) {
        window.location = "treemap.html";
    };

    //load JSON data
    tm.loadJSON(json);

}