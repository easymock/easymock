#!/bin/bash

# has to be executed on shell.sf.net with appropriate rights

cd /home/groups/e/ea/easymock \
&& rm -rf htdocs2 \
&& svn export http://easymock.svn.sourceforge.net/svnroot/easymock/trunk/website htdocs2  \
&& chmod -R g+w htdocs2 \
&& mv htdocs/maven htdocs2/maven \
&& rm -rf htdocs \
&& mv htdocs2 htdocs
