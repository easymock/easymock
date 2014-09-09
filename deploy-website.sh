#!/bin/bash

git clone git@github.com:easymock/easymock.git site
git checkout gh-pages-test

rm -rf site/* # sauf .git

copy -R website/* site

cd site
git add .
git commit -m "from master `git log | head -n 1`"
echo git push origin gh-pages
cd ..

echo git reset --hard HEAD
