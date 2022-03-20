name := "flinkDemo"

version := "0.1.0"

scalaVersion := "2.12.15"

// resolvers += Resolver.mavenLocal
// resolvers += "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/"
// resolvers += "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository"

// javacOptions ++= Seq("-source", "11", "-target", "11")

val flinkVersion = "1.14.4"

val flinkDependencies = Seq(
  "org.apache.flink" %  "flink-core"                      % flinkVersion % "provided",
  "org.apache.flink" %% "flink-scala"                     % flinkVersion % "provided",
  "org.apache.flink" %% "flink-streaming-scala"           % flinkVersion % "provided",
  "org.apache.flink" %% "flink-table-api-scala-bridge"    % flinkVersion,
  "org.apache.flink" %% "flink-table-planner"             % flinkVersion % "provided",  // needed for IDE,
  "org.apache.flink" %% "flink-runtime-web"               % flinkVersion,
  // "org.apache.flink" %  "flink-queryable-state-runtime"   % flinkVersion,
  "org.apache.flink" %  "flink-csv"                       % flinkVersion
)

val otherDependencies = Seq(
  "org.apache.derby"           %  "derby"         % "10.13.1.1" % "provided",
  "org.slf4j"                  %  "slf4j-log4j12" % "1.7.25", // % "runtime"
  "org.apache.logging.log4j"   % "log4j-core"     % "2.17.1", //  % "runtime",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
)

libraryDependencies ++= flinkDependencies
libraryDependencies ++= otherDependencies

// stays inside the sbt console when we press "ctrl-c" while a Flink programme executes with "run" or "runMain"
Compile / run / fork := true
Global / cancelable := true

// exclude Scala library from assembly
assembly / assemblyOption  := (assembly / assemblyOption).value.copy(includeScala = false)