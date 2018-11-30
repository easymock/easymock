#!/bin/bash

if [ "$#" -ne 2 ]; then
    echo "Usage: ./generate-changelog.sh user/repo <milestone>"
    exit 1
fi

repository=$1
milestone=$2

IFS=$'\n'
echo "Change log"
echo "----------"

for i in $(curl -s "https://api.github.com/repos/${repository}/issues?milestone=${milestone}&state=closed" | jq -c '.[] | [.html_url, .number, .title]'); do
    echo $i | sed 's/\["\(.*\)",\(.*\),\"\(.*\)\"\]/* \3 ([#\2](\1))/' | sed 's/\\"/"/g'
done
