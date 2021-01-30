# EasyMock

[![Join the chat at https://gitter.im/easymock/easymock](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/easymock/easymock?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![Download latest version](https://api.bintray.com/packages/easymock/distributions/easymock/images/download.svg) ](https://bintray.com/easymock/distributions/easymock/_latestVersion)

EasyMock is a Java library that provides an easy way to use Mock Objects in unit testing.

You can find the website and user documentation at http://easymock.org.

# Developer information

## Build status
[![Build Status](https://travis-ci.org/easymock/easymock.svg?branch=master)](https://travis-ci.org/easymock/easymock)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.easymock/easymock/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.easymock/easymock)

## Environment setup

I'm using:
- IntelliJ 2020.3 Ultimate (thanks to JetBrains for the license)
- Maven 3.6.3

You can also use Eclipse. I tried
- Eclipse 2020.12 (but there was a weird compilation issue with ASM)

To configure your local workspace:
- Import the Maven parent project to Eclipse or IntelliJ
- Import the Eclipse formatting file `EasyMock-formatter.xml` (usable in Eclipse or IntelliJ)

## To build EasyMock with Maven

There are three different levels of build.

### Build without any active profile

It is a basic compilation of the application.

`mvn install`

### Full build

This build will check code coverage using Jacoco, run spotbugs and
validate that the license headers are correctly set.

`mvn install -PfullBuild`

### Deploy build

This is the build to launch to deploy to the surefire repository. It assembles the application and add
the gpg checksum. You will usually launch it on top of the full build.

The command line will ask you to give the passphrase for the gpg private key.

`mvn install -PdeployBuild`

## To compile EasyMock in Eclipse

- Install m2e
- Import the EasyMock Maven parent project to your Eclipse workspace

## To compile EasyMock in IntelliJ

- Import the EasyMock Maven parent project as an New IntelliJ project

## To update the versions

- `mvn versions:set -DnewVersion=X.Y -Pall`
- `mvn versions:commit -Pall` if everything is ok, `mvn versions:revert -Pall` otherwise

## Configure to deploy to the Bintray maven repository

- You will first need to add something like this to your settings.xml
```xml
<server>
  <id>bintray</id>
  <username>user</username>
  <password>password</password>
</server>
```
- Then follow the instructions from the site below to create your key to sign the deployed items

http://www.sonatype.com/people/2010/01/how-to-generate-pgp-signatures-with-maven/

## To build the maven site (with spotbugs, checkstyle, jdepends and JavaNCSS reports)

- You will to give enough memory to maven with `export MAVEN_OPTS=-Xmx512m` (or setting it as environment variable)
- Then type `mvn site`

## To check dependencies and plugins versions

`mvn versions:display-dependency-updates versions:display-plugin-updates -Pall`

## To download the sources associated to our dependencies

`mvn dependency:resolve -Dclassifier=sources`

## To update the license

`mvn validate license:format -Pall`

## To run Sonar

`mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent test sonar:sonar`

## Android

- Install the Android SDK
- Configure a device (real or simulated)
- Add an `ANDROID_HOME` to target the Android SDK
- Possibly put these in your path: `$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools/bin:$ANDROID_HOME/emulator`
- To launch an emulator from command line
  - To list the configured device: `emulator -list-avds`
  - To launch: `emulator -avd Nexus_5X_API_28`
- Activate the debug mode if it's a real device
- `mvn install -Pandroid`

## To bundle EasyMock and deploy

- Make sure the poms are on the snapshot of the version you want to deploy
- Launch and Android emulator or connect an Android phone
- Make sure `jq` is installed. If not, try `brew install jq` or `choco install jq`
- If gpg gives an `Inappropriate ioctl for device` error, enter this in your shell: `export GPG_TTY=$(tty)`
- Add a little speech on the features in "ReleaseNotes.md" (remove the Change Log part, which will be regenerated automatically)
- Set the github_user, github_password, gpg_passphrase, bintray_user, bintray_api_key as environment variables
- Launch ./deploy-easymock.sh version
- During the deployment, you will be asked to do different things. Do them
- Announce to gitter, tweet and blog ;-)

In case of a failure during the build before the deployment, do `mvn versions:commit -Pall` and start over.

## Deploy the website

- In local:
  - Go to the EasyMock root directory
  - Make sure the website directory is clean
  - `./deploy-website.sh`

## Start next version

In local:
```bash
mvn versions:set -DnewVersion=X.Z-SNAPSHOT -Pall
mvn versions:commit -Pall
```
- Create next version in Jira (http://jira.codehaus.org/plugins/servlet/project-config/EASYMOCK/versions)
