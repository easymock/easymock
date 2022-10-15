#!/bin/bash

# This script expects:
# - gpg_passphrase to be an environment variable

# to exit in case of error
set -e
# to see what's going on
set -v

increment=$1

case $increment in
  'major')
    ;;
  'minor')
    ;;
  'patch')
    ;;
  *)
    echo "You need to tell what number should be incremented: major, minor or patch"
    exit 1
    ;;
esac

function pause {
    echo
    read -p "Press [enter]  to continue"
}

function incrementVersionLastElement {
    IN=$1

    MAJOR=$(echo $IN | cut -d'.' -f 1)
    MINOR=$(echo $IN | cut -d'.' -f 2)
    PATCH=$(echo $IN | cut -d'.' -f 2)

    case $increment in
      'major')
        MAJOR=$((MAJOR+1))
        ;;
      'minor')
        MINOR=$((MINOR+1))
        ;;
      'patch')
        PATCH=$((PATCH+1))
        ;;
      *)
        STATEMENTS
        ;;
    esac

    OUT="$MAJOR.$MINOR.$PATCH"

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

# Seems to be required to make gpg happy
export GPG_TTY=$(tty)

# Update the version
echo
echo "************** Delivering version $version ****************"
echo

pause

echo "Generate the changelog"
milestone=$(curl -s "https://api.github.com/repos/easymock/easymock/milestones" | jq ".[] | select(.title==\"$version\") | .number")
if [ $(curl -s "https://api.github.com/repos/easymock/easymock/issues?milestone=${milestone}&state=open" | wc -l) != "3" ]; then
    echo "There are unclosed issues on milestone $version. Please fix them or moved them to a later release"
    exit 1
fi

./generate-changelog.sh easymock/easymock ${milestone} >> ReleaseNotes.md

echo "Check the release notes"
pause

echo "Start clean"
mvn clean -Pall

echo "Make sure we have a target directory"
test ! -d target && mkdir target

echo "Update the Maven version"
mvn versions:set -DnewVersion=${version} -Pall

echo "Build"
mvn clean install -PfullBuild,deployBuild,all-no-android

echo "Deploy"
mvn deploy -PfullBuild,deployBuild,all-no-android -DskipTests

echo "Deployment done, please validate the staging repository https://oss.sonatype.org/#stagingRepositories"
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

echo "Close the milestone in GitHub and create the new one"
open "https://github.com/easymock/easymock/milestones"
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
