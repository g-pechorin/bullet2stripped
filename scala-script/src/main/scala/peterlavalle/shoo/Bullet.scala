package peterlavalle.shoo

import java.io.{Writer, FileWriter, FileInputStream, File}

object Bullet extends App {

  val searchPaths =
    new SearchPaths(
      new File("../src")
    )

  val files = List("btBulletCollisionCommon.h", "btBulletDynamicsCommon.h")

  files
    .foldLeft((new FileWriter("target/bullet.hpp").asInstanceOf[Writer], 0, "bullet.hpp")) {
      case ((w: Writer, l: Int, n: String), name) =>
        searchPaths(name) match {
          case inputStream: FileInputStream =>
            SourceLine
              .formStream(name, inputStream)
              .foldLeft((w, l, n)) {
                case ((lastWriter: Writer, lastLine: Int, lastName: String), SourceLine(sourceName: String, sourceLine: Int, sourceText: String)) =>
                  ((if (lastLine != sourceLine || lastName != sourceName)
                    lastWriter.append("#pragma %d \"%s\"\n".format(sourceLine, sourceName))
                  else
                    lastWriter).append(sourceText).append("\n"), sourceLine + 1, name)
              }
        }
    }
    ._1.close()
}
