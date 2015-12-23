package peterlavalle.walker

import fastparse.all._

object Pragma {

  val tokenWhiteSpace = P(" " | "\t").rep(1)
  val tokenPoundIf = P(tokenWhiteSpace.? ~ "#" ~ tokenWhiteSpace.? ~ "if" ~ tokenWhiteSpace)
  val tokenWord = P(CharIn('A' to 'Z') | CharIn('a' to 'z') | "_")
  val tokenAlphaNumeric = tokenWord | CharIn('0' to '9')
  val tokenMacroText = tokenWord ~ tokenAlphaNumeric.rep(0)

  val ruleDefined = "defined" ~ tokenWhiteSpace.? ~ "(" ~ tokenWhiteSpace.? ~ tokenMacroText.! ~ tokenWhiteSpace.? ~ ")" ~ tokenWhiteSpace.?

}
