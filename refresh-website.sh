#!/bin/bash

# Has to be executed on shell.sourceforge.net with appropriate rights!
# To create a shell replace USERNAME with your username in the following snippet:
# ssh -t USERNAME,easymock@shell.sourceforge.net create

cd /home/groups/e/ea/easymock \
&& rm -rf htdocs2 \
&& svn export http://easymock.svn.sourceforge.net/svnroot/easymock/trunk/website htdocs2  \
&& chmod -R g+w htdocs2 \
&& rm -rf htdocs \
&& mv htdocs2 htdocs
