package peterlavalle.shoo

import java.io.{FileInputStream, File}

class SearchPaths(roots: List[File]) {
  def this(roots: File*) = this(roots.toList)

  def apply(name: String): FileInputStream = {
    roots.foldLeft(null.asInstanceOf[FileInputStream]) {
      case (null, root) =>
        val file = new File(root, name)

        if (file.exists())
          new FileInputStream(file)
        else
          ???
    }
  }
}
