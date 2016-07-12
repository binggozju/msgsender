package org.binggo.msgsender.tools;

public class FeedBack {
	private int retCode;
	private String retMessage;
	
	public FeedBack(int retCode, String retMessage) {
		this.retCode = retCode;
		this.retMessage = retMessage;
	}
	
	public int getRetCode() {
		return retCode;
	}
	
	public String getRetMessage() {
		return retMessage;
	}
}
