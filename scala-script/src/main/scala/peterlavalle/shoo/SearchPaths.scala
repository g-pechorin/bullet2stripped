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
}
