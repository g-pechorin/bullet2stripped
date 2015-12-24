package peterlavalle.shoo

import java.io.InputStream

import scala.io.Source

case class SourceLine(name: String, line: Int, text: String) {
  require(name.matches("(\\.\\./)*([\\w\\.]+/)*[\\w\\.]+"))

  def find(path: String): Option[String] = {
    require(path.matches("(\\.\\./)*([\\w\\.]+/)*[\\w\\.]+"))

    def refine(string: String): Option[String] = {
      string.replaceAll("([\\w\\.]+)(/[\\w\\.]+/\\.\\./)([\\w\\.]+)", "$1/$3") match {
        case same if same == string =>
          Some(string)
        case other =>
          ???
      }
    }

    refine(name.replaceAll("[\\w\\.]*$", "") + path)
  }
}

object SourceLine {
  def formStream(name: String, inputStream: InputStream): Stream[SourceLine] =
    Source
      .fromInputStream(inputStream)
      .getLines()
      .toStream
      .zipWithIndex
      .map {
        case (text, line) =>
          SourceLine(name, line, text)
      }

}
