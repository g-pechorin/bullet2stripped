# TL:DR

In your **main** `.cpp` file;

```
#define PALBULLET282_CPP // include the implementation
#include "palBullet282.hpp"
```

... then in every other one just ...

```
#include "palBullet282.hpp" // only include the interface
```

# bullet2stripped

This is a fork of "Bullet 2 Stripped" which 

1. Erwin Coumas forked (his own project) Bullet to strip out cruft
2. Peter LaValle forked the result of the above to make a CMake build
  * ... to get somethig that worked with `add_subdirectory(...)` from within an outer project
3. Peter LaValle then used that version, with a Scala program, to make this single header variant
  * this included some trivial renames and removal of duplicate static globals

[The program](scala-script/) is used to prepare [palBullet282](palBullet282.hpp) which contains the whole physics library in one "stb-style" `.hpp` header.

[Mainstream Bullet](https://github.com/erwincoumans/bullet3) 
