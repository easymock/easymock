#!/bin/bash

# This script expects:
# - jira_user / jira_password to be available environment variables
# - the next version to be received as the first parameter

if [ "$1" == "" ]; then
    echo "Next version should be provided"
    exit 1
fi

set -e

version=$1

# Start the new version
mvn versions:set -DnewVersion=${version}-SNAPSHOT -Pall
mvn versions:commit -Pall

# Commit
git commit -am "Start version $version"
git push
