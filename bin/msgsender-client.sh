#!/bin/bash
# ----------------------------------------------------
# Description: used to send a message to msgsender.
# ----------------------------------------------------


HOST=http://localhost:8090


mail_msg=$(cat <<EOF
{
"subject": "监控告警",
"receivers": "ybzhan@ibenben.com",
"content": "this is a mail message from msgsender",

"source": "监控平台"
}
EOF
)


weixin_msg=$(cat <<EOF
{
"receivers": "ybzhan",
"content": "this is a weixin message",

"source": "监控平台"
}
EOF
)


curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d "$mail_msg" $HOST/mail/async 2> /dev/null

curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d "$weixin_msg" $HOST/weixin/async 2> /dev/null
