package org.binggo.msgsender.service;

import org.binggo.msgsender.domain.Message;
import org.binggo.msgsender.tools.SendResult;

public interface Sender {
	
	/**
	 * send the given message synchronously.
	 * @param message mail message, weixin message, sms message, etc
	 * @return the result of sending the message
	 * @throws SenderException
	 */
	SendResult sendSyncMessage(Message message);
	
	/**
	 * send the given message asynchronously.
	 * @param message mail message, weixin message, sms message, etc
	 */
	void sendAsyncMessage(Message message);
}
