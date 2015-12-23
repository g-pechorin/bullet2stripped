package peterlavalle.walker

import fastparse.all._

object FastParse extends App {

  // http://lihaoyi.github.io/fastparse/#Basic
  if (true) {

    // Basic
    val parseA = P("a")

    parseA.parse("a") match {
      case Parsed.Success(value, successIndex) =>
        assert(value ==(), successIndex == 1)
    }

    parseA.parse("b") match {
      case failure: Parsed.Failure =>
        assert(failure.lastParser == ("a": P0))
        assert(failure.index == 0)
        assert(failure.extra.traced.trace == """parseA:1:1 / "a":1:1 ..."b"""")
    }

    // Sequence
    val ab = P("a" ~ "b")

    ab.parse("ab") match {
      case Parsed.Success(_, index) =>
        assert(2 == index)
    }

    ab.parse("aa") match {
      case Parsed.Failure(parser, index, _) =>
        assert(parser == ("b": P0))
        assert(1 == index)
    }
  }

  // http://lihaoyi.github.io/fastparse/#Repeat
  if (true) {
    val ab = P("a".rep ~ "b")
    val Parsed.Success(_, 8) = ab.parse("aaaaaaab")
    val Parsed.Success(_, 4) = ab.parse("aaaba")

    val abc = P("a".rep(sep = "b") ~ "c")
    val Parsed.Success(_, 8) = abc.parse("abababac")
    val Parsed.Failure(parser, 3, _) = abc.parse("abaabac")

    val ab4 = P("a".rep(min = 2, max = 4, sep = "b"))
    val Parsed.Success(_, 7) = ab4.parse("ababababababa")

    val ab4c = P("a".rep(min = 2, max = 4, sep = "b") ~ "c")
    val Parsed.Failure(_, 1, _) = ab4c.parse("ac")
    val Parsed.Success(_, 4) = ab4c.parse("abac")
    val Parsed.Success(_, 8) = ab4c.parse("abababac")
    val Parsed.Failure(_, 7, _) = ab4c.parse("ababababac")
  }
}
