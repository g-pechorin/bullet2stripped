package peterlavalle.walker

import fastparse.all._
import junit.framework.TestCase
import peterlavalle.walker.Pragma._

class PragmaTest extends TestCase {

  def testTokenWhiteSpace(): Unit = {
    tokenWhiteSpace.parse(" ") match {
      case Parsed.Success((), _) =>
    }
    tokenWhiteSpace.?.parse("") match {
      case Parsed.Success((), _) =>
    }
    tokenWhiteSpace.parse("\t ") match {
      case Parsed.Success((), _) =>
    }
  }

  def testTokenPoundIf(): Unit = {
    tokenPoundIf.parse(" #if ") match {
      case Parsed.Success((), _) =>
    }
    tokenPoundIf.parse(" #   if ") match {
      case Parsed.Success((), _) =>
    }
    tokenPoundIf.parse("\t #  \t if \t") match {
      case Parsed.Success((), _) =>
    }
  }

  def testRuleDefined(): Unit = {
    ruleDefined.parse("#if defined(FOO)") match {
      case Parsed.Success("FOO", 12) =>
        ???
    }
  }
}
