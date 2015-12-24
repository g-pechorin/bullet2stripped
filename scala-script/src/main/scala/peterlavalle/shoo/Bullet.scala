package peterlavalle.shoo

import java.io._

import scala.collection.immutable.Stream.Empty

object Bullet extends App {

  val searchPaths =
    new SearchPaths(
      new File("../src")
    )

  val files = List("btBulletCollisionCommon.h", "btBulletDynamicsCommon.h")


  val rawStream = {
    def recur(todo: List[String]): Stream[SourceLine] =
      todo match {
        case Nil => Empty

        case name :: tail => searchPaths(name) match {
          case inputStream: FileInputStream =>
            SourceLine.formStream(name, inputStream) ++ recur(tail)
        }
      }

    recur(files)
  }

  val outStream = {

    val rInclude = "\\s*#\\s*include\\s*\"([^\"]+)\"\\s*".r

    def recur(todo: Stream[SourceLine]): Stream[SourceLine] =
      todo match {
        case Empty =>
          Empty

        case SourceLine(_, _, rInclude(include)) #:: tail =>
          val path: String =
            todo.head.find(include) match {
              case Some(inc) if searchPaths ? inc => inc
              case _ => include
            }

          if (searchPaths ? path)
            recur(SourceLine.formStream(path, searchPaths(path)) ++ tail)
          else
            todo.head #:: recur(tail)

        case head #:: tail =>
          head #:: recur(tail)
      }

    recur(rawStream)
  }

  val (writer, _, _) =
    outStream.foldLeft((new FileWriter("target/bullet.hpp").asInstanceOf[Writer], -1, "-1")) {
      case ((lastWriter: Writer, lastLine: Int, lastName: String), SourceLine(sourceName: String, sourceLine: Int, sourceText: String)) =>
        ((if (lastLine != sourceLine || lastName != sourceName)
          lastWriter.append("#pragma %d \"%s\"\n".format(sourceLine, sourceName))
        else
          lastWriter).append(sourceText).append("\n"), sourceLine + 1, sourceName)
    }
  writer.close()
}
