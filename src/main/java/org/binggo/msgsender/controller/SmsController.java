package org.binggo.msgsender.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;

import org.binggo.msgsender.domain.Sms;
import org.binggo.msgsender.service.SmsSenderService;
import org.binggo.msgsender.tools.FeedBack;
import org.binggo.msgsender.tools.SendResult;

@RestController
@RequestMapping("/sms")
public class SmsController {
	private static final Logger logger = LoggerFactory.getLogger(SmsController.class);
	
	private SmsSenderService smsSenderService;
	
	@Autowired
	public void setSmsSenderService(SmsSenderService smsSenderService) {
		this.smsSenderService = smsSenderService;
	}
	
	/**
	 * @param sms
	 * @throws Exception
	 */
	@RequestMapping(value="/sync", method=RequestMethod.POST, consumes={"application/json"})
	public String handleSyncSms(@RequestBody Sms sms) throws Exception {
		logger.info("Receive a sync sms request: " + sms.toString());
		
		if (sms.getPhone() == null || sms.getContent() == null) {
			logger.error("missing some fields in sync sms request");
			return FeedBack.MISSING_FIELDS.toString();
		}
		
		SendResult ret = smsSenderService.sendSyncMessage(sms);
		return ret.convertToFeedBack().toString();
	}

	@RequestMapping(value="/async", method=RequestMethod.POST, consumes={"application/json"})
	public String handleAsyncSms(@RequestBody Sms sms) throws Exception {
		logger.info("Receive a async sms request: " + sms.toString());
		
		if (sms.getPhone() == null || sms.getContent() == null) {
			logger.error("missing some fields in async sms request");
			return FeedBack.MISSING_FIELDS.toString();
		}

		smsSenderService.sendAsyncMessage(sms);
		return FeedBack.OK.toString();
	}
	
}
