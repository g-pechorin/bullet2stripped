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

This is a fork of "Bullet 2 Stripped" which was itself a fork of Bullet 2.82 (by the original creator)
Peter LaValle forked it to get a CMake build which could be embedded with `add_subdirectory(...)` into another project.

Since that time Peter LaValle has assembled [the script](scala-script/) to prepare [palBullet282](palBullet282.hpp) which contains the whole physics library in one "stb-style" `.hpp` header.

If this doesn't make sense to you ; just use [mainstream Bullet](https://github.com/erwincoumans/bullet3)
