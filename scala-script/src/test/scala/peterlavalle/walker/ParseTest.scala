package peterlavalle.walker

import junit.framework.TestCase
import fastparse.all._

class ParseTest extends TestCase {

  val tokenNewLine = P("\r".? ~ "\n")
  val tokenSpace = P(" " | "\t" | P("\\" ~ P(" " | "\t").rep(0) ~ tokenNewLine ~ P(" " | "\t").rep(0)))

  def test(): Unit = {
    ???
  }

}
