package org.binggo.msgsender.utils;

public enum SendResult {
	OK (0), 
	FAILURE (1),
	TIMEOUT (2),
	INTERRUPT (3);
	
	private final int code;
	
	SendResult(int code) {
		this.code = code;
	}
	
	public int getCode () {
		return code;
	}
	
	public FeedBack convertToFeedBack() {
		switch (this) {
			case OK:
				return FeedBack.OK;
			case INTERRUPT:
				return FeedBack.INTERRUPT;
			case TIMEOUT:
				return FeedBack.TIMEOUT;
			case FAILURE:
				return FeedBack.FAILURE;
			default:
				return FeedBack.UNKNOWN;
		}
	}

}
