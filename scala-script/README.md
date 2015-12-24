Toy that chews up Bullet 2.82 and spits out a single header

... which doesn't build **YET**

**Because (I think that)** the pre-processor inclusion thing gets some of the headers trapped behind directive blocks that're inactive


Might be useful for other projects with not-lemon build configurations

The entry point is `[peterlavalle.shoo.Bullet](src/main/scala/peterlavalle/shoo/Bullet.scala)`


# Usage

* Turn on SBT's auto-recompile-and-run thing
	* `sbtw`
	* `~ runMain peterlavalle.shoo.Bullet`
* use `try.bullet` with CMake to see what's going on

 