#!/bin/bash
set -e

# Launched from Jenkins to move documentation at the right place during a deployment
# Usage: prepage-website.sh 2.1 - Launched from the easymock source root directory

if [ "$#" = "0" ]; then
  echo "Usage: $0 version"; exit 1
fi

echo $PWD

VERSION=$1
VERSION_=`echo $VERSION | sed -r 's/\./_/g'`

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
