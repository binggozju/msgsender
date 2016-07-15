package org.binggo.msgsender.domain;

public class Mail implements Message {
	
	// required fields, missing some fields may lead to get error response
	private String subject;
	private String to;	// use a semicolon between multiple addresses
	private String content;
	
	// optional fields, null default
	private String source;
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getTo() {
		return to;
	}
	
	public void setTo(String to) {
		this.to = to;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return String.format("Mail [source=%s, subject=%s, to=%s]", source, subject, to);
	}
	
}
