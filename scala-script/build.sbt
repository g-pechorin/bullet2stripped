
name := "hello sbtw"
version := "1.0"
scalaVersion := "2.11.7"

TaskKey[Unit]("sbtWrapper", "Downloads SBT") := {
  import java.util.zip.ZipInputStream
  
  println("TODO ; check for sbt.versioon")
  // http://www.scala-sbt.org/0.13/tutorial/Hello.html#Setting+the+sbt+version
  val sbtWrappedVersion = "0.13.9"
  
  ///
  /// download and extract sbt to sbtw/
  val zipInputStream =
    new ZipInputStream(
      new java.net.URL(
        "https://dl.bintray.com/sbt/native-packages/sbt/{sbtWrappedVersion}/sbt-{sbtWrappedVersion}.zip".replace("{sbtWrappedVersion}", sbtWrappedVersion)
      ).openStream()
    )
  
  // neato trick to hammer the .zip until it's empty
  Stream.continually(zipInputStream.getNextEntry).takeWhile(_ != null).filterNot(_.getName.endsWith("/")).foreach {
    case zipEntry =>
      val buffer = Array.ofDim[Byte](zipEntry.getSize.toInt)
      
      // TODO ; stop using a var here
      var read = 0      
      while (read < buffer.size) {
        var r = zipInputStream.read(buffer, read, buffer.size - read)
        require (r > 0)
        read = read + r
      }
      
      val outputStream =
        new java.io.FileOutputStream({
          // TODO ; check versions against internal paths
          val outputFile = new File(zipEntry.getName.replaceAll("^sbt/", "sbtw/").replace("\\", "/"))
          require(outputFile.getParentFile.exists || outputFile.getParentFile.mkdirs)
          outputFile
        })
      outputStream.write(buffer)
      outputStream.close()
  }
  
  // write sbtw.bat
  new java.io.FileWriter("sbtw.bat")
    .append(
      """
      |@ECHO OFF
      |SET CMD_LINE_ARGS=%$
      |SET PATH=sbtw/bin:%PATH%
      |sbt %2 %*
      """
      .stripMargin
    )
    .close
  
  println("TODO ; write sbtw (.sh)")
}