package org.binggo.msgsender.domain;

public class Mail implements Message {
	
	private String subject;
	private String to;
	private String body;
	
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
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	@Override
	public String toString() {
		return String.format("Mail [subject=%s, to=%s]", subject, to);
	}
	
}
