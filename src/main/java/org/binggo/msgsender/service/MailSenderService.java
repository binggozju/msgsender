package org.binggo.msgsender.service;

import java.util.concurrent.Callable;
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
import org.binggo.msgsender.generate.mapper.MailRecordMapper;
import org.binggo.msgsender.generate.model.MailRecord;
import org.binggo.msgsender.utils.SendResult;

@Service
@Configurable
public class MailSenderService extends AbstractSender {
	
	private static final Logger logger = LoggerFactory.getLogger(MailSenderService.class);
	
	@Value("${spring.mail.username}")
	private String from;
	
	private JavaMailSender mailSender;
	
	@Autowired
	private MailRecordMapper mailRecordMapper;
	
	public MailSenderService() {
		super("mail-sender");
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

	private static MailRecord generateMailRecord(Mail mail, int sendResult) {
		MailRecord record = new MailRecord();
		
		record.setSubject(mail.getSubject());
		record.setReceivers(mail.getReceivers());
		record.setContent(mail.getContent());
		if (mail.getSource() != null) {
			record.setSource(mail.getSource());
		} else {
			record.setSource(SenderConstants.DEFAULT_SOURCE);
		}
		if (mail.getSendTime() != 0) {
			record.setSendTime(mail.getSendTime());
		} else {
			long nowTime = System.currentTimeMillis() / 1000;
			record.setSendTime((int) nowTime);
		}
		record.setSendResult((byte) sendResult);
		
		return record;
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
				// send the mail
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
				
				helper.setFrom(from);
				helper.setTo(mail.getReceivers().split(";"));
				
				if (mail.getSource() != null) {
					helper.setSubject(String.format("[%s] %s", mail.getSource(), mail.getSubject()));
				} else {
					helper.setSubject(String.format("[%s] %s", SenderConstants.DEFAULT_SOURCE, mail.getSubject()));
				}
				
				helper.setText(mail.getContent());
				
				mailSender.send(mimeMessage);
				logger.info("send the sync mail successfully: " + mail.toString());
				
				// save the info about this mail to mysql
				MailRecord record = MailSenderService.generateMailRecord(mail, SendResult.OK.getCode());
				mailRecordMapper.insert(record);
				logger.info("save the sync mail record successfully");
				
				return SendResult.OK;
				
			} catch (Exception ex) {
				logger.error("fail to send mail: " + ex.getMessage());
				
				// save the info about this mail to mysql
				MailRecord record = MailSenderService.generateMailRecord(mail, SendResult.FAILURE.getCode());
				mailRecordMapper.insert(record);
				logger.info("save the sync mail record successfully");
				
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
				// send the mail
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
				
				helper.setFrom(from);
				helper.setTo(mail.getReceivers().split(";"));
				
				if (mail.getSource() != null) {
					helper.setSubject(String.format("[%s] %s", mail.getSource(), mail.getSubject()));
				} else {
					helper.setSubject(String.format("[%s] %s", SenderConstants.DEFAULT_SOURCE, mail.getSubject()));
				}
				
				helper.setText(mail.getContent());
				
				mailSender.send(mimeMessage);
				logger.info("send the async mail successfully");
				
				// save the info about this mail to mysql
				MailRecord record = MailSenderService.generateMailRecord(mail, SendResult.OK.getCode());
				mailRecordMapper.insert(record);
				logger.info("save the async mail record successfully");
				
			} catch (Exception ex) {
				logger.error("fail to send mail: " + ex.getMessage());
				
				// save the info about this mail to mysql
				MailRecord record = MailSenderService.generateMailRecord(mail, SendResult.FAILURE.getCode());
				mailRecordMapper.insert(record);
				logger.info("save the async mail record successfully");
			}
		}
	}

}