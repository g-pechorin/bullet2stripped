package peterlavalle.shoo

import java.io._
import java.util

import scala.collection.immutable.Stream.Empty

object Bullet extends App {

  val searchPaths =
    new SearchPaths(
      new File("../src").getAbsoluteFile
    )

  val txt = "palBullet282"
  val inc = List("btBulletCollisionCommon.h", "btBulletDynamicsCommon.h")
  val src = searchPaths.listing("([\\.\\w]+/)*[\\.\\w]+\\.c(pp)?")

  def makeStreams(todo: List[String]): Stream[SourceLine] = {
    def recur(todo: List[String]): Stream[SourceLine] =
      todo match {
        case Nil => Empty

        case name :: tail => searchPaths(name) match {
          case inputStream: FileInputStream =>
            SourceLine.formStream(name, inputStream) ++ recur(tail)
        }
      }

    recur(todo)
  }

  def cookStreams(filter: (String => Boolean), data: Stream[SourceLine]): Stream[SourceLine] = {

    val rInclude = "\\s*#\\s*include\\s*\"([^\"]+)\"\\s*(//.*)?".r

    def recur(todo: Stream[SourceLine]): Stream[SourceLine] =
      todo match {
        case Empty =>
          Empty

        case SourceLine(_, _, rInclude(include, _)) #:: tail =>
          val path: String =
            todo.head.find(include) match {
              case Some(h) if searchPaths ? h => h
              case _ => include
            }

          if (filter(path))
            recur(tail)
          else if (searchPaths ? path)
            recur(SourceLine.formStream(path, searchPaths(path)) ++ tail)
          else
            todo.head #:: recur(tail)

        case head #:: tail =>
          head #:: recur(tail)
      }

    recur(data)
  }

  def writeOut(writer: Writer, streams: Stream[SourceLine]): Writer =
    streams.foldLeft((writer, -1, "-1")) {
      case ((lastWriter: Writer, lastLine: Int, lastName: String), SourceLine(sourceName: String, sourceLine: Int, sourceText: String)) =>
        ((if (lastLine != sourceLine || lastName != sourceName)
          lastWriter.append("#line %d \"%s\"\n".format(sourceLine, sourceName))
        else
          lastWriter).append(sourceText).append("\n"), sourceLine + 1, sourceName)
    }._1

  // HACK ; guard against multiple inclusion
  val visited = new util.HashSet[String]()

  val writer = new FileWriter("target/%s.hpp".format(txt))

  // write the header section
  writeOut(writer, cookStreams(p => !visited.add(p), makeStreams(inc)))

  // write the body
  writer.append("#ifdef %s_CPP\n".format(txt.toUpperCase))
  writeOut(writer,  cookStreams(p => !visited.add(p), makeStreams(src)))
  writer.append("#endif // %s_CPP\n".format(txt.toUpperCase)).close()

}
