package peterlavalle

package object walker {
  def ??? = {
    val notImplementedError = new NotImplementedError()
    notImplementedError.setStackTrace(notImplementedError.getStackTrace.tail)
    throw notImplementedError
  }
}
