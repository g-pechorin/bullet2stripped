package peterlavalle.shoo

import java.io.ByteArrayInputStream

import org.junit.Assert._
import junit.framework.TestCase

class SourceLineTest extends TestCase {
  def testFormStream(): Unit = {
    assertEquals(
      Stream(
        SourceLine("foo/goo", 0, ""),
        SourceLine("foo/goo", 1, ""),
        SourceLine("foo/goo", 2, "#ifndef	BT_MATRIX3x3_H"),
        SourceLine("foo/goo", 3, "#define BT_MATRIX3x3_H"),
        SourceLine("foo/goo", 4, ""),
        SourceLine("foo/goo", 5, "#include \"btVector3.h\""),
        SourceLine("foo/goo", 6, "#include \"btQuaternion.h\""),
        SourceLine("foo/goo", 7, "#include <stdio.h>"),
        SourceLine("foo/goo", 8, ""),
        SourceLine("foo/goo", 9, "#ifdef BT_USE_SSE")
      ),
      SourceLine.formStream(
        "foo/goo",
        new ByteArrayInputStream(
          """
            |
            |#ifndef	BT_MATRIX3x3_H
            |#define BT_MATRIX3x3_H
            |
            |#include "btVector3.h"
            |#include "btQuaternion.h"
            |#include <stdio.h>
            |
            |#ifdef BT_USE_SSE
            |
            |
          """.stripMargin.replaceAll("[\\s^\n]*$", "").getBytes("UTF8")
        )
      )
    )
  }

  def testDoPath(): Unit = {
    assertEquals(
      Some(List("foo/btVector3.h", "btVector3.h")),
      SourceLine("foo/goo", 0, "").find("btVector3.h")
    )
  }
}
