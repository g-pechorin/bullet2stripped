package peterlavalle.walker

object PragIr {

  type Contents = List[TPragIr]

  sealed trait TPragIr {
    val file: String
    val line: Int
  }

  sealed trait TPragBranch extends TPragIr


  case class EndIf(file: String, line: Int) extends TPragBranch

  case class If(file: String, line: Int, condition: PragCon.TPragCon, contents: Contents, alternative: TPragBranch) extends TPragBranch

  case class Include(file: String, line: Int, path: String) extends TPragIr

}
