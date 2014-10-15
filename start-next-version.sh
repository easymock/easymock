#!/bin/bash

if [ "$1" == "" ]
    echo "Next version should be provided"
    exit 1
end

set -e

version=$1

# Start the new version
mvn versions:set -DnewVersion=${version}-SNAPSHOT -Pall
mvn versions:commit -Pall

# Commit
git commit -am "Start version $version"
git push

# Create next version in Jira (http://jira.codehaus.org/plugins/servlet/project-config/EASYMOCK/versions)
curl -D- -u $user:$password -X POST --data '{ "description": "EasyMock $version", "name": "$version", "archived": false, "released": false, "project": "EASYMOCK", "projectId": 12103 }' -H "Content-Type:application/json" https://jira.codehaus.org/rest/api/2/version
