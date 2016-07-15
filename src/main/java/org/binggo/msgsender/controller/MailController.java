package org.binggo.msgsender.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;

import org.binggo.msgsender.domain.Mail;
import org.binggo.msgsender.service.MailSenderService;
import org.binggo.msgsender.tools.FeedBack;
import org.binggo.msgsender.tools.SendResult;

@RestController
@RequestMapping("/mail")
public class MailController {
	
	private static final Logger logger = LoggerFactory.getLogger(MailController.class);

	private MailSenderService mailSenderService;
	
	@Autowired
	public void setMailSenderService(MailSenderService mailSenderService) {
		this.mailSenderService = mailSenderService;
	}
	
	/**
	 * @param mail
	 * @throws Exception
	 */
	@RequestMapping(value="/sync", method=RequestMethod.POST, consumes={"application/json"})
	public String handleSyncMail(@RequestBody Mail mail) throws Exception {
		logger.info("Receive a sync mail request: " + mail.toString());
		
		if (mail.getSubject() == null || mail.getTo() == null || mail.getContent() == null) {
			logger.error("missing some fields in sync mail request");
			return FeedBack.MISSING_FIELDS.toString();
		} 
		
		SendResult ret = mailSenderService.sendSyncMessage(mail);
		return ret.convertToFeedBack().toString();
	}

	@RequestMapping(value="/async", method=RequestMethod.POST, consumes={"application/json"})
	public String handleAsyncMail(@RequestBody Mail mail) throws Exception {
		logger.info("Receive a async mail request: " + mail.toString());
		
		if (mail.getSubject() == null || mail.getTo() == null || mail.getContent() == null) {
			logger.error("missing some fields in async mail request");
			return FeedBack.MISSING_FIELDS.toString();
		} 

		mailSenderService.sendAsyncMessage(mail);
		return FeedBack.OK.toString();
	}
	
}
