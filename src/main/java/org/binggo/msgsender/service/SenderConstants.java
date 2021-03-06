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
	
	// the access token in WeixinSenderService will be updated every hour
	public static int TOKEN_UPDATE_SECONDS_DEFAULT = 1800;
	// the size of thread pool for ThreadPoolTaskScheduler
	public static int SCHEDULE_POOL_SIZE_DEFAULT = 1;
	
	private SenderConstants() {
		// disable explicit object creation
	}

}
