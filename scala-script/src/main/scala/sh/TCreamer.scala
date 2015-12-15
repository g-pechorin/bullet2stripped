package sh

import java.io.File

import scala.io.Source

trait TCreamer {

  val roots: List[File]

  trait TSourceLine {
    val file: String
    val line: Int
    val nesting = 0
  }

  case class RawSource(file: String, line: Int, text: String) extends TSourceLine

  case class IncSource(file: String, line: Int, path: String) extends TSourceLine

  case class EndIfSource(file: String, line: Int, name: String) extends TSourceLine {
    override val nesting = -1
  }

  case class IfDefSource(file: String, line: Int, name: String) extends TSourceLine {
    override val nesting = +1
  }

  case class IfNDefSource(file: String, line: Int, name: String) extends TSourceLine {
    override val nesting = +1
  }

  case class Else() extends TSourceLine

  case class DefineSource(file: String, line: Int, name: String) extends TSourceLine

  val rEndIf = "^\\s*#\\s*endif\\s*//\\s*([^\\s]*)\\s*$".r
  val rIfDef = "^\\s*#\\s*ifdef\\s+([^\\s]*)\\s*$".r
  val rIfNDef = "^\\s*#\\s*ifndef\\s+([^\\s]*)\\s*$".r
  val rHeader = "^\\s*#\\s*include\\s+\"(.*)\"\\s*$".r
  val rDefine = "^\\s*#\\s*define\\s+([^\\s]*)\\s*$".r

  /**
    * Pulls the named thing apart into line/by/line TSourceLine objects
    */
  def lineStream(path: String): Stream[TSourceLine] =
    read(path) match {
      case Some(source) =>
        source.split("\r?\n").toStream.zipWithIndex.map {
          case (text, line) => (text, line + 1)
        }.map {
          case (rIfDef(name), line) => IfDefSource(path, line, name)

          case (rDefine(name), line) => DefineSource(path, line, name)

          case (rIfNDef(name), line) => IfNDefSource(path, line, name)

          case (rEndIf(name), line) => EndIfSource(path, line, name)

          case (rHeader(file), line) => IncSource(path, line, file)

          case (text, line) =>
            RawSource(path, line, text)
        }
    }

  def read(self: String, path: String): Option[String] = {
    System.err.println("Do this for real")

    Some("???")
  }

  def read(path: String): Option[String] =
    roots.foldLeft(None.asInstanceOf[Option[String]]) {
      case (None, root: File) =>
        val file: File = new File(root, path)

        if (!file.exists())
          None
        else
          Some(
            Source.fromFile(file).mkString
          )

      case (found, _) =>
        found
    }
}
