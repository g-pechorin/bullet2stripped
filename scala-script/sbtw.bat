
@ECHO OFF
SET CMD_LINE_ARGS=%$
SET PATH=sbtw/bin:%PATH%
sbt %2 %*
      