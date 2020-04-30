import Dependencies.{ logback, _ }
import scala.concurrent.duration._

val projectPrefix = "example"

val baseSettings = Seq(
  scalaVersion := "2.13.1",
  organization := "com.example",
  version := "1.0.0-SNAPSHOT",
  scalacOptions ++=
    Seq(
      "-feature",
      "-deprecation",
      "-unchecked",
      "-encoding",
      "UTF-8",
      "-language:_",
      "-target:jvm-1.8"
    ),
  resolvers ++= Seq(
      "Sonatype OSS Snapshot Repository" at "https://oss.sonatype.org/content/repositories/snapshots/",
      "Sonatype OSS Release Repository" at "https://oss.sonatype.org/content/repositories/releases/",
      "Akka Snapshots" at "https://repo.akka.io/snapshots",
      Resolver.bintrayRepo("akka", "snapshots"),
      "Seasar Repository" at "https://maven.seasar.org/maven2/",
      "DynamoDB Local Repository" at "https://s3-ap-northeast-1.amazonaws.com/dynamodb-local-tokyo/release",
      Resolver.bintrayRepo("beyondthelines", "maven"),
      Resolver.bintrayRepo("segence", "maven-oss-releases"),
      // Resolver.bintrayRepo("everpeace", "maven"),
      Resolver.bintrayRepo("tanukkii007", "maven"),
      Resolver.bintrayRepo("kamon-io", "snapshots")
    ),
  libraryDependencies ++= Seq(
      scalaLang.scalaReflect(scalaVersion.value),
      iheart.ficus,
      slf4j.api,
      sulky.ulid,
      monix.monix,
      timepit.refined,
      airframe.airframe,
      scalatest.scalatest   % Test,
      scalacheck.scalacheck % Test
    ),
  scalafmtOnCompile := true
)
val baseDir    = "."
val modulesDir = s"$baseDir/modules"

val infrastructure =
  (project in file(s"$modulesDir/infrastructure"))
    .settings(baseSettings)
    .settings(
      name := s"$projectPrefix-infrastructure",
      libraryDependencies ++= Seq(
          logback.classic % Test
        )
    )

val domain = (project in file(s"$modulesDir/domain"))
  .settings(baseSettings)
  .settings(name := s"$projectPrefix-domain")
  .dependsOn(infrastructure)

val contractsDir = s"$baseDir/contracts"

val `contract-interface-adaptor-command` =
  (project in file(s"$contractsDir/interface-adaptor-command"))
    .enablePlugins(AkkaGrpcPlugin)
    .settings(baseSettings)
    .settings(
      name := s"$projectPrefix-contract-interface-command",
      libraryDependencies ++= Seq(
          playCore,
          guice,
          akka.actorTyped,
          akka.slf4j,
          akka.stream
        ),
      PB.protoSources in Compile += (baseDirectory in LocalRootProject).value / "protobuf" / "command"
    )
    .dependsOn(domain)

val `contract-interface-adaptor-query` =
  (project in file(s"$contractsDir/interface-adaptor-query"))
    .settings(baseSettings)
    .settings(
      name := s"$projectPrefix-contract-interface-query",
      libraryDependencies ++= Seq(
          playCore,
          akka.slf4j,
          akka.stream
        ),
      PB.protoSources in Compile += (baseDirectory in LocalRootProject).value / "protobuf" / "query"
    )

val `contract-use-case` =
  (project in file(s"$contractsDir/use-case"))
    .settings(baseSettings)
    .settings(
      name := "truss-contract-use-case",
      libraryDependencies ++= Seq(
          akka.actorTyped,
          akka.stream
        )
    )
    .dependsOn(domain)

// --- modules

val `use-case` =
  (project in file(s"$baseDir/modules/use-case"))
    .settings(baseSettings)
    .settings(
      name := s"$projectPrefix-use-case",
      libraryDependencies ++= Seq(
          akka.actorTyped,
          logback.classic    % Test,
          akka.testKitTyped  % Test,
          akka.streamTestKit % Test
        )
    )
    .dependsOn(`contract-use-case`, `contract-interface-adaptor-command`, infrastructure, domain)
val dbDriver    = "com.mysql.jdbc.Driver"
val dbName      = "dddbase"
val dbUser      = "dddbase"
val dbPassword  = "passwd"
val dbPort: Int = Utils.RandomPortSupport.temporaryServerPort()
val dbUrl       = s"jdbc:mysql://localhost:$dbPort/$dbName?useSSL=false"
lazy val flyway = (project in file("tools/flyway"))
  .settings(baseSettings)
  .settings(
    libraryDependencies ++= Seq(
        "mysql" % "mysql-connector-java" % "5.1.42"
      ),
    parallelExecution in Test := false,
    wixMySQLVersion := com.wix.mysql.distribution.Version.v5_6_21,
    wixMySQLUserName := Some(dbUser),
    wixMySQLPassword := Some(dbPassword),
    wixMySQLSchemaName := dbName,
    wixMySQLPort := Some(dbPort),
    wixMySQLDownloadPath := Some(sys.env("HOME") + "/.wixMySQL/downloads"),
    wixMySQLTimeout := Some(2 minutes),
    flywayDriver := dbDriver,
    flywayUrl := dbUrl,
    flywayUser := dbUser,
    flywayPassword := dbPassword,
    flywaySchemas := Seq(dbName),
    flywayLocations := Seq(
        s"filesystem:${baseDirectory.value}/src/test/resources/rdb-migration/"
      ),
    skip in publish := true,
    flywayMigrate := (flywayMigrate dependsOn wixMySQLStart).value
  )
  .enablePlugins(FlywayPlugin)
