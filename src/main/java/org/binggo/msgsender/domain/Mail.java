package org.binggo.msgsender.domain;

public class Mail implements Message {
	
	private String subject;
	private String to;
	private String content;
	
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
		return String.format("Mail [subject=%s, to=%s]", subject, to);
	}
	
}
