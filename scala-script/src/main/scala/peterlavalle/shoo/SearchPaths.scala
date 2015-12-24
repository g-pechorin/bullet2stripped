package peterlavalle.shoo

import java.io.{FileInputStream, File}

class SearchPaths(roots: List[File]) {
  def this(roots: File*) = this(roots.toList)

  def ?(name: String) =
    roots.foldLeft(false) {
      case (false, root) =>
        new File(root, name).exists()
      case _ =>
        true
    }

  def apply(name: String): FileInputStream = {
    roots.foldLeft(null.asInstanceOf[FileInputStream]) {
      case (null, root) =>
        val file = new File(root, name)

        if (file.exists())
          new FileInputStream(file)
        else
          null
    }
  }

  def listing(pattern: String): List[String] =
    roots.foldLeft(Set[String]()) {
      case (found, root) =>

        def recur(done: Set[String], list: List[String]): Set[String] =
          list match {
            case Nil =>
              done

            case name :: tail =>
              val file = new File(root, name)

              val (okay, next) =
                if (file.isFile)
                  if (name.matches(pattern))
                    (done + name, tail)
                  else
                    (done, tail)
                else {
                  require(file.isDirectory)

                  (done, file.list().map(name + "/" + _).foldRight(tail)(_ :: _))
                }

              recur(okay, next)
          }

        recur(found, root.list().toList)
    }.toList.sorted
}
