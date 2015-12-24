package peterlavalle.shoo

import java.io._
import java.util

import scala.collection.immutable.Stream.Empty

object Bullet extends App {

  val searchPaths =
    new SearchPaths(
      new File("../src").getAbsoluteFile
    )

  val txt = "Bullet"
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

  val rawStream = makeStreams(inc)


  val visited = new util.HashSet[String]()

  def cookStreams(filter: (String => Boolean), data: Stream[SourceLine]): Stream[SourceLine] = {

    val rInclude = "\\s*#\\s*include\\s*\"([^\"]+)\"\\s*".r

    def recur(todo: Stream[SourceLine]): Stream[SourceLine] =
      todo match {
        case Empty =>
          Empty

        case SourceLine(_, _, rInclude(include)) #:: tail =>
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

  val outStream = cookStreams(p => !visited.add(p), rawStream)

  val (writer, _, _) =
    outStream.foldLeft((new FileWriter("target/%s.hpp".format(txt)).asInstanceOf[Writer], -1, "-1")) {
      case ((lastWriter: Writer, lastLine: Int, lastName: String), SourceLine(sourceName: String, sourceLine: Int, sourceText: String)) =>
        ((if (lastLine != sourceLine || lastName != sourceName)
          lastWriter.append("#pragma %d \"%s\"\n".format(sourceLine, sourceName))
        else
          lastWriter).append(sourceText).append("\n"), sourceLine + 1, sourceName)
    }

  cookStreams(p => !visited.add(p), makeStreams(src)).foldLeft((writer.append("#ifdef %s_CPP\n".format(txt)), -1, "-1")) {
    case ((lastWriter: Writer, lastLine: Int, lastName: String), SourceLine(sourceName: String, sourceLine: Int, sourceText: String)) =>
      ((if (lastLine != sourceLine || lastName != sourceName)
        lastWriter.append("#pragma %d \"%s\"\n".format(sourceLine, sourceName))
      else
        lastWriter).append(sourceText).append("\n"), sourceLine + 1, sourceName)
  }._1.append("#enfif // %s_CPP\n".format(txt)).close()
}
