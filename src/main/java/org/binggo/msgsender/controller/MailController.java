package org.binggo.msgsender.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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
	public @ResponseBody FeedBack handleSyncMail(@RequestBody Mail mail) throws Exception {
		logger.debug("Receive a sync mail request: " + mail.toString());
		
		SendResult ret = mailSenderService.sendSyncMessage(mail);
		if (ret == SendResult.SUCCESS) {
			return new FeedBack(0, "ok");
		} else {
			return new FeedBack(1, "fail to send the mail");
		}
		
	}

	@RequestMapping(value="/async", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody FeedBack handleAsyncMail(@RequestBody Mail mail) throws Exception {
		logger.debug("Receive a async mail request: " + mail.toString());

		mailSenderService.sendAsyncMessage(mail);
		return new FeedBack(0, "ok");
	}
	
}
