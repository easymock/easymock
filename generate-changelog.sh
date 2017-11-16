#!/bin/bash

if [ "$#" -ne 4 ]; then
    echo "Usage: ./generate-changelog.sh user/repo <milestone> <username> <password>"
    exit 1
fi

repository=$1
milestone=$2
user=$3
password=$4

IFS=$'\n'
echo "Change log"
echo "----------"

for i in $(curl -s -u "${user}:${password}" -H "Accept: application/vnd.github.v3+json" "https://api.github.com/repos/${repository}/issues?milestone=${milestone}&state=closed" | jq -c '.[] | [.html_url, .number, .title]'); do
    echo $i | sed 's/\["\(.*\)",\(.*\),\"\(.*\)\"\]/* \3 ([#\2](\1))/' | sed 's/\\"/"/g'
done
