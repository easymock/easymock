#!/bin/bash

# to exit in case of error
set -e

# make sure the script is launched from the project root directory
if [ "$(dirname $0)" != "." ]; then
    echo "The script should be launched from EasyMock root directory"
    exit 1
fi

# clone the website branch
echo "************** CLONE ************************"
git clone --depth=1 --branch gh-pages git@github.com:easymock/easymock.git site

pushd site

# delete all none hidden directories (keep .git for instance)
ls -1 | xargs rm -rf

# copy the new site to the branch
cp -R ../website/* .

# to help debugging in case of issue
echo "************** STATUS************************"
git status

# push the site
echo "************** COMMIT ***********************"
git add --ignore-removal .
git commit -m "from master $(git log | head -n 1)"

echo "************** PUSH ************************"
git push origin gh-pages

popd

rm -rf site
