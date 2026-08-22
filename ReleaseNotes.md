Add Java 26 support where bytebuddy isn't using unsafe anymore.

Change log
----------
* Add mock() methods without parameters using varargs reification ([#933](https://github.com/easymock/easymock/issues/933))
* Mock creation fails with JDK 26 and ByteBuddy 1.18.10 ([#908](https://github.com/easymock/easymock/issues/908))git log --oneline
* Use new Maven central plugin
* Use a github_token instead
* Add the missing REST calls to fully automated the release
* Move to .mvn style version
* Use assertThrows all over the place
* Optimize imports and finish updating to JUnit 5
* Update eclipse configuration
* Move all to UTF-8 like it should already be
* Use https for xsd
* Update objenesis to 3.6
* Update copyrights to 2026
* Remove Hamcrest usage
* Add Maven cache
* Bump actions/checkout from 7.0.0 to 7.0.1 ([#917](https://github.com/easymock/easymock/pull/917))
* Bump actions/github-script from 8 to 9 ([#887](https://github.com/easymock/easymock/pull/887))
* Bump actions/setup-java from 5.6.0 to 5.7.0 ([#918](https://github.com/easymock/easymock/pull/918))
* Bump activesupport from 8.1.2 to 8.1.2.1 in /website ([#882](https://github.com/easymock/easymock/pull/882))
* Bump addressable from 2.8.8 to 2.9.0 in /website ([#886](https://github.com/easymock/easymock/pull/886))
* Bump ch.qos.logback:logback-classic from 1.3.14 to 1.6.3 ([#930](https://github.com/easymock/easymock/pull/930))
* Bump com.github.spotbugs:spotbugs from 4.8.6 to 4.10.3 ([#921](https://github.com/easymock/easymock/pull/921))
* Bump com.github.spotbugs:spotbugs-maven-plugin from 4.8.6.6 to 4.10.3.0 ([#922](https://github.com/easymock/easymock/pull/922))
* Bump com.mycila:license-maven-plugin from 4.6 to 5.1.1 ([#919](https://github.com/easymock/easymock/pull/919))
* Bump com.puppycrawl.tools:checkstyle from 13.9.0 to 13.11.0 ([#931](https://github.com/easymock/easymock/pull/931))
* Bump concurrent-ruby from 1.3.6 to 1.3.7 in /website ([#913](https://github.com/easymock/easymock/pull/913))
* Bump faraday from 2.14.2 to 2.14.3 in /website ([#912](https://github.com/easymock/easymock/pull/912))
* Bump json from 2.20.0 to 2.21.2 in /website ([#924](https://github.com/easymock/easymock/pull/924))
* Bump junit.jupiter.version from 5.14.1 to 6.1.3 ([#926](https://github.com/easymock/easymock/pull/926))
* Bump net.bytebuddy:byte-buddy from 1.18.8 to 1.18.12 ([#932](https://github.com/easymock/easymock/pull/932))
* Bump nokogiri from 1.19.3 to 1.19.4 in /website ([#911](https://github.com/easymock/easymock/pull/911))
* Bump org.apache.felix:maven-bundle-plugin from 5.1.9 to 6.1.0 ([#929](https://github.com/easymock/easymock/pull/929))
* Bump org.apache.maven.plugins:maven-assembly-plugin from 3.7.1 to 3.8.0 ([#832](https://github.com/easymock/easymock/pull/832))
* Bump org.apache.maven.plugins:maven-compiler-plugin from 3.14.1 to 3.15.0 ([#863](https://github.com/easymock/easymock/pull/863))
* Bump org.apache.maven.plugins:maven-dependency-plugin from 3.10.0 to 3.11.0 ([#899](https://github.com/easymock/easymock/pull/899))
* Bump org.apache.maven.plugins:maven-enforcer-plugin from 3.6.2 to 3.6.3 ([#894](https://github.com/easymock/easymock/pull/894))
* Bump org.apache.maven.plugins:maven-jar-plugin from 3.4.2 to 3.5.0 ([#833](https://github.com/easymock/easymock/pull/833))
* Bump org.apache.maven.plugins:maven-resources-plugin from 3.4.0 to 3.5.0 ([#879](https://github.com/easymock/easymock/pull/879))
* Bump org.apache.maven.plugins:maven-shade-plugin from 3.6.1 to 3.6.2 ([#880](https://github.com/easymock/easymock/pull/880))
* Bump org.apache.maven.plugins:maven-source-plugin from 3.3.1 to 3.4.0 ([#837](https://github.com/easymock/easymock/pull/837))
* Bump org.apache.maven.plugins:maven-surefire-plugin from 3.5.5 to 3.5.6 ([#900](https://github.com/easymock/easymock/pull/900))
* Bump org.apache.maven.plugins:maven-toolchains-plugin from 3.2.0 to 3.3.0 ([#928](https://github.com/easymock/easymock/pull/928))
* Bump org.apache.maven.surefire:surefire-testng from 3.5.5 to 3.5.6 ([#897](https://github.com/easymock/easymock/pull/897))
* Bump org.codehaus.mojo:animal-sniffer-maven-plugin from 1.26 to 1.27 ([#852](https://github.com/easymock/easymock/pull/852))
* Bump org.codehaus.mojo:exec-maven-plugin from 3.6.2 to 3.6.3 ([#844](https://github.com/easymock/easymock/pull/844))
* Bump org.codehaus.mojo:jdepend-maven-plugin from 2.1 to 2.2.0 ([#855](https://github.com/easymock/easymock/pull/855))
* Bump org.codehaus.mojo:versions-maven-plugin from 2.20.1 to 2.21.0 ([#854](https://github.com/easymock/easymock/pull/854))
* Bump org.jacoco:jacoco-maven-plugin from 0.8.14 to 0.8.15 ([#905](https://github.com/easymock/easymock/pull/905))
* Bump org.objenesis:objenesis from 3.4 to 3.5 ([#860](https://github.com/easymock/easymock/pull/860))
* Bump org.ow2.asm:asm from 9.10 to 9.10.1 ([#896](https://github.com/easymock/easymock/pull/896))
* Bump org.testng:testng from 7.5.1 to 7.12.0 ([#858](https://github.com/easymock/easymock/pull/858))
* Bump slf4j.version from 2.0.17 to 2.0.18 ([#891](https://github.com/easymock/easymock/pull/891))
* Bump uri from 1.0.3 to 1.0.4 in /website ([#846](https://github.com/easymock/easymock/pull/846))
