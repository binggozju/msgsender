#!/bin/bash

HOST=http://localhost:8090

curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"source": "msgsender-client", "subject":"mail sender of msgsender", "content": "a sync mail"}' $HOST/mail/sync
#curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"source": "msgsender客户端", "subject":"mail sender of msgsender", "receivers": "ybzhan@ibenben.com", "content": "a sync mail"}' $HOST/mail/sync

#curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"source": "msgsender-client", "subject":"mail sender of msgsender", "receivers": "ybzhan@ibenben.com;470138367@qq.com", "content": "an async mail"}' $HOST/mail/async

#curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"source": "msgsender-client", "receivers": "xxx", "content": "a sync weixin message"}' $HOST/weixin/sync




