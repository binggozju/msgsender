package org.binggo.msgsender.service;

import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

import org.binggo.msgsender.domain.Sms;
import org.binggo.msgsender.domain.Message;
import org.binggo.msgsender.tools.SendResult;

@Service
@Configurable
public class SmsSenderService extends AbstractSender {
	
	private static final Logger logger = LoggerFactory.getLogger(SmsSenderService.class);
	
	public SmsSenderService() {
		super("sms-sender");
	}
	

	@Override
	protected Callable<SendResult> getSyncTask(Message message) {
		if (message != null && message instanceof Sms) {
			return new SyncSmsTask((Sms) message);
		} else {
			logger.error("unvalid sms object");
			return null;
		}
	}

	@Override
	protected Runnable getAsyncTask(Message message) {
		if (message != null && message instanceof Sms) {
			return new AsyncSmsTask((Sms) message);
		} else {
			logger.error("unvalid sms object");
			return null;
		}
	}
	
	private class SyncSmsTask implements Callable<SendResult> {
		private Sms sms;
		
		public SyncSmsTask(Sms sms) {
			this.sms = sms;
		}

		@Override
		public SendResult call() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	private class AsyncSmsTask implements Runnable {
		private Sms sms;
		
		public AsyncSmsTask(Sms sms) {
			this.sms = sms;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	}

}
