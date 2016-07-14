package org.binggo.msgsender.service;

import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

import org.binggo.msgsender.domain.Weixin;
import org.binggo.msgsender.domain.Message;
import org.binggo.msgsender.tools.SendResult;

@Service
@Configurable
public class WeixinSenderService extends AbstractSender {
	
	private static final Logger logger = LoggerFactory.getLogger(WeixinSenderService.class);
	
	public WeixinSenderService() {
		super("weixin-sender");
	}
	

	@Override
	protected Callable<SendResult> getSyncTask(Message message) {
		if (message != null && message instanceof Weixin) {
			return new SyncWeixinTask((Weixin) message);
		} else {
			logger.error("unvalid weixin object");
			return null;
		}
	}

	@Override
	protected Runnable getAsyncTask(Message message) {
		if (message != null && message instanceof Weixin) {
			return new AsyncWeixinTask((Weixin) message);
		} else {
			logger.error("unvalid weixin object");
			return null;
		}
	}
	
	
	private class SyncWeixinTask implements Callable<SendResult> {
		private Weixin weixin;
		
		public SyncWeixinTask(Weixin weixin) {
			this.weixin = weixin;
		}
			
		@Override
		public SendResult call() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
	} 
	
	private class AsyncWeixinTask implements Runnable {
		private Weixin weixin;
		
		public AsyncWeixinTask(Weixin weixin) {
			this.weixin = weixin;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	}

}
