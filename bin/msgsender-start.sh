#!/bin/bash
# ---------------------------------------------------------------------
# Description: used to configure the paramters of JVM and msgsender,
#	start msgsender service
# ---------------------------------------------------------------------

DIR=$(dirname `readlink -m $0`)
cd $DIR/..; pwd

# configuration of JVM
JAVA_OPTS=

# configuration of msgsender
MSGSENDER_OPTS="--spring.config.location=config/msgsender.properties"

exec java $JAVA_OPTS -jar target/msgsender-0.0.1-SNAPSHOT.jar $MSGSENDER_OPTS
