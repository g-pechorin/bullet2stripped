package peterlavalle

/**
  * Created by peter on 23/12/2015.
  */
package object shoo {
  def ??? = {
    val notImplementedError: NotImplementedError = new NotImplementedError()
    notImplementedError.setStackTrace(notImplementedError.getStackTrace.tail)
    throw notImplementedError
  }
}
