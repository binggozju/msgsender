package org.binggo.msgsender.service;

public final class SenderConstants {
	public static long SEND_TIMEOUT = 10; // second
	
	public static int MAX_RETRY_TIMES = 3;
	
	public static String DEFAULT_SOURCE = "监控平台";
	
	private SenderConstants() {
		// disable explicit object creation
	}

}
