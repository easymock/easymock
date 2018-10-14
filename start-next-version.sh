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

# Create next version in Jira (http://jira.codehaus.org/plugins/servlet/project-config/EASYMOCK/versions)
curl -D- -u ${jira_user}:${jira_password} -X POST --data '{ "description": "EasyMock $version", "name": "$version", "archived": false, "released": false, "project": "EASYMOCK", "projectId": 12103 }' -H "Content-Type:application/json" https://jira.codehaus.org/rest/api/2/version
