package org.binggo.msgsender.service;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.binggo.msgsender.domain.Message;
import org.binggo.msgsender.tools.SendResult;

public abstract class AbstractSender implements Sender {
	
	private String name;
	
	private ThreadPoolTaskExecutor executor;
	
	{
		executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(SenderConstants.CORE_POOL_SIZE);
		executor.setMaxPoolSize(SenderConstants.MAX_POOL_SIZE);
		executor.setKeepAliveSeconds(SenderConstants.KEEPALIVE_SECONDS);
		executor.setQueueCapacity(SenderConstants.QUEUE_CAPACITY);
		executor.initialize();
	}
	
	public AbstractSender() {
		name = "sender";
	}
	
	public AbstractSender(String name) {
		this.name = name;
	}

	@Override
	public SendResult sendSyncMessage(Message message) {
		Future<SendResult> future = executor.submit(getSyncTask(message));
		
		try {
			SendResult ret = future.get(SenderConstants.SEND_TIMEOUT, TimeUnit.SECONDS);
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
