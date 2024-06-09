#!/bin/bash

set -e

# get the version from the ci build
JAVA_VERSIONS=$(grep "java:" .github/workflows/ci.yml)
JAVA_VERSIONS=${JAVA_VERSIONS#*\[} # remove "java: ["
JAVA_VERSIONS=${JAVA_VERSIONS//\'/} # remove all single quotes
JAVA_VERSIONS=${JAVA_VERSIONS//]/}  # remove closing bracket
JAVA_VERSIONS=${JAVA_VERSIONS//,/}  # remove commas

for i in $JAVA_VERSIONS
do
  echo "Java $i"
  mvn clean install -PfullBuild,all-no-android
done
