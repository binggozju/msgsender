package org.binggo.msgsender.domain;

public class Sms implements Message {
	
	// required fields, missing some fields may lead to get error response
	private String phone;
	private String content;
	
	// optional fields, null default
	private String source;
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String toString() {
		return String.format("Sms [phone=%s]", phone);
	}
	
}
