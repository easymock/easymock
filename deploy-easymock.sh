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

# make sure the version is passed in parameter
if [ "$1" == "" ]; then
    echo "Version to deploy should be provided"
    exit 1
fi

message="should be an environment variable"
[ -z "$gpg_passphrase" ] && echo "gpg_passphrase $message" && exit 1
[ -z "$bintray_api_key" ] && echo "bintray_api_key $message" && exit 1
[ -z "$bintray_user" ] && echo "bintray_user $message" && exit 1

# make sure the script is launched from the project root directory
if [ "$(dirname $0)" != "." ]; then
    echo "The script should be launched from EasyMock root directory"
    exit 1
fi

version=$1

echo "Start clean"
mvn clean -Pall

echo "Make sure we have a target directory"
test ! -d target && mkdir target

pause

echo "Update Release Notes"
release_notes_page="http://jira.codehaus.org/secure/ReleaseNote.jspa?version=${jira_version_id}&styleName=Text&projectId=12103"
release_notes=$(curl --silent "$release_notes_page")
cp "ReleaseNotes.txt" "target/ReleaseNotes.txt"
echo "$release_notes" | sed -n "/<textarea rows=\"40\" cols=\"120\">/,/<\/textarea>/p" | grep -v "textarea" >> "target/ReleaseNotes.txt"
echo "For details, please see $release_notes_page" >> "target/ReleaseNotes.txt"
release_notes=$(cat "target/ReleaseNotes.txt")
echo "$release_notes"

pause

echo "Update the Maven version"
mvn versions:set -DnewVersion=${version} -Pall

pause

echo "Build and deploy"
mvn -T 8.0C deploy -PfullBuild,deployBuild -Dgpg.passphrase=${gpg_passphrase}

pause

echo "Please close the repository at https://oss.sonatype.org"

pause

echo "Test release"
mvn package -f "easymock-test-deploy/pom.xml"

pause

mvn versions:commit -Pall
git commit -am "Move to version ${version}"
git tag easymock-${version}
git status
git push
git push --tags

pause

echo "Release the version in Jira"
curl -D- -u $jira_user:$jira_password -X PUT --data '{ "released": true }' -H "Content-Type:application/json" https://jira.codehaus.org/rest/api/2/version/${jira_version_id}

pause

echo "Deploy the bundle to Bintray"
date=$(date )
content="{ \"name\": \"$version\", \"desc\": \"$version\", \"released\": \"${date}T00:00:00.000Z\", \"github_use_tag_release_notes\": false, \"vcs_tag\": \"easymock-$version\" }"
curl -v -XPOST -H "Content-Type: application/json" -H "X-GPG-PASSPHRASE: ${gpg_passphrase}" -u${bintray_user}:${bintray_api_key} \
	-d "$content" \
    https://api.bintray.com/packages/easymock/distributions/zzz-easymock-classextension/versions

pause

echo "Close the deployment at Sonatype Nexus UI"
echo
echo "Goto https://oss.sonatype.org/index.html#stagingRepositories"
echo "Release the repository. It will be synced with Maven Central Repository"

pause

echo "Update Javadoc"
git rm -rf website/api
cp -r easymock/target/apidocs website/api

pause

echo "Update the version on the website"
sed -i '' "s/latest_version: .*/latest_version: $version/" 'website/_config.yml'

echo "Commit the new website"
git add website
git commit -m "Upgrade website to version $version"

echo "Update website"
./deploy-website.sh

pause

echo "Send new release mail"
mail="$(cat mail.txt)"
mail="${mail//@version@/$version}"
mail="${mail/@release_notes@/$release_notes}"
echo "$mail" > target/mail.txt
echo "Please send a mail to easymock@yahoogroups.com using the content of target/mail.txt"

pause

echo
echo "Job done!"
echo
