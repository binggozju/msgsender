#!/bin/bash
# -------------------------------------------------------------
# Description: test the msgsender system. 
# -------------------------------------------------------------

COUNT=2

HOST=http://localhost:8090

function send_weixin() {
	curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d "{\"receivers\": \"ybzhan\", \"content\": \"this is $1-th message\"}" $HOST/weixin/async 2> /dev/null
}

for((i=1; i<= $COUNT; i++)); do
	echo "$i"
	send_weixin $i
done

echo "complete the benchmark test."
