#!/bin/bash

# This script expects:
# - github_user / github_password to be available environment variables
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

function incrementVersionLastElement {
    IN=$1

    VER1=`echo $IN | awk -F\. 'BEGIN{i=2}{res=$1; while(i<NF){res=res"."$i; i++}print res}'`
    VER2=`echo $IN | awk -F\. '{print $NF}'`
    VER2=`expr $VER2 + 1`
    OUT="$VER1.$VER2"

    echo $OUT
}

# Make sure the script is launched from the project root directory
if [ "$(dirname $0)" != "." ]; then
    echo "The script should be launched from EasyMock root directory"
    exit 1
fi

# Get the version to deliver
version=$(sed -n 's/.*>\(.*\)-SNAPSHOT<.*/\1/p' pom.xml | head -1)
tag=easymock-${version}

[ -z "$version" ] && echo "Only snapshots can be delivered" && exit 1

# Get we have the environment variable we need
message="should be an environment variable"
[ -z "$gpg_passphrase" ] && echo "gpg_passphrase $message" && exit 1
[ -z "$github_user" ] && echo "github_user $message" && exit 1
[ -z "$github_password" ] && echo "github_password $message" && exit 1
[ -z "$bintray_api_key" ] && echo "bintray_api_key $message" && exit 1
[ -z "$bintray_user" ] && echo "bintray_user $message" && exit 1

# Seems to be required to make gpg happy
export GPG_TTY=$(tty)

# Update the version
echo
echo "************** Delivering version $version ****************"
echo

pause

echo "Generate the changelog"
milestone=$(curl -s -u "${github_user}:${github_password}" "https://api.github.com/repos/easymock/easymock/milestones" | jq ".[] | select(.title==\"$version\") | .number")
if [ $(curl -s -u "${github_user}:${github_password}" -H "Accept: application/vnd.github.v3+json" "https://api.github.com/repos/easymock/easymock/issues?milestone=$milestone&state=open" | wc -l) != "3" ]; then
    echo "There are unclosed issues on milestone $version. Please fix them or moved them to a later release"
    exit 1
fi

./generate-changelog.sh easymock/easymock ${milestone} ${github_user} ${github_password} >> ReleaseNotes.md

echo "Check the release notes"
pause

echo "Start clean"
mvn clean -Pall

echo "Make sure we have a target directory"
test ! -d target && mkdir target

echo "Update the Maven version"
mvn versions:set -DnewVersion=${version} -Pall

echo "Build"
mvn clean install -PfullBuild,deployBuild,all

echo "Deploy"
mvn deploy -PfullBuild,deployBuild,all -DskipTests
pause

echo "Commit everything"
mvn versions:commit -Pall
git commit -am "Move to version ${version}"
git tag $tag
git status
git push
git push --tags

pause

# currently not working because of the description that is multiline. Probably need to replace with \n
#echo "Create the github release"
#description="$(cat ReleaseNotes.md)"
#content="{\"tag_name\": \"$tag\", \"target_commitish\": \"master\", \"name\": \"$tag\", \"body\": \"$description\", \"draft\": false, \"prerelease\": false }"
#curl -v -u "${github_user}:${github_password}" \
#  -XPOST -H "Accept: application/vnd.github.v3+json" \
#  -d "$content" \
#  "https://api.github.com/repos/easymock/easymock/releases"

# Do the github release note
echo "Please add the release notes in github (Draft New Release)"
echo "\tTag version: easymock-${version}"
echo "\tRelease title: ${version}"
echo "\tDescription: Content of ReleaseNotes.md"
echo "\tAttach core/target/easymock-${version}-bundle.zip"
echo "\tPublish release"
open "https://github.com/easymock/easymock/releases"
pause

# Login to bintray
echo "Please login on bintray"
open "https://bintray.com/easymock"
pause

# Create the distribution in bintray
date=$(date "+%Y-%m-%d")
content="{ \"name\": \"$version\", \"desc\": \"$version\", \"released\": \"${date}T00:00:00.000Z\", \"github_use_tag_release_notes\": true, \"vcs_tag\": \"easymock-${version}\" }"
curl -H "Content-Type: application/json" -u$bintray_user:$bintray_api_key \
  -d "$content" https://api.bintray.com/packages/easymock/distributions/easymock/versions

curl -v -H "X-GPG-PASSPHRASE: $gpg_passphrase" -u$bintray_user:$bintray_api_key -T "core/target/easymock-${version}-bundle.zip" https://api.bintray.com/content/easymock/distributions/easymock/${version}/easymock-${version}-bundle.zip?publish=1

echo "Flag the bundle as 'Show in download list' in bintray"
open "https://bintray.com/easymock/distributions/easymock/${version}#files"
pause

echo "Close the milestone in GitHub and create the new one"
open "https://github.com/easymock/easymock/milestones"
pause

echo "Sync to Maven central"
open "https://bintray.com/easymock/maven/easymock/${version}#central"
pause

echo "Update Javadoc"
git rm -rf website/api
cp -r core/target/apidocs website/api
pause

echo "Update the version on the website"
sed -i '' "s/latest_version: .*/latest_version: $version/" 'website/_config.yml'

echo "Commit the new website"
git add website
git commit -m "Upgrade website to version $version"

echo "Update website"
./deploy-website.sh

echo "Start new version"
nextVersion=$(incrementVersionLastElement $version)-SNAPSHOT
mvn versions:set -DnewVersion=${nextVersion} -Pall
mvn versions:commit
git commit -am "Starting to develop version ${nextVersion}"

echo
echo "Job done!"
echo
