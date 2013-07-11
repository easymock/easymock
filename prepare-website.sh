#!/bin/bash
set -e

# Launched from Jenkins to move documentation at the right place during a deployment
# Usage: prepage-website.sh 2.1 "This version contains something cool" - Launched from the easymock source root directory

if [ "$#" = "0" ]; then
  echo "Usage: $0 version"; exit 1
fi

echo $PWD

VERSION=$1
VERSION_=`echo $VERSION | sed -r 's/\./_/g'`
DATE=`date +%F`
ANNOUNCEMENT=$2

checkExist() {
  if [ ! -e "$*" ]
  then  
    echo "$* not found"
    echo
    exit 1
  fi
}

EASYMOCK_BUNDLE="easymock/target/easymock-${VERSION}-bundle.zip"
EASYMOCK_JAVADOC="easymock/target/apidocs"

EASYMOCKCS_BUNDLE="easymock-classextension/target/easymockclassextension-${VERSION}-bundle.zip"
EASYMOCKCS_JAVADOC="easymock-classextension/target/apidocs"

echo
echo  --- Checking necessary artefacts ---
echo
checkExist "$EASYMOCK_BUNDLE"
checkExist "$EASYMOCKCS_BUNDLE"
checkExist "$EASYMOCK_JAVADOC"
checkExist "$EASYMOCK_JAVADOC"

echo
echo --- Copy the documentation from EasyMock to the website ---
echo
unzip "$EASYMOCK_BUNDLE" "easymock-$VERSION/Documentation*.html" -d easymock/target
mv "easymock/target/easymock-$VERSION/Documentation.html" "website/EasyMock${VERSION_}_Documentation.html"
mv "easymock/target/easymock-$VERSION/Documentation_fr.html" "website/EasyMock${VERSION_}_Documentation_fr.html"

echo
echo --- Copy the documentation from EasyMock ClassExtension to the website ---
echo
unzip "$EASYMOCKCS_BUNDLE" "easymockclassextension-$VERSION/Documentation*.html" -d "easymock-classextension/target"
mv "easymock-classextension/target/easymockclassextension-$VERSION/Documentation.html" "website/EasyMock${VERSION_}_ClassExtension_Documentation.html"
mv "easymock-classextension/target/easymockclassextension-$VERSION/Documentation_fr.html" "website/EasyMock${VERSION_}_ClassExtension_Documentation_fr.html"

echo
echo --- Retrieve the javadoc ---
echo
mv "$EASYMOCK_JAVADOC" "website/api/easymock/${VERSION}"
mv "$EASYMOCKCS_JAVADOC" "website/api/easymockclassextension/${VERSION}"

echo
echo --- Add Downloads ---
echo
sed -i -e "s/<!--StartE-->\(.\+\)<strong>\(.\+\)<\/strong>\(.\+\)<!--EndE-->/<!--StartE--><li><a href=\"http:\/\/sourceforge.net\/projects\/easymock\/files\/EasyMock\/${VERSION}\/easymock-${VERSION}.zip\/download\"><strong>EasyMock ${VERSION} (${DATE})<\/strong><\/a><\/li><!--EndE-->\n  \1\2\3/" "website/Downloads.html"
sed -i -e "s/<!--StartECS-->\(.\+\)<strong>\(.\+\)<\/strong>\(.\+\)<!--EndECS-->/<!--StartECS--><li><a href=\"http:\/\/sourceforge.net\/projects\/easymock\/files\/EasyMock Class Extension\/${VERSION}\/easymockclassextension-${VERSION}.zip\/download\"><strong>EasyMock ${VERSION} Class Extension (${DATE})<\/strong><\/a><\/li><!--EndECS-->\n  \1\2\3/" "website/Downloads.html"

echo
echo --- Add Documentation ---
echo
sed -i -e "s/<!--StartE-->\(.\+\)<strong>\(.\+\)<\/strong>\(.\+\)<!--EndE-->/<!--StartE--><strong><a href=\"EasyMock${VERSION_}_Documentation.html\">EasyMock ${VERSION}<\/a> (<a href=\"EasyMock${VERSION_}_Documentation_fr.html\">French<\/a>) (<a href=\"api\/easymock\/${VERSION}\/index.html\">API<\/a>)<\/strong><\/li><!--EndE-->\n  \1\2\3/" "website/Documentation.html"
sed -i -e "s/<!--StartECS-->\(.\+\)<strong>\(.\+\)<\/strong>\(.\+\)<!--EndECS-->/<!--StartECS--><strong><a href=\"EasyMock${VERSION_}_ClassExtension_Documentation.html\">EasyMock ${VERSION} Class Extension<\/a> (<a href=\"EasyMock${VERSION_}_ClassExtension_Documentation_fr.html\">French<\/a>) (<a href=\"api\/easymockclassextension\/${VERSION}\/index.html\">API<\/a>)<\/strong><\/li><!--EndECS-->\n  \1\2\3/" "website/Documentation.html"

echo
echo --- Add Announcements ---
echo
sed -i -e "s/<!--StartE-->\(.\+\)<strong>\(.\+\)<\/strong>\(.\+\)<\/li><li>\(.\+\)<strong>\(.\+\)<\/strong>\(.\+\)<!--EndE-->/<!--StartE--><li>${DATE}: <strong>EasyMock ${VERSION} is available<\/strong>. ${ANNOUNCEMENT}.<\/li><li>${DATE}: <strong>EasyMock ${VERSION} Class Extension is available<\/strong>. Just following EasyMock versionning. Still deprecated.<\/li><!--EndE-->\n  \1\2\3<\/li>\n  <li>\4\5\6/" "website/index.html"
