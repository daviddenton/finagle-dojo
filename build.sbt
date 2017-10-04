lazy val baseSettings = Seq(
  name := "finagle-dojo",
  scalaVersion := "2.12.3",
  libraryDependencies ++= Seq(
    "com.twitter" %% "finagle-http" % "7.0.0"
  )
)

lazy val step1 = project.settings(baseSettings).settings(moduleName := "step1")

lazy val step2 = project.settings(baseSettings).settings(moduleName := "step2")

lazy val step3 = project.settings(baseSettings).settings(moduleName := "step3")

lazy val step4 = project.settings(baseSettings).settings(moduleName := "step4")

lazy val step5 = project.settings(baseSettings).settings(moduleName := "step5")

lazy val step6 = project.settings(baseSettings).settings(moduleName := "step6")

lazy val step7 = project.settings(baseSettings).settings(moduleName := "step7")

lazy val step8 = project.settings(baseSettings).settings(moduleName := "step8")

lazy val step9 = project.settings(baseSettings).settings(moduleName := "step9")

lazy val all = project.in(file("."))
  .settings(baseSettings)
  .aggregate(step1, step2, step3, step4, step5, step6, step7, step8, step9)
