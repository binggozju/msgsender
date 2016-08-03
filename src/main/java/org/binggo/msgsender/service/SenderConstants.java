package org.binggo.msgsender.service;

public final class SenderConstants {

	// configuration for ThreadPoolTaskExecutor
	public static int CORE_POOL_SIZE_DEFAULT = 2;
	public static int MAX_POOL_SIZE_DEFAULT = 5;
	public static int KEEPALIVE_SECONDS_DEFAULT = 180;
	public static int QUEUE_CAPACITY_DEFAULT = 30;
	
	public static long SEND_TIMEOUT_DEFAULT = 10; // second
	public static int MAX_RETRY_TIMES_DEFAULT = 3;
	
	public static String SOURCE_DEFAULT = "监控平台";
	
	private SenderConstants() {
		// disable explicit object creation
	}

}
