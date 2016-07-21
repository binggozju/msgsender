package org.binggo.msgsender.service;

public final class SenderConstants {

	// configuration for ThreadPoolTaskExecutor
	public static int CORE_POOL_SIZE = 2;	// default: 1
	public static int MAX_POOL_SIZE = 8;	//default: Integer.MAX_VALUE
	public static int KEEPALIVE_SECONDS = 180;	// default: 60
	public static int QUEUE_CAPACITY = 30;	// default: Integer.MAX_VALUE
	
	public static long SEND_TIMEOUT = 10; // second
	public static int MAX_RETRY_TIMES = 3;
	
	public static String DEFAULT_SOURCE = "监控平台";
	
	private SenderConstants() {
		// disable explicit object creation
	}

}
