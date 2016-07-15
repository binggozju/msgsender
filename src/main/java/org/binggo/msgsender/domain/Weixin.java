package org.binggo.msgsender.domain;

public class Weixin implements Message {
	
	// required fields, missing some fields may lead to get error response
	private String receivers;	// use a semicolon between multiple receivers
	private String content;
	
	// optional fields, null default
	private String source;
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public String getReceivers() {
		return receivers;
	}
	
	public void setReceivers(String receivers) {
		this.receivers = receivers;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return String.format("Weinxin [soruce=%s, receivers=%s]", source, receivers);
	}

}
