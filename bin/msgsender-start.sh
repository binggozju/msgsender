#!/bin/bash
# ---------------------------------------------------------------------
# Description: used to configure the paramters of JVM and msgsender,
#	start msgsender service
# ---------------------------------------------------------------------


if [ $# -ne 1 ]; then
	echo "Usage: $0 local|pre-release|production"
	exit 1
fi

ENV=$1
case $ENV in
	local|pre-release|production)
	;;

	*)
	echo "Error: unvalid parameter"
	echo "Usage: $0 local|pre-release|production"
	exit 1
	;;
esac

DIR=$(dirname `readlink -m $0`)
cd $DIR/..; pwd

# configuration of JVM
JAVA_OPTS="-Xms512m -Xmx1g"
echo "JAVA_OPTS: ${JAVA_OPTS}"

# configuration of msgsender
MSGSENDER_OPTS="--spring.config.location=config/msgsender.properties,${ENV}.properties"
echo "MSGSENDER_OPTS: ${MSGSENDER_OPTS}"

if [ $ENV = local ]; then
	echo "in the local environment"
	exec java $JAVA_OPTS -jar target/msgsender.jar $MSGSENDER_OPTS
else
	echo "in the pre-release|production environment" 
	exec java $JAVA_OPTS -jar libs/msgsender.jar $MSGSENDER_OPTS
fi
