package org.binggo.msgsender.service;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.binggo.msgsender.domain.Message;
import org.binggo.msgsender.utils.SendResult;

public abstract class AbstractSender implements Sender {
	
	private String name;
	
	private ThreadPoolTaskExecutor executor;
	
	public AbstractSender(Environment env) {
		name = "sender";
		
		executor = new ThreadPoolTaskExecutor();
		
		Integer corePoolSize = env.getProperty("sender.threadpool.core-pool-size", Integer.class);
		if (corePoolSize != null) {
			executor.setCorePoolSize(corePoolSize);
		} else {
			executor.setCorePoolSize(SenderConstants.CORE_POOL_SIZE_DEFAULT);
		}
		
		Integer maxPoolSize = env.getProperty("sender.threadpool.max-pool-size", Integer.class);
		if (maxPoolSize != null) {
			executor.setMaxPoolSize(maxPoolSize);
		} else {
			executor.setMaxPoolSize(SenderConstants.MAX_POOL_SIZE_DEFAULT);
		}
		
		Integer keepAliveSeconds = env.getProperty("sender.threadpool.keepalive-seconds", Integer.class);
		if (keepAliveSeconds != null) {
			executor.setKeepAliveSeconds(keepAliveSeconds);
		} else {
			executor.setKeepAliveSeconds(SenderConstants.KEEPALIVE_SECONDS_DEFAULT);
		}
		
		Integer queueCapacity = env.getProperty("sender.threadpool.queue-capacity", Integer.class);
		if (queueCapacity != null) {
			executor.setQueueCapacity(queueCapacity);
		} else {
			executor.setQueueCapacity(SenderConstants.QUEUE_CAPACITY_DEFAULT);
		}
		
		executor.initialize();
	}
	
	public AbstractSender(String name, Environment env) {
		this.name = name;
		
		executor = new ThreadPoolTaskExecutor();
		
		Integer corePoolSize = env.getProperty("sender.threadpool.core-pool-size", Integer.class);
		if (corePoolSize != null) {
			executor.setCorePoolSize(corePoolSize);
		} else {
			executor.setCorePoolSize(SenderConstants.CORE_POOL_SIZE_DEFAULT);
		}
		
		Integer maxPoolSize = env.getProperty("sender.threadpool.max-pool-size", Integer.class);
		if (maxPoolSize != null) {
			executor.setMaxPoolSize(maxPoolSize);
		} else {
			executor.setMaxPoolSize(SenderConstants.MAX_POOL_SIZE_DEFAULT);
		}
		
		Integer keepAliveSeconds = env.getProperty("sender.threadpool.keepalive-seconds", Integer.class);
		if (keepAliveSeconds != null) {
			executor.setKeepAliveSeconds(keepAliveSeconds);
		} else {
			executor.setKeepAliveSeconds(SenderConstants.KEEPALIVE_SECONDS_DEFAULT);
		}
		
		Integer queueCapacity = env.getProperty("sender.threadpool.queue-capacity", Integer.class);
		if (queueCapacity != null) {
			executor.setQueueCapacity(queueCapacity);
		} else {
			executor.setQueueCapacity(SenderConstants.QUEUE_CAPACITY_DEFAULT);
		}
		
		executor.initialize();
	}

	@Override
	public SendResult sendSyncMessage(Message message) {
		Future<SendResult> future = executor.submit(getSyncTask(message));
		
		try {
			SendResult ret = future.get(SenderConstants.SEND_TIMEOUT_DEFAULT, TimeUnit.SECONDS);
			return ret;
		} catch (ExecutionException ex) {
			return SendResult.FAILURE;
		} catch (InterruptedException ex) {
			return SendResult.INTERRUPT;
		} catch (TimeoutException ex) {
			return SendResult.TIMEOUT;
		}
	}

	@Override
	public void sendAsyncMessage(Message message) {
		executor.execute(getAsyncTask(message));
	}
	
	protected abstract Callable<SendResult> getSyncTask(Message message);
	
	protected abstract Runnable getAsyncTask(Message message);
	
	public String toString() {
		return this.getClass().getName() + "{name: " + name + "}";
	}

}
