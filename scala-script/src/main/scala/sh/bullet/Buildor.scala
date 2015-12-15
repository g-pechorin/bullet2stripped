package sh.bullet

import java.io.{FileWriter, File}

import sh.{TCreamer, Target}

object Buildor extends App with TCreamer {

  val roots = List(
    new File("../src")
  )

  val out = new File("../inc")


  def create(target: Target) = {
    val writer =
      new FileWriter({
        val file = new File(out, target.name + ".hpp")
        require(file.getParentFile.exists() || file.getParentFile.mkdirs())
        file
      })

    writer.append("// aggregated `").append(target.name).append("`\n// ... because SQLite reported this resulted in \"a 5% to 10% performance improvement\" - https://www.sqlite.org/howtocompile.html\n#pragma once\n")

    val allSource = target.headers.flatMap(lineStream)

    val outSource: Stream[TSourceLine] = {
      def recur(defined: Set[String], stream: List[TSourceLine]): Stream[TSourceLine] =
        stream match {
          case IfNDefSource(_, _, name) :: tail =>
            if (defined.contains(name))
              ???
            else
              ???

          case (raw: RawSource) :: tail =>
            raw #:: recur(defined, tail)
        }


      recur(Set(), allSource)
    }

    outSource.foldLeft("") {
      case (last, src) =>
        src match {
          case IfNDefSource(file, _, name) =>
            writer.append("#ifndef ").append(name).append("\n")

          case DefineSource(file, _, name) =>
            writer.append("#define ").append(name).append("\n")

          case EndIfSource(file, _, name) =>
            writer.append("#endif // ").append(name).append("\n")

          case IncSource(file, _, path) =>
            System.err.println("Expand these this for real")
            writer.append("# include -> ").append(path).append("\n")

          case RawSource(file, line, text) =>
            if (last != file)
              writer.append("#line %d %s\n".format(line, file))
            writer.append(text).append("\n")
        }
        src.file

    }

    // TODO ; write the source files
    System.err.println("TODO ; write the source files")

    writer.close()
  }

  create(Targets.BulletDynamics)
}
