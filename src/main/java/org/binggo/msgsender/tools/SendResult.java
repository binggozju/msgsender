package org.binggo.msgsender.tools;

public enum SendResult {
	OK, 
	INTERRUPT, 
	TIMEOUT,
	FAILURE;
	
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
