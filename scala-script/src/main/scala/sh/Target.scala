package sh

case class Target(name: String, dependencies: Set[Target], headers: List[String], sources: Set[String]) {

  def flatDependencies: Set[Target] =
    dependencies.toList.foldLeft(dependencies) {
      case (left, next) =>
        left ++ next.flatDependencies
    }

  require(!flatDependencies.exists(_.name == name))

  def ++(other: Target): Target = {
    if (dependencies.contains(other))
      this
    else {
      Target(
        name,
        dependencies + other,
        headers,
        sources
      )
    }
  }
}

object Target {
  def apply(name: String, sources: String*): Target =
    Target(
      name,
      Set[Target](),
      sources.filter(_.matches(".*\\.(h|hh|hpp|inc)")).toList.sorted,
      sources.filterNot(_.matches(".*\\.(h|hh|hpp|inc)")).toSet
    )
}
