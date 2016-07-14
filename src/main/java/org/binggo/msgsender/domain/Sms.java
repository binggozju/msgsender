package org.binggo.msgsender.domain;

public class Sms implements Message {
	
	private String phone;
	private String content;
	
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
