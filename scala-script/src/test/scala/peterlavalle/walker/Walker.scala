package peterlavalle.walker

import fastparse.all._
import peterlavalle.walker.Pragma._

object Walker {


  def parse(file: String, source: String): PragIr.TPragIr = {

    case class State(lines: List[Line], defined: Set[String])

    def recur(state: State, lines: Stream[Line]): State =
      lines match {
        case Walker.Line(effective, original, lineFile, lineLine) #:: tail =>
          tokenPoundIf.parse(effective) match {
            case Parsed.Success((), index) =>
              ruleDefined.parse(effective.substring(index)) match {
                case Parsed.Success((symbol), _) =>
                  ???
              }
          }

          ???
      }

    recur(State(Nil, Set()), enline(file, source))

    ???
  }

  def write(file: String, pragir: PragIr.TPragIr): String =
    ???

  def enline(file: String, text: String): Stream[Line] =
    if (text.contains("\r"))
      enline(file, text.replaceAll("\r?\n", "\n"))
    else {
      def recur(start: Int, index: Int): Stream[Line] =
        if (text.length == index)
          if (start != index)
            Line(
              text.substring(start, index),
              file,
              text.substring(0, start).count(_ == '\n')
            ) #:: recur(index, index)
          else
            Stream.Empty
        else
          text(index) match {
            case '\\' =>
              val prefix = text.substring(start, index)
              assert(prefix != null)

              text.indexOf('\n', index) match {
                case -1 =>
                  ???

                case begin if begin > index =>
                  recur(begin + 1, begin + 1) match {
                    case Line(headEffective: String, headOriginal: String, headFile: String, headLine: Int) #:: tail =>
                      require(headFile == file)

                      // manufacture an entry
                      Line(
                        prefix + headEffective,
                        text.substring(start, begin + 1) + headOriginal,
                        file,
                        text.substring(0, start).count(_ == '\n')
                      ) #:: tail
                  }
              }

            case '\n' =>
              Line(
                text.substring(start, index),
                file,
                text.substring(0, start).count(_ == '\n')
              ) #:: recur(index + 1, index + 1)

            case c =>
              require(c != '\r')
              recur(start, index + 1)
          }

      recur(0, 0)
    }

  def Line(content: String, file: String, line: Int): Line =
    new Line(content, content, file, line)

  case class Line(effective: String, original: String, file: String, line: Int)

}