val `interface-adaptor-common` = (project in file(s"$modulesDir/interface-adaptor-common"))
  .disablePlugins(WixMySQLPlugin)
  .settings(baseSettings)
  .settings(
    name := s"$projectPrefix-interface-adaptor-common",
    libraryDependencies ++= Seq(
        playCore,
        guice,
        "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
        circe.core,
        circe.generic,
        circe.parser,
        akka.http,
        heikoseeberger.akkaHttpCirce,
        slick.slick,
        slick.slickHikariCP
      ),
    // JDBCのドライバークラス名を指定します(必須)
    driverClassName in generator := dbDriver,
    // JDBCの接続URLを指定します(必須)
    jdbcUrl in generator := dbUrl,
    // JDBCの接続ユーザ名を指定します(必須)
    jdbcUser in generator := dbUser,
    // JDBCの接続ユーザのパスワードを指定します(必須)
    jdbcPassword in generator := dbPassword,
    // カラム型名をどのクラスにマッピングするかを決める関数を記述します(必須)
    propertyTypeNameMapper in generator := {
      case "INTEGER" | "INT" | "TINYINT"     => "Int"
      case "BIGINT"                          => "Long"
      case "VARCHAR"                         => "String"
      case "BOOLEAN" | "BIT"                 => "Boolean"
      case "DATE" | "TIMESTAMP" | "DATETIME" => "java.time.Instant"
      case "DECIMAL"                         => "BigDecimal"
      case "ENUM"                            => "String"
    },
    tableNameFilter in generator := { tableName: String =>
      (tableName.toUpperCase != "SCHEMA_VERSION") && (tableName
        .toUpperCase() != "FLYWAY_SCHEMA_HISTORY") && !tableName.toUpperCase
        .endsWith("ID_SEQUENCE_NUMBER")
    },
    outputDirectoryMapper in generator := {
      case s if s.endsWith("Spec") => (sourceDirectory in Test).value
      case s =>
        new java.io.File((scalaSource in Compile).value, "/com/github/j5ik2o/warikan/interfaceAdaptor/dao")
    },
    // モデル名に対してどのテンプレートを利用するか指定できます。
    templateNameMapper in generator := {
      case className if className.endsWith("Spec") => "template_spec.ftl"
      case className                               => "template.ftl"
    },
    generateAll in generator := Def
        .taskDyn {
          val ga = (generateAll in generator).value
          Def
            .task {
              (wixMySQLStop in flyway).value
            }
            .map(_ => ga)
        }
        .dependsOn(flywayMigrate in flyway)
        .value,
    compile in Compile := ((compile in Compile) dependsOn (generateAll in generator)).value
  )
  .dependsOn(flyway)
  .disablePlugins(WixMySQLPlugin)

val `interface-adaptor-query` =
  (project in file(s"$modulesDir/interface-adaptor-query"))
    .enablePlugins(PlayScala)
    .settings(baseSettings)
    .settings(
      name := s"$projectPrefix-interface-adaptor-query",
      libraryDependencies ++= Seq(
          ghostdogpr.caliban,
          ghostdogpr.calibanAkkaHttp,
          "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
        )
    )
    .dependsOn(`contract-interface-adaptor-query`, `interface-adaptor-common`, infrastructure)

val `interface-adaptor-command` =
  (project in file(s"$modulesDir/interface-adaptor-command"))
    .enablePlugins(PlayScala)
    .settings(baseSettings)
    .settings(
      name := s"$projectPrefix-interface-adaptor-command",
      libraryDependencies ++= Seq(
          akka.streamKafka,
          akka.streamKafkaClusterSharding,
          akka.discovery,
          akka.actorTyped,
          akka.clusterTyped,
          akka.clusterShardingTyped,
          akka.persistenceTyped,
          akka.serializationJackson,
          megard.akkaHttpCors,
          j5ik2o.akkaPersistenceDynamodb,
          j5ik2o.akkaPersistenceKafka,
          j5ik2o.akkaPersistenceS3,
          akkaManagement.akkaManagement,
          akkaManagement.clusterHttp,
          akkaManagement.clusterBootstrap,
          akkaManagement.k8sApi,
          aspectj.aspectjweaver,
          j5ik2o.reactiveAwsDynamodbTest % Test,
          logback.classic                % Test,
          akka.testKit                   % Test,
          akka.testKitTyped              % Test,
          akka.streamTestKit             % Test,
          akka.multiNodeTestKit          % Test,
          embeddedkafka.embeddedKafka    % Test,
          whisk.dockerTestkitScalaTest   % Test,
          whisk.dockerTestkitImplSpotify % Test,
          slf4j.julToSlf4j               % Test,
          "org.scalatestplus.play"       %% "scalatestplus-play" % "5.0.0" % Test
        )
    )
    .dependsOn(`contract-interface-adaptor-command`, `interface-adaptor-common`, infrastructure, `use-case`)

// --- applications

val applicationsDir = s"$baseDir/applications"

lazy val play = (project in file(s"$applicationsDir/api-sever"))
  .enablePlugins(PlayScala)
  .settings(baseSettings)
  .settings(
    name := s"$projectPrefix-api-server",
//    libraryDependencies += guice,
    libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
//
    // Adds additional packages into Twirl
    //TwirlKeys.templateImports += "com.example.controllers._"

    // Adds additional packages into conf/routes
    // play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
  )
  .dependsOn(`interface-adaptor-command`, `interface-adaptor-query`)

lazy val root = (project in file("."))
  .settings(name := s"$projectPrefix-root")
  .aggregate(play)
