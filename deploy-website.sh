#!/bin/bash

# clone the website branch
echo "************** CLONE ************************"
git clone --depth=1 --branch gh-pages git@github.com:easymock/easymock.git site

pushd site

# delete all none hidden directories (keep .git for instance)
ls -1 | xargs rm -rf

# copy the new site to the branch
cp -R ../website/* .

# to help debugging in case of iissue
echo "************** STATUS************************"
git status

# push the site
echo "************** COMMIT ***********************"
git add .
git commit -m "from master $(git log | head -n 1)"

echo "************** PUSH ************************"
git push origin gh-pages

popd

rm -rf site
