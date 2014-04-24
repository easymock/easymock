EasyMock
========

EasyMock is a Java library that provides an easy way to use Mock Objects in unit testing.

You can find the website and user documentation at http://easymock.org.

Developer information
=====================

Environment setup
-----------------

I'm using:
- Eclipse 4.3.2 (Kepler Service Release 2)
- Maven 3.2.1
- m2e with the following connectors:
  - buildhelper (for build-helper-maven-plugin)
- IntelliJ 13.1.2 (instead of Eclipse)

To configure your local workspace:
- Import the Maven parent project to Eclipse or IntelliJ
- Import the Eclipse formatting file `EasyMock-formatter.xml`

To build EasyMock with Maven
----------------------------

There are three different levels of build.

1. Build without any active profile

It is a basic compilation of the application.

- "mvn install"

2. Full build

This build will check code coverage with Clover (must be 100%) and
validate that the license headers are correctly set.

You need to add a section to your settings.xml in order to make it works

    <profile>
      <id>easymock</id>
      <properties>
        <maven.clover.licenseLocation>${user.home}/clover.license</maven.clover.licenseLocation>
      </properties>
    </profile

To tell clover where to find its license

Then launch with

- "mvn install -PfullBuild"

3. Deploy build

This is the build to launch to deploy to the surefire repository. It assembles the application and add
the gpg checksum. You will usually launch it on top of the full build.

The command line will ask you to give the passphrase for the gpg private key.

- "mvn install -PdeployBuild"

4. Continuous integration

A continuous integration is provided by Cloudbees. You will find the following builds:
- https://henri.ci.cloudbees.com/job/easymock-commit/ : Build launch on commit

To compile EasyMock in Eclipse
-----------------------------
- Install m2e
- Import the EasyMock Maven parent project to your Eclipse workspace

To update the versions
----------------------
- "mvn versions:set -DnewVersion=X.Y -Pandroid,bench"
- "mvn versions:commit -Pandroid,bench" if everything is ok, "mvn versions:revert -Pandroid,bench" otherwise

Configure to deploy to the Sonatype maven repository
----------------------------------------------------
- You will first need to add something like this to your settings.xml

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

- Then follow the instructions from the site below to create your key to sign the deployed items

http://www.sonatype.com/people/2010/01/how-to-generate-pgp-signatures-with-maven/

To generate the aggregated clover report
--------------------------------------------------------------------------------------
- After a fullBuild, at the EasyMock parent project level
- Type 'mvn verify clover2:aggregate -PfullBuild'
- Then 'mvn -N clover2:clover'

To build the maven site (with findbugs, checkstyle, jdepends and JavaNCSS reports)
--------------------------------------------------------------------------------------
- You will to give enough memory to maven with 'set MAVEN_OPTS=-Xmx512m' (or setting it as environment variable)
- Then type 'mvn site'

To check dependencies and plugins versions
--------------------------------------------------------------------------------------
- mvn versions:display-dependency-updates versions:display-plugin-updates

To download the sources associated to our dependencies
--------------------------------------------------------------------------------------
- mvn dependency:resolve -Dclassifier=sources

To update the license
--------------------------------------------------------------------------------------
- mvn validate license:format

Android
--------------------------------------------------------------------------------------
- Install the Android SDK
- Configure a device (real or simulated)
- Add an ANDROID_HOME to tarket the Android SDK
- Add $ANDROID_HOME/platform-tools to your path 
- Activate the debug mode if it's a real device
- mvn install -Pandroid

To bundle EasyMock and deploy
--------------------------------------------------------------------------------------
- Update "easymock/ReleaseNotes.txt"
  - Add a little speech on the features
  - Retrieve the release notes in the textual format (http://jira.codehaus.org/browse/EASYMOCK#selectedTab=com.atlassian.jira.plugin.system.project%3Aroadmap-panel)
- In local:
  mvn versions:set -DnewVersion=x.y -Pandroid,bench
  mvn versions:commit -Pandroid,bench
  git commit -am "Move to version x.y"
  mvn -T 8.0C clean deploy -PfullBuild,deployBuild -Dgpg.passphrase=xxxx
- Close the deployment at Sonatype Nexus UI (https://oss.sonatype.org/index.html#stagingRepositories)
  More details on the deployment rules here: https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide
- Add the staging repository url found in Nexus to easymock-test-deploy
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
- In local:
  mvn -f easymock-test-deploy/pom.xml clean test
  ./prepare-website.sh x.y "This version contains cool stuff"
  git commit -am "Documentation for version x.y"
  git tag -a easymock-x.y -m "EasyMock x.y"
  git push --tags
- Release the repository. It will be synced with Maven Central Repository
- Go to the File Manager on the EasyMock SF project (https://sourceforge.net/projects/easymock/files/?source=navbar)
  - In EasyMock, create a folder named "x.y"
  - Upload "easymock/target/easymock-x.y-bundle.zip" and "easymock/ReleaseNotes.txt" in the newly created directory
  - Show the detailled information and rename it to remove the "-bundle" at the end
  - Show the detailled information and rename it to readme.txt
  - Repeat these step for EasyMock Class Extension
- Launch easymock-site on Cloudbees
- Release the Jira version (http://jira.codehaus.org/plugins/servlet/project-config/EASYMOCK/versions)
- Announce to easymock@yahoogroups.com, twitter, blog

Start next version
--------------------------------------------------------------------------------------
In local:
  - mvn versions:set -DnewVersion=X.Z-SNAPSHOT -Pandroid,bench
  - mvn versions:commit -Pandroid,bench
- Create next version in Jira (http://jira.codehaus.org/plugins/servlet/project-config/EASYMOCK/versions)
