#!/bin/bash
# ---------------------------------------------------------------------
# Description: used to configure the paramters of JVM and msgsender,
#	start msgsender service
# ---------------------------------------------------------------------

DIR=$(dirname `readlink -m $0`)
cd $DIR/..; pwd

# configuration of JVM
# TODO

# configuration of msgsender
# TODO

exec java -jar target/msgsender-0.0.1-SNAPSHOT.jar
