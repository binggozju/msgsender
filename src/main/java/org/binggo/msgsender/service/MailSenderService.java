package org.binggo.msgsender.service;

import java.util.concurrent.Callable;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.binggo.msgsender.domain.Mail;
import org.binggo.msgsender.domain.Message;
import org.binggo.msgsender.tools.SendResult;

@Service
@Configurable
public class MailSenderService extends AbstractSender {
	
	private static final Logger logger = LoggerFactory.getLogger(MailSenderService.class);
	
	@Value("${spring.mail.username}")
	private String from;
	
	private JavaMailSender mailSender;
	
	public MailSenderService() {
		super("mailsender");
	}
	
	@Autowired
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	@Override
	protected Callable<SendResult> getSyncTask(Message message) {
		if (message != null && message instanceof Mail) {
			return new SyncMailTask((Mail) message);
		} else {
			logger.error("unvalid mail object");
			return null;
		}
	}
	
	@Override
	protected Runnable getAsyncTask(Message message) {
		if (message != null && message instanceof Mail) {
			return new AsyncMailTask((Mail) message);
		} else {
			logger.error("unvalid mail object");
			return null;
		}
	}

	private class SyncMailTask implements Callable<SendResult> {
		private Mail mail;
		
		public SyncMailTask(Mail mail) {
			this.mail = mail;
		}

		@Override
		public SendResult call() throws Exception {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			try {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
				
				helper.setFrom(from);
				helper.setTo(mail.getTo());
				helper.setSubject(mail.getSubject());
				helper.setText(mail.getBody());
				
				mailSender.send(mimeMessage);
				return SendResult.SUCCESS;
				
			} catch (MessagingException ex) {
				logger.error("fail to send mail: " + ex.getMessage());
				return SendResult.FAILURE;
			}
		}
	}
	
	private class AsyncMailTask implements Runnable {
		private Mail mail;
		
		public AsyncMailTask(Mail mail) {
			this.mail = mail;
		}

		@Override
		public void run() {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			try {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
				
				helper.setFrom(from);
				helper.setTo(mail.getTo());
				helper.setSubject(mail.getSubject());
				helper.setText(mail.getBody());
				
				mailSender.send(mimeMessage);
			} catch (MessagingException ex) {
				logger.error("fail to send mail: " + ex.getMessage());
			}
		}
	}

}