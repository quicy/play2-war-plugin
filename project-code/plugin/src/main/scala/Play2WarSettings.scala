package com.github.play2war.plugin

import sbt.{ `package` => _, _ }
import sbt.Keys._
import PlayKeys._
import Play2WarKeys._

trait Play2WarSettings {
  this: Play2WarCommands =>

  val backlog = "https://everforth.backlog.jp/dav/APC/maven/"

  lazy val play2WarSettings = Seq[Setting[_]](
    
    libraryDependencies <++= (servletVersion) { (v) =>
      val servletVersionString = v match {
        case "2.5" => "25"
        case _ => "30"
      }
      Seq("com.github.play2war" %% ("play2-war-core-servlet" + servletVersionString) % com.github.play2war.plugin.Play2WarVersion.current)
    },

    resolvers ++= Seq(
      // Releases
      "Play2war plugin" at backlog + "releases",
      // Snapshots
      "Play2war plugin snapshot" at backlog + "snapshots"),

    webappResource <<= baseDirectory / "war",

    // War attifact
    artifact in war <<= moduleName(n => Artifact(n, "war", "war")),

    // Bind war building to "war" task
    war <<= warTask,

    // Bind war task to "package" task (phase)
    `package` <<= war //
    ) ++
    // Attach war artifact. War file is published on "publish-local" and "publish"
    Seq(addArtifact(artifact in (Compile, war), war).settings: _*)

}
