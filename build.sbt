name := "akka-http-fun"

version := "1.0"

scalaVersion := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

mainClass in Compile := Some("main.SimpleMain")

//libraryDependencies ++= {
//  val akkaV = "2.3.9"
//  val sprayV = "1.3.3"
//  Seq(
//    "io.spray"            %%  "spray-can"     % sprayV,
//    "io.spray"            %%  "spray-routing" % sprayV,
//    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
//    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
//    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
//    "org.specs2"          %%  "specs2-core"   % "2.3.11" % "test"
//  )
//}

libraryDependencies ++= {
  val akkaV       = "2.3.12"
  val akkaStreamV = "2.0-M1"
  val scalaTestV  = "3.0.0-M12"
  Seq(
    "com.typesafe.akka" %% "akka-actor"                           % akkaV,
    "com.typesafe.akka" %% "akka-stream-experimental"             % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-core-experimental"          % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-experimental"               % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-xml-experimental"           % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental"    % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-testkit-experimental"       % akkaStreamV,
    "org.scalatest"     %% "scalatest"                            % scalaTestV % "test"
  )
}
/*
akka-http-core
akka-http
akka-http-testkit
akka-http-spray-json
akka-http-xml
 */

//Revolver.settings
    