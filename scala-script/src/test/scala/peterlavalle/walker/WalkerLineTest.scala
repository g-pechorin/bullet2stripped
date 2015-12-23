package peterlavalle.walker

import junit.framework.Assert._
import junit.framework.TestCase

class WalkerLineTest extends TestCase {
  def testSplitLines(): Unit = {
    assertEquals(
      Stream(
        Walker.Line("# if defined(FOO)  !defined(BAR)", "# if defined(FOO) \\\n !defined(BAR)", getName, 0),
        Walker.Line("# include  \"bar.h\"", "# include  \"bar.h\"", getName, 2),
        Walker.Line("#endif", "#endif", getName, 3)
      ),
      Walker.enline(getName,
        """
          |# if defined(FOO) \
          | !defined(BAR)
          |# include  "bar.h"
          |#endif
        """.stripMargin.trim.replaceAll("\r?\n", "\n")
      )
    )
  }

}
