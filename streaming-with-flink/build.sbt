name := "Streaming with Flink"

version := "0.1.0"

scalaVersion := "2.12.15"

// resolvers += Resolver.mavenLocal
// resolvers += "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/"
// resolvers += "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository"

// javacOptions ++= Seq("-source", "11", "-target", "11")

val flinkVersion = "1.14.3"

libraryDependencies += "org.apache.flink" % "flink-core" % flinkVersion
libraryDependencies += "org.apache.flink" %% "flink-scala" % flinkVersion
libraryDependencies += "org.apache.flink" %% "flink-streaming-scala" % flinkVersion
libraryDependencies += "org.apache.flink" %% "flink-runtime-web" % flinkVersion
libraryDependencies += "org.apache.flink" % "flink-queryable-state-runtime" % flinkVersion
libraryDependencies += "org.apache.derby" % "derby" % "10.13.1.1" % "provided"
libraryDependencies += "org.slf4j"        % "slf4j-log4j12" % "1.7.25" // % "runtime"
libraryDependencies += "log4j"            % "log4j" % "1.2.17" % "runtime"
