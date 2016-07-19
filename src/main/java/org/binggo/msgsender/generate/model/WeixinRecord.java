package org.binggo.msgsender.generate.model;

public class WeixinRecord {
    private String receivers;

    private String content;

    private String source;

    private Integer sendTime;

    private Byte sendResult;

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers == null ? null : receivers.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public Integer getSendTime() {
        return sendTime;
    }

    public void setSendTime(Integer sendTime) {
        this.sendTime = sendTime;
    }

    public Byte getSendResult() {
        return sendResult;
    }

    public void setSendResult(Byte sendResult) {
        this.sendResult = sendResult;
    }
}