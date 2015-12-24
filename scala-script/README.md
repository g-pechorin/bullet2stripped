Toy that chews up Bullet 2.82 and spits out a single header

Tested in;

* 2013-x86
* 2013-amd64
* 2015-x86
* 2015-amd64

Might be useful for other projects with not-lemon build configurations
The entry point is `[peterlavalle.shoo.Bullet](src/main/scala/peterlavalle/shoo/Bullet.scala)`

# Usage

* Turn on SBT's auto-recompile-and-run thing
	* `sbtw`
	* `~ runMain peterlavalle.shoo.Bullet`
* use `try.bullet` with CMake to see what's going on

It currently "forces" some includes to get around my limited pre-processor.
