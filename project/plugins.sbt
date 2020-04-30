resolvers ++= Seq(
  "Sonatype OSS Snapshot Repository" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype OSS Release Repository" at "https://oss.sonatype.org/content/repositories/releases/",
  Resolver.bintrayRepo("beyondthelines", "maven"),
  Resolver.bintrayRepo("kamon-io", "sbt-plugins"),
  "Seasar2 Repository" at "https://maven.seasar.org/maven2"
)
libraryDependencies ++= Seq(
  "com.thesamet.scalapb" %% "compilerplugin" % "0.10.2",
  "org.seasar.util"      % "s2util"          % "0.0.1"
)

addSbtPlugin("com.chatwork" % "sbt-wix-embedded-mysql" % "1.0.9")

addSbtPlugin("jp.co.septeni-original" % "sbt-dao-generator" % "1.0.8")

addSbtPlugin("io.github.davidmweber" % "flyway-sbt" % "5.0.0")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.3.2")

addSbtPlugin("io.kamon" % "sbt-aspectj-runner" % "1.1.0")

addSbtPlugin("com.lightbend.sbt" % "sbt-javaagent" % "0.1.4")

addSbtPlugin("com.typesafe.sbt" % "sbt-multi-jvm" % "0.4.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.10")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")

addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.31")

addSbtPlugin("com.lightbend.akka.grpc" % "sbt-akka-grpc" % "0.8.2")

addSbtPlugin("com.mintbeans" % "sbt-ecr" % "0.14.1")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.0")

addSbtPlugin("org.foundweekends.giter8" % "sbt-giter8-scaffold" % "0.11.0")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.3.2")

addSbtPlugin("io.kamon" % "sbt-aspectj-runner" % "1.1.0")

addSbtPlugin("com.lightbend.sbt" % "sbt-javaagent" % "0.1.4")

addSbtPlugin("com.typesafe.sbt" % "sbt-multi-jvm" % "0.4.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.10")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")

addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.31")

addSbtPlugin("com.lightbend.akka.grpc" % "sbt-akka-grpc" % "0.8.2")

addSbtPlugin("com.mintbeans" % "sbt-ecr" % "0.14.1")
