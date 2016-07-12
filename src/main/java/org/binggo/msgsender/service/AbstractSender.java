package org.binggo.msgsender.service;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.binggo.msgsender.domain.Message;
import org.binggo.msgsender.tools.SendResult;

public abstract class AbstractSender implements Sender {
	
	private String name;
	
	private AsyncTaskExecutor executor;
	
	public AbstractSender() {
		name = "sender";
		executor = new SimpleAsyncTaskExecutor(name);
	}
	
	public AbstractSender(String name) {
		this.name = name;
		executor = new SimpleAsyncTaskExecutor(this.name);
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
