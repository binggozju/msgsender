package org.binggo.msgsender.utils;

public enum FeedBack {
	OK (0, "ok"),
	
	// inner error of msgsender
	FAILURE	(1, "fail to send the message"),
	TIMEOUT	(2, "timeout to send"),
	INTERRUPT (3, "sending has been interrupted"),
	NOTSUPPORT (4, "this endpoint has not been suppported by now"),
	
	// error cause by callers
	MISSING_FIELDS (20, "missing some fields"),
	
	// other reasons
	UNKNOWN (999, "unknown result");

	private final int retCode;
	private final String retMessage;
	
	FeedBack(int retCode, String retMessage) {
		this.retCode = retCode;
		this.retMessage = retMessage;
	}
	
	public int getRetCode() {
		return retCode;
	}
	
	public String getRetMessage() {
		return retMessage;
	}
	
	@Override
	public String toString() {
		return String.format("{\"retcode\": \"%s\", \"retmessage\": \"%s\"}", retCode, retMessage);
	}
}
