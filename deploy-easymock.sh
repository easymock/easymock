#!/bin/bash

# This script expects:
# - the version to be deployed as the first parameter
# - jira_user / jira_password to be available environment variables
# - gpg_passphrase to be an environment variable
# - bintray_user to be an environment variable
# - bintray_api_key to be an environment variable

# to exit in case of error
set -e
# to see what's going on
set -v

function pause {
    echo
    read -p "Press [enter]  to continue"
}

# Make sure the script is launched from the project root directory
if [ "$(dirname $0)" != "." ]; then
    echo "The script should be launched from EasyMock root directory"
    exit 1
fi

# Get the version to deliver
version=$(sed -n 's/.*>\(.*\)-SNAPSHOT<.*/\1/p' pom.xml | head -1)

[ -z "$version" ] && echo "Only snapshots can be delivered" && exit 1

# Get we have the environment variable we need
message="should be an environment variable"
[ -z "$gpg_passphrase" ] && echo "gpg_passphrase $message" && exit 1
#[ -z "$bintray_api_key" ] && echo "bintray_api_key $message" && exit 1
#[ -z "$bintray_user" ] && echo "bintray_user $message" && exit 1

# Update the version
echo
echo "************** Delivering version $version ****************"
echo

echo "Start clean"
mvn clean -Pall

echo "Make sure we have a target directory"
test ! -d target && mkdir target

echo "Update the Maven version"
mvn versions:set -DnewVersion=${version} -Pall

echo "Build and deploy"
mvn -T 8.0C deploy -PfullBuild,deployBuild,all

echo "Please publish on bintray"
open "https://bintray.com/easymock/maven/easymock/$version"

pause

echo "Commit everything"
mvn versions:commit -Pall
git commit -am "Move to version ${version}"
git tag easymock-${version}
git status
git push
git push --tags

echo "Deploy the bundle to Bintray"
date=$(date )
content="{ \"name\": \"$version\", \"desc\": \"$version\", \"released\": \"${date}T00:00:00.000Z\", \"github_use_tag_release_notes\": true, \"vcs_tag\": \"easymock-$version\" }"
curl -v -XPOST -H "Content-Type: application/json" -H "X-GPG-PASSPHRASE: ${gpg_passphrase}" -u${bintray_user}:${bintray_api_key} \
    -d "$content" \
    https://api.bintray.com/packages/easymock/distributions/easymock/versions

pause

echo
echo "Job done!"
echo
