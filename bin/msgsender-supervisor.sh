#!/bin/bash
# ---------------------------------------------------------------------------
# Description: supervisor msgsender's running status.
#   add to crontab: Crontab: */2 * * * *  /your/path/msgsender-supervisor.sh
# ---------------------------------------------------------------------------


# configuration of log file
SUPERVISOR_LOG_FILE=/data0/logs/msgsender/supervisor.log
MSGSENDER_LOG_FILE=/dev/null

function log() {
    local log_level=$1
    local log_msg=$2
    local date_str=$(date +"%Y-%m-%d %H:%M:%S")
    echo "[$date_str] [$log_level] $log_msg" >> $SUPERVISOR_LOG_FILE
}

EXIST=$(ps aux | grep "msgsender.properties" | grep -v grep | wc -l)
if [ $EXIST -gt 0 ]; then
    log INFO "msgsender is running"
else
    log ERROR "msgsender has stopped"
    # use the correct location of msgsender-start.sh
    nohup /your/path/msgsender-start.sh 2>&1 >> $MSGSENDER_LOG_FILE &
    log INFO "msgsender-supervisor has restarted msgsender"
fi

