lazy val commonSettings = Seq(
        organization:="com.xjc",
        name:="utils",
        scalaVersion:="2.11.7"
)

lazy val root = (project in file(".")).
    settings(commonSettings: _*).
    settings(
        //libraryDependencies += hadoop_common,
        resolvers += "Local Maven Repository" at "file:///data3/maven/M2Repository/"
    ) 
