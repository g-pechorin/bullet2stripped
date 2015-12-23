package peterlavalle.walker

import junit.framework.Assert._
import junit.framework.TestCase


class WalkerBasicTests extends TestCase {

  def testParse() {
    assertEquals(
      PragIr.If(getName, 2,
        PragCon.Defined("FOO"),
        List(
          PragIr.Include(getName, 1, "bar.h")
        ),
        PragIr.EndIf(getName, 2)
      ),
      Walker.parse(
        getName,
        """
          |# if defined(FOO)
          |# include  "bar.h"
          |#endif
        """.stripMargin.trim
      )
    )
  }

  def testWrite() {
    assertEquals(
      """
        |#line 0 "%s"
        |#if defined(FOO)
        |#include "bar.h"
        |#endif
      """.stripMargin.format(getName).trim,
      Walker.write(
        getName,
        PragIr.If(getName, 2,
          PragCon.Defined("FOO"),
          List(
            PragIr.Include(getName, 1, "bar.h")
          ),
          PragIr.EndIf(getName, 2)
        )
      )
    )
  }

  def testFull(): Unit = {
    assertEquals(
      """
        |#line 0 "%s"
        |#if defined(FOO)
        |#include "bar.h"
        |#endif
      """.stripMargin.format(getName).trim,
      Walker.write(
        getName,
        Walker.parse(
          getName,
          """
            |# if defined(FOO)
            |# include  "bar.h"
            |#endif
          """.stripMargin.trim
        )
      )
    )
  }


}
