#!/bin/bash

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

if [ -z "$github_token" ]; then
    echo "github_token environment variable must be set"
    exit 1
fi

function pause {
    echo
    read -p "Press [enter]  to continue"
}

function incrementVersionLastElement {
    IN=$1

    MAJOR=$(echo $IN | cut -d'.' -f 1)
    MINOR=$(echo $IN | cut -d'.' -f 2)
    PATCH=$(echo $IN | cut -d'.' -f 3)

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
version=$(sed -n 's/^-Drevision=\(.*\)-SNAPSHOT$/\1/p' .mvn/maven.config | head -1)
nextVersion=$(incrementVersionLastElement $version)-SNAPSHOT
tag=easymock-${version}

[ -z "$version" ] && echo "Only snapshots can be delivered" && exit 1

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
sed -i '' "s/^-Drevision=.*/-Drevision=${version}/" .mvn/maven.config

echo "Build"
mvn clean install -PfullBuild,deployBuild,all-no-android

echo "Deploy"
mvn deploy -PfullBuild,deployBuild,all-no-android -DskipTests

echo "Deployment done, please validate the staging repository https://central.sonatype.com/publishing"
pause

echo "Commit everything"
git commit -am "Move to version ${version}"
git tag $tag
git status
git push
git push --tags

pause

echo "Create the github draft release"
description=$(jq -Rs . < ReleaseNotes.md)
content="{\"tag_name\": \"$tag\", \"target_commitish\": \"master\", \"name\": \"$version\", \"body\": $description, \"draft\": true, \"prerelease\": false }"
release_response=$(curl -v -H "Authorization: token ${github_token}" \
  -XPOST -H "Accept: application/vnd.github.v3+json" \
  -d "$content" \
  "https://api.github.com/repos/easymock/easymock/releases")

release_id=$(echo "$release_response" | jq ".id")

curl -v -H "Authorization: token ${github_token}" \
  -XPOST \
  -H "Accept: application/vnd.github.v3+json" \
  -H "Content-Type: application/zip" \
  --data-binary "@core/target/easymock-${version}-bundle.zip" \
  "https://uploads.github.com/repos/easymock/easymock/releases/${release_id}/assets?name=easymock-${version}-bundle.zip"

echo "Check the release"
open "https://github.com/easymock/easymock/releases#release-easymock-${version}"
pause

echo "Publish the release"
curl -v -H "Authorization: token ${github_token}" \
  -XPATCH \
  -H "Accept: application/vnd.github.v3+json" \
  -d '{"draft": false}' \
  "https://api.github.com/repos/easymock/easymock/releases/${release_id}"

echo "Close the milestone in GitHub and create the new one"
curl -v -H "Authorization: token ${github_token}" \
  -XPATCH \
  -H "Accept: application/vnd.github.v3+json" \
  -d '{"state": "closed"}' \
  "https://api.github.com/repos/easymock/easymock/milestones/${milestone}"

curl -v -H "Authorization: token ${github_token}" \
  -XPOST \
  -H "Accept: application/vnd.github.v3+json" \
  -d "{\"title\": \"${nextVersion%%-SNAPSHOT}\"}" \
  "https://api.github.com/repos/easymock/easymock/milestones"
open "https://api.github.com/repos/easymock/easymock/milestones"
pause

echo "Update Javadoc"
git rm -rf website/api
cp -r core/target/reports/apidocs website/api
pause

echo "Update the version on the website"
sed -i '' "s/latest_version: .*/latest_version: $version/" 'website/_config.yml'

echo "Commit the new website"
git add website
git commit -m "Upgrade website to version $version"

echo "Update website"
./deploy-website.sh

echo "Start new version"
sed -i '' "s/^-Drevision=.*/-Drevision=${nextVersion}/" .mvn/maven.config
git commit -am "Starting to develop version ${nextVersion}"

echo
echo "Job done!"
echo
