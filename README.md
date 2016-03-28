EasyMock
========

[![Join the chat at https://gitter.im/easymock/easymock](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/easymock/easymock?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![Download latest version](https://api.bintray.com/packages/easymock/distributions/easymock/images/download.svg) ](https://bintray.com/easymock/distributions/easymock/_latestVersion)

EasyMock is a Java library that provides an easy way to use Mock Objects in unit testing.

You can find the website and user documentation at http://easymock.org.

Developer information
=====================

Travis status
-------------
[![Build Status](https://travis-ci.org/easymock/easymock.svg?branch=master)](https://travis-ci.org/easymock/easymock)

Environment setup
-----------------

I'm using:
- IntelliJ 15 Ultimate (thanks to JetBrains for the license)
- Maven 3.3.9

You can also use Eclipse. I tried 
- Eclipse 4.5.0 (Mars)
- Say yes to all the plugins an m2 connectors to install

To configure your local workspace:
- Import the Maven parent project to Eclipse or IntelliJ
- Import the Eclipse formatting file `EasyMock-formatter.xml` (usable in Eclipse or IntelliJ)

To build EasyMock with Maven
----------------------------

There are three different levels of build.

### Build without any active profile

It is a basic compilation of the application.

`mvn install`

### Full build

This build will check code coverage using Jacoco, run findbugs and
validate that the license headers are correctly set.

`mvn install -PfullBuild`

### Deploy build

This is the build to launch to deploy to the surefire repository. It assembles the application and add
the gpg checksum. You will usually launch it on top of the full build.

The command line will ask you to give the passphrase for the gpg private key.

`mvn install -PdeployBuild`

To compile EasyMock in Eclipse
-----------------------------
- Install m2e
- Import the EasyMock Maven parent project to your Eclipse workspace

To compile EasyMock in IntelliJ
-----------------------------
- Import the EasyMock Maven parent project as an New IntelliJ project

To update the versions
----------------------
- `mvn versions:set -DnewVersion=X.Y -Pandroid,bench`
- `mvn versions:commit -Pandroid,bench` if everything is ok, `mvn versions:revert -Pandroid,bench` otherwise

Configure to deploy to the Sonatype maven repository
----------------------------------------------------
- You will first need to add something like this to your settings.xml
```xml
<servers>
  <server>
    <id>sonatype-nexus-snapshots</id>
    <username>sonatypeuser</username>
    <password>sonatypepassword</password>
  </server>
  <server>
    <id>sonatype-nexus-staging</id>
    <username>sonatypeuser</username>
    <password>sonatypepassword</password>
  </server>
</servers>
```
- Then follow the instructions from the site below to create your key to sign the deployed items

http://www.sonatype.com/people/2010/01/how-to-generate-pgp-signatures-with-maven/

To build the maven site (with findbugs, checkstyle, jdepends and JavaNCSS reports)
--------------------------------------------------------------------------------------
- You will to give enough memory to maven with 'set MAVEN_OPTS=-Xmx512m' (or setting it as environment variable)
- Then type `mvn site`

To check dependencies and plugins versions
--------------------------------------------------------------------------------------
`mvn versions:display-dependency-updates versions:display-plugin-updates -Pall`

To download the sources associated to our dependencies
--------------------------------------------------------------------------------------
`mvn dependency:resolve -Dclassifier=sources`

To update the license
--------------------------------------------------------------------------------------
`mvn validate license:format -Pall`

To run Sonar
--------------------------------------------------------------------------------------
`mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent test sonar:sonar`

Android
--------------------------------------------------------------------------------------
- Install the Android SDK
- Configure a device (real or simulated)
- Add an `ANDROID_HOME` to target the Android SDK
- Add `$ANDROID_HOME/platform-tools` to your path 
- Activate the debug mode if it's a real device
- `mvn install -Pandroid`

To bundle EasyMock and deploy
--------------------------------------------------------------------------------------
- Add a little speech on the features in "ReleaseNotes.md"
- Set the jira_user, jira_password, gpg_passphrase, sf_user, sf_api_key as environment variables
- Launch ./deploy-easymock.sh version

- Close the deployment at Sonatype Nexus UI (https://oss.sonatype.org/index.html#stagingRepositories)
  More details on the deployment rules here: https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide
- Add the staging repository url found in Nexus to easymock-test-deploy
```xml
<repositories>
  <repository>
    <id>sonatype-nexus-staging</id>
    <name>Nexus Release Repository</name>
    <url>https://oss.sonatype.org/content/repositories/orgeasymock-213/</url>
    <releases>
      <enabled>true</enabled>
    </releases>
    <snapshots>
      <enabled>false</enabled>
    </snapshots>
  </repository>
</repositories>
```
- Release the repository. It will be synced with Maven Central Repository
- Announce to gitter, tweet and blog ;-)

Deploy the website
--------------------------------------------------------------------------------------
- In local:
  - Go to the EasyMock root directory
  - Make sure the website directory is clean
  - `./deploy-website.sh`

Start next version
--------------------------------------------------------------------------------------
In local:
```bash
mvn versions:set -DnewVersion=X.Z-SNAPSHOT -Pall
mvn versions:commit -Pall
```
- Create next version in Jira (http://jira.codehaus.org/plugins/servlet/project-config/EASYMOCK/versions)
