#!/bin/bash

# Has to be executed on shell.sourceforge.net with appropriate rights!
# To create a shell replace USERNAME with your username in the following snippet:
# ssh -t USERNAME,easymock@shell.sourceforge.net create

cd /home/project-web/easymock \
&& rm -rf htdocs-bak \
&& git clone https://github.com/easymock/easymock.git htdocs2  \
&& chmod -R g+w htdocs2/website \
&& mv htdocs htdocs-bak \
&& mv htdocs2/website htdocs \
&& rm -rf htdocs2

